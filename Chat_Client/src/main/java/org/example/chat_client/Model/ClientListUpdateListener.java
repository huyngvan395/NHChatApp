package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

public class ClientListUpdateListener implements MessageListener{
    private final Gson gson=new Gson();
    private final ObservableList<Client> clientList;

    public ClientListUpdateListener(ObservableList<Client> clientList){
        this.clientList=clientList;
    }
    @Override
    public void onMessageReceived(String message) {
        String[] messageParts=message.split("\\|");
        if(message.startsWith("listClient")){
            Platform.runLater(()->{
                List<Client> list=gson.fromJson(messageParts[1], new TypeToken<List<Client>>(){}.getType());
                clientList.setAll(list);
            });
        }else if(message.startsWith("removeClientInList")){
            Platform.runLater(()->{
                String clientID=messageParts[1];
                clientList.removeIf(client -> client.getClientID().equals(clientID));
            });
        }
    }
}
