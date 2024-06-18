package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageBotLoadListener implements MessageListener {
    private ObservableList<Message> messages;
    private Gson gson = new Gson();

    public MessageBotLoadListener(ObservableList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void onMessageReceived(String message) {
        String[] messageParts = message.split("\\|");
        if(message.startsWith("loadHistoryBot")){
            Platform.runLater(()->{
                List<Message> messageList=gson.fromJson(messageParts[1], new TypeToken<List<Message>>(){}.getType());
                messages.setAll(messageList);
            });
        }
    }
}
