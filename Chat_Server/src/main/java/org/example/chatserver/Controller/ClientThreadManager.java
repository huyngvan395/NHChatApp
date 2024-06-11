package org.example.chatserver.Controller;

import com.google.gson.Gson;
import org.example.chatserver.Model.Client;
import org.example.chatserver.Model.Model;
import org.example.chatserver.Utilities.SendMail;

import java.util.ArrayList;
import java.util.List;

public class ClientThreadManager {
    private final List<ClientThreadHandle> clientHandlers;
    private final Gson gson;

    public ClientThreadManager() {
        clientHandlers = new ArrayList<>();
        gson=new Gson();
    }

    public synchronized void addClientHandler(ClientThreadHandle clientHandler) {
        clientHandlers.add(clientHandler);
    }

    public synchronized void removeClientHandler(ClientThreadHandle clientHandler) {
        clientHandler.stopClient();
        clientHandlers.remove(clientHandler);
    }

    public synchronized void singleChat(String receiveID, String message) {
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(clientHandler.getClientID().equals(receiveID)){
                clientHandler.writeMessage(message);
            }
        }
    }

    public synchronized String sendCode(String email){
        String code= SendMail.randomCode();
        SendMail.sendCode(email, code);
        return code;
    }

    public synchronized void sendInfoNewClientToOthers(Client newClient){
        String newClientJson=gson.toJson(newClient);
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(!clientHandler.getClientID().equals(newClient.getClientID())){
                clientHandler.writeMessage("newClient/"+newClientJson);
            }
        }
        System.out.println("Sent new client to other clients");
    }

    public synchronized void sendListOnlineClient(ClientThreadHandle clientHandler){
        List<Client> listClient=new ArrayList<>();
        for(Client client: Model.getInstance().getClientOnlineList()){
            if(!clientHandler.getClientID().equals(client.getClientID())){
                listClient.add(client);
            }
        }
        String listClientOnlineJson=gson.toJson(listClient);
        clientHandler.writeMessage("listOnline/"+listClientOnlineJson);
    }

    public synchronized void sendRemoveClient(String message){
        for (ClientThreadHandle clientHandler : clientHandlers) {
            clientHandler.writeMessage(message);
        }
    }

    public synchronized void receiveFile(){}

//    public synchronized void stopAllClients() {
//        for (ClientThreadHandle clientHandler : clientHandlers) {
//            clientHandler.stopClient();
//        }
//        clientHandlers.clear();
//    }


}
