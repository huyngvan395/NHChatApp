package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.example.chat_client.View.ViewFactory;

import java.io.IOException;
import java.util.List;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final SocketClient socketClient;
    private ObservableList<Client> clientOnlineList;
    private final Gson gson;
    private Client currentClient;
    private boolean running;

    private Model() throws IOException {
        this.viewFactory = new ViewFactory();
        this.socketClient= new SocketClient();
        this.clientOnlineList= FXCollections.observableArrayList();
        this.gson = new Gson();
        running = true;
    }

    public static synchronized Model getInstance() {
        try{
            if (model == null) {
                model = new Model();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void addClientOnline(Client client) {
        clientOnlineList.add(client);
    }

    public void removeClientOnline(String ClientID) {
        this.clientOnlineList.removeIf(client -> client.getClientID().equals(ClientID));
    }

    public ObservableList<Client> getClientOnlineList() {
        return clientOnlineList;
    }

    public void updateClientOnline(){
        new Thread(()->{
            while(running){
                String message=Model.getInstance().getSocketClient().receiveResponse();
                if (message == null || message.trim().isEmpty()) {
                    continue;
                }
                String[] messageParts=message.split("/");
                if(message.startsWith("newClient")){
                    System.out.println(message);
                    Platform.runLater(()->{
                        Client newClient=gson.fromJson(messageParts[1], Client.class);
                        this.addClientOnline(newClient);
                    });
                }
                else if(message.startsWith("listOnline")){
                    System.out.println(message);
                    Platform.runLater(()->{
                        List<Client> clientList=gson.fromJson(messageParts[1], new TypeToken<List<Client>>(){}.getType());
                        this.clientOnlineList.setAll(clientList);
                    });
                }
                else if(message.startsWith("removeClientOnline")){
                    Platform.runLater(()->this.removeClientOnline(messageParts[1]));
                }
            }
        }).start();
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
