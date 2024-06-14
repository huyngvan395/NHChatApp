package org.example.chat_client.Model;

import com.google.gson.Gson;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.chat_client.View.ViewFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final SocketClient socketClient;
    private ObservableList<Client> clientOnlineList;
    private ObservableList<Client> listClient;
    private Client currentClient;
    private volatile boolean running;
    private ObjectProperty<Client> targetClient=new SimpleObjectProperty<>();
    private final BlockingQueue<String> messageResponseQueue;
    private final MessageHandler messageHandler;

    private Model() throws IOException {
        this.viewFactory = new ViewFactory();
        this.socketClient= new SocketClient();
        this.clientOnlineList= FXCollections.observableArrayList();
        this.listClient=FXCollections.observableArrayList();
        running = true;
        this.messageHandler = new MessageHandler();
        this.messageResponseQueue = new LinkedBlockingQueue<>(1);
        this.messageHandler.addMessageListener(new ClientOnlineListUpdateListener(clientOnlineList));
        this.messageHandler.addMessageListener(new ClientListUpdateListener(listClient));
    }

    public static synchronized Model getInstance() {
        try{
            if(model==null){
                model=new Model();
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

    public ObservableList<Client> getClientOnlineList() {
        return clientOnlineList;
    }

    public ObservableList<Client> getListClient(){
        return listClient;
    }

    public void startMessageReader() {
        new Thread(() -> {
            while (running) {
                try {
                    String message = socketClient.receiveResponse();
                    System.out.println(message);
                    if (message != null && !message.trim().isEmpty()) {
                        messageResponseQueue.put(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void processMessages() {
        new Thread(()->{
            while (running) {
                String message= null;
                try {
                    message = messageResponseQueue.take();
                    if (message != null && !message.trim().isEmpty()) {
                        this.messageHandler.handleMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public ObjectProperty<Client> targetClientObjectProperty(){
        return targetClient;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void resetData(){
        this.currentClient=null;
        this.listClient.clear();
        this.clientOnlineList.clear();
    }

    public BlockingQueue<String> getMessageResponseQueue() {
        return messageResponseQueue;
    }
}
