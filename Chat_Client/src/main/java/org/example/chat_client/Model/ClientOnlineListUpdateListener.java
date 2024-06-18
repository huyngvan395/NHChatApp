package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

public class ClientOnlineListUpdateListener implements MessageListener {
    private final Gson gson = new Gson();
    private final ObservableList<Client> clientOnlineList;

    public ClientOnlineListUpdateListener(ObservableList<Client> clientOnlineList) {
        this.clientOnlineList = clientOnlineList;
    }

    @Override
    public void onMessageReceived(String message) {
        if(Model.getInstance().isRunning()){
            String[] messageParts = message.split("\\|");

            if (message.startsWith("newClientOnline")) {
                Platform.runLater(() -> {
                    Client newClient = gson.fromJson(messageParts[1], Client.class);
                    clientOnlineList.add(newClient);
                });
            } else if (message.startsWith("listOnline")) {
                Platform.runLater(() -> {
                    List<Client> clientList = gson.fromJson(messageParts[1], new TypeToken<List<Client>>() {}.getType());
                    clientOnlineList.setAll(clientList);
                });
            } else if (message.startsWith("removeClientOnline")) {
                Platform.runLater(() -> {
                    String clientId = messageParts[1];
                    clientOnlineList.removeIf(client -> client.getClientID().equals(clientId));
                });
            }
        }
    }
}
