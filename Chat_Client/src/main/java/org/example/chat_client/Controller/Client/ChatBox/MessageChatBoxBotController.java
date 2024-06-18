package org.example.chat_client.Controller.Client.ChatBox;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.chat_client.Model.DateTimeFormatMessage;
import org.example.chat_client.Model.Message;
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
        loadHistoryMessage();
        content_chat.heightProperty().addListener(((observable, oldValue, newValue) -> scrollToBottom()));
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
            String timeShow= DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane messageContain= Model.getInstance().getViewFactory().getMessageSend(Enter_message.getText(), timeShow);
            content_chat.getChildren().add(messageContain);
            Model.getInstance().getSocketClient().sendMessage("chatbot/"+Enter_message.getText()+"/"+Model.getInstance().getCurrentClient().getClientID()+"/"+localDateTime);
        }
    }

    @Override
    public void onMessageReceived(String message) {
        String currentClientID = Model.getInstance().getCurrentClient().getClientID();
        if(Model.getInstance().isRunning()){
            Platform.runLater(()->{
                String[] messageParts = message.split("/");
                if (message.startsWith("chatbot_response")) {
                    String messageResponse = messageParts[1].replace("<br>","\n");
                    String time_created = messageParts[2];
                    String timeShow = DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(time_created));
                    System.out.println(message + " " + timeShow);
                    AnchorPane messageReceiveChatBot = Model.getInstance().getViewFactory().getMessageSingleReceive(messageResponse, timeShow);
                    content_chat.getChildren().add(messageReceiveChatBot);
                }else if(message.startsWith("loadHistoryBot")) {
                    content_chat.getChildren().clear();
                    content_chat.requestLayout();
                    if(Model.getInstance().getMessageListBot()!=null){
                        for(Message m : Model.getInstance().getMessageListBot()){
                            Platform.runLater(()->{
                                if(m.getSenderID().equals(currentClientID)){
                                    String timeShow= DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(m.getTimeSend()));
                                    AnchorPane textPane=Model.getInstance().getViewFactory().getMessageSend(m.getMessage(), timeShow);
                                    content_chat.getChildren().add(textPane);
                                }else{
                                    String timeShow= DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(m.getTimeSend()));
                                    AnchorPane textPane=Model.getInstance().getViewFactory().getMessageSingleReceive(m.getMessage().replace("<br>","\n"), timeShow);
                                    content_chat.getChildren().add(textPane);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void loadHistoryMessage(){
        String currentClientID=Model.getInstance().getCurrentClient().getClientID();
        Model.getInstance().getSocketClient().sendMessage("load_history_bot/"+currentClientID);
    }

    private void scrollToBottom(){
        Platform.runLater(()->{
            content_chat.layout();
            scrollPane.setVvalue(1.0);
        });
    }
}
