package org.example.chat_client.Controller.Client.Message;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.chat_client.Model.MessageListener;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MessageChatBoxBotController implements Initializable, MessageListener {
    public ScrollPane scrollPane;
    @FXML
    private TextArea Enter_message;
    @FXML
    private Button sendMessage_btn;
    @FXML
    private VBox content_chat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendMessage_btn.setOnAction(e->{
            sendMessage();
            Enter_message.setText("");
            Enter_message.requestFocus();
        });
        Model.getInstance().getMessageHandler().addMessageListener(this);
    }

    public void sendMessage() {
        if(!Enter_message.getText().isEmpty()){
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = dateTimeFormatter.format(localDateTime);
            AnchorPane messageContain= Model.getInstance().getViewFactory().getMessageSend(Enter_message.getText(), formattedDate);
            content_chat.getChildren().add(messageContain);
            Model.getInstance().getSocketClient().sendMessage("chatbot/"+Enter_message.getText()+"/"+Model.getInstance().getCurrentClient().getClientID());
        }
    }

    @Override
    public void onMessageReceived(String message) {
        if(Model.getInstance().isRunning()){
            Platform.runLater(()->{
                String[] messageParts = message.split("/");
                if (message.startsWith("chatbot_response")) {
                    String messageResponse = messageParts[1].replace("<br>","\n");
                    String time_created = messageParts[2];
                    System.out.println(message + " " + time_created);
                    Platform.runLater(() -> {
                        AnchorPane messageReceiveChatBot = Model.getInstance().getViewFactory().getMessageSingleReceive(messageResponse, time_created);
                        content_chat.getChildren().add(messageReceiveChatBot);
                    });
                }
            });
        }
    }
}
