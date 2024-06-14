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

    public void addClientHandler(ClientThreadHandle clientHandler) {
        clientHandlers.add(clientHandler);
    }

    public void removeClientHandler(ClientThreadHandle clientHandler) {
        clientHandler.stopClient();
        clientHandlers.remove(clientHandler);
    }

    public void singleChat(String receiverID, String message) {
        System.out.println(receiverID);
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(clientHandler.getClientID().equals(receiverID)){
                clientHandler.writeMessage(message);
            }
        }
    }

    public void chatbot(String receiveID, String message) {
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(clientHandler.getClientID().equals(receiveID)){
                clientHandler.writeMessage(message);
            }
        }
    }

    public String sendCode(String email){
        String code= SendMail.randomCode();
        SendMail.sendCode(email, code);
        return code;
    }

    public void sendInfoNewClientToOthers(Client newClient){
        String newClientJson=gson.toJson(newClient);
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(!clientHandler.getClientID().equals(newClient.getClientID())){
                clientHandler.writeMessage("newClient/"+newClientJson);
            }
        }
        System.out.println("Sent new client to other clients");
    }

    public void sendListOnlineClient(ClientThreadHandle clientHandler){
        List<Client> listClientTemp=new ArrayList<>();
        for(Client client: Model.getInstance().getClientOnlineList()){
            if(!clientHandler.getClientID().equals(client.getClientID())){
                listClientTemp.add(client);
            }
        }
        String listClientOnlineJson=gson.toJson(listClientTemp);
        clientHandler.writeMessage("listOnline/"+listClientOnlineJson);
    }

    public void sendRemoveClient(String message){
        for (ClientThreadHandle clientHandler : clientHandlers) {
            clientHandler.writeMessage(message);
        }
    }

    public void sendListClient(ClientThreadHandle clientThreadHandle){
        List<Client> listClient=Model.getInstance().getListClientDAO().getListClient();
        listClient.removeIf(client -> client.getClientID().equals(clientThreadHandle.getClientID()));
        String listClientJson=gson.toJson(listClient);
        clientThreadHandle.writeMessage("listClient/"+listClientJson);
    }

    public void receiveFile(){}

//    public synchronized void stopAllClients() {
//        for (ClientThreadHandle clientHandler : clientHandlers) {
//            clientHandler.stopClient();
//        }
//        clientHandlers.clear();
//    }


}
