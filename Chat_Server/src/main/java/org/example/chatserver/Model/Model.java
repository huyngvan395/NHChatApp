package org.example.chatserver.Model;

import com.google.gson.Gson;
import org.example.chatserver.Controller.ClientThreadHandle;
import org.example.chatserver.Controller.ClientThreadManager;
import org.example.chatserver.DAO.AccountDAO;
import org.example.chatserver.DAO.ConversationDAO;
import org.example.chatserver.DAO.MessageDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static Model model;
    private AccountDAO accountDAO;
    private MessageDAO messageDAO;
    private ConversationDAO conversationDAO;
    private final ClientThreadManager clientThreadManager;
    private final List<Client> clientOnlineList;

    public Model(){
        this.accountDAO=new AccountDAO();
        this.clientThreadManager=new ClientThreadManager();
        this.clientOnlineList=new ArrayList<>();
    }

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public ConversationDAO getConversationDAO() {
        return conversationDAO;
    }

    public List<Client> getClientOnlineList() {
        return clientOnlineList;
    }

    public synchronized void addClientOnlineList(Client client) {
        this.clientOnlineList.add(client);
    }

    public synchronized void removeClientOnlineList(String ClientID) {
        this.clientOnlineList.removeIf(client -> client.getClientID().equals(ClientID));
    }

    public ClientThreadManager getClientThreadManager() {
        return clientThreadManager;
    }

}
