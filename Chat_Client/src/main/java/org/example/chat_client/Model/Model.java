package org.example.chat_client.Model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.chat_client.View.ViewFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final SocketClient socketClient;
    private final SocketStreaming socketStreaming;
    private ObservableList<Client> clientOnlineList;
    private ObservableList<Client> listClient;
    private ObservableList<Group> groupList;
    private ObservableList<Message> messageListSingle;
    private ObservableList<Message> messageListGroup;
    private ObservableList<Message> messageListBot;
    private Client currentClient;
    private volatile boolean running;
    private ObjectProperty<Client> targetClient=new SimpleObjectProperty<>();
    private ObjectProperty<Caller> targetCall = new SimpleObjectProperty<>();
    private final BlockingQueue<String> messageResponseQueue;
    private final MessageHandler messageHandler;
    private ObjectProperty<Group> targetGroup=new SimpleObjectProperty<>();

    private Model() throws IOException {
        this.viewFactory = new ViewFactory();
        this.socketClient= new SocketClient();
        this.socketStreaming = new SocketStreaming();
        this.clientOnlineList= FXCollections.observableArrayList();
        this.listClient=FXCollections.observableArrayList();
        this.groupList=FXCollections.observableArrayList();
        this.messageListSingle=FXCollections.observableArrayList();
        this.messageListGroup=FXCollections.observableArrayList();
        this.messageListBot=FXCollections.observableArrayList();
        this.running = true;
        this.messageHandler = new MessageHandler();
        this.messageResponseQueue = new LinkedBlockingQueue<>(1);
        this.messageHandler.addMessageListener(new ClientOnlineListUpdateListener(clientOnlineList));
        this.messageHandler.addMessageListener(new ClientListUpdateListener(listClient));
        this.messageHandler.addMessageListener(new GroupListUpdateListener(groupList));
        this.messageHandler.addMessageListener(new MessageSingleLoadListener(messageListSingle));
        this.messageHandler.addMessageListener(new MessageGroupLoadListener(messageListGroup));
        this.messageHandler.addMessageListener(new MessageBotLoadListener(messageListBot));
        this.messageHandler.addMessageListener(new CallToClientListener());
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

    public SocketStreaming getSocketStreaming() {
        return socketStreaming;
    }

    public ObservableList<Client> getClientOnlineList() {
        return clientOnlineList;
    }

    public ObservableList<Client> getListClient(){
        return listClient;
    }

    public ObservableList<Group> getGroupList() {
        return groupList;
    }

    public ObservableList<Message> getMessageListSingle(){
        return messageListSingle;
    }

    public ObservableList<Message> getMessageListGroup(){
        return messageListGroup;
    }

    public ObservableList<Message> getMessageListBot(){
        return messageListBot;
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
                    if (!message.trim().isEmpty()) {
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

    public ObjectProperty<Group> targetGroupObjectProperty(){
        return targetGroup;
    }

    public ObjectProperty<Caller> targetCallObjectProperty(){
        return targetCall;
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
        this.targetClient=null;
        this.targetGroup=null;
        this.groupList.clear();
    }

    public BlockingQueue<String> getMessageResponseQueue() {
        return messageResponseQueue;
    }
}
