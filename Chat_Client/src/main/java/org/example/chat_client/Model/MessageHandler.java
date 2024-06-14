package org.example.chat_client.Model;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private final List<MessageListener> listeners = new ArrayList<>();

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void handleMessage(String message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }

}
