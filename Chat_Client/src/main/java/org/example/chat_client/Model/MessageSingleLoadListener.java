package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

public class MessageSingleLoadListener implements MessageListener {
    private Gson gson = new Gson();
    private ObservableList<Message> messages;

    public MessageSingleLoadListener(ObservableList<Message> messages) {
        this.messages = messages;
    }
    @Override
    public void onMessageReceived(String message) {
        String[] messageParts= message.split("\\|");
        if(message.startsWith("loadHistorySingle")){
            Platform.runLater(()->{
                List<Message> messageList=gson.fromJson(messageParts[1], new TypeToken<List<Message>>(){}.getType());
                messages.setAll(messageList);
            });
        }
    }
}
