package org.example.chatserver.Controller;

import com.google.gson.Gson;
import org.example.chatserver.Model.Client;
import org.example.chatserver.Model.Group;
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

    public void groupChat(String groupID,String senderID, String message){
        List<String> listIDMember=Model.getInstance().getConversationDAO().getIDMembers(groupID);
        for(String IDMember:listIDMember){
            for(ClientThreadHandle clientHandler : clientHandlers){
                if(clientHandler.getClientID().equals(IDMember) && !clientHandler.getClientID().equals(senderID)){
                    clientHandler.writeMessage(message);
                }
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

    public void callRequest(String receiverID,String senderHost, String senderID, String senderName, String senderImg){
        for (ClientThreadHandle clientHandler : clientHandlers) {
            if(clientHandler.getClientID().equals(receiverID)){
                clientHandler.writeMessage("call-request|"+senderHost+"|"+senderID+"|"+senderName+"|"+senderImg);
            }
        }
    }

    public void callResponse(String msg, String receiverID){
        for(ClientThreadHandle clientHandler : clientHandlers){
            if(clientHandler.getClientID().equals(receiverID)){
                clientHandler.writeMessage(msg);
            }
        }
    }

    public void stopCall(String receiverID){
        for (ClientThreadHandle clientThreadHandle: clientHandlers){
            if(clientThreadHandle.getClientID().equals(receiverID)){
                clientThreadHandle.writeMessage("stop-call");
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
                clientHandler.writeMessage("newClientOnline|"+newClientJson);
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
        clientHandler.writeMessage("listOnline|"+listClientOnlineJson);
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
        clientThreadHandle.writeMessage("listClient|"+listClientJson);
    }

    public void setListClient(ClientThreadHandle clientThreadHandle){
        for(ClientThreadHandle clientHandler:clientHandlers){
            if(!clientHandler.getClientID().equals(clientThreadHandle.getClientID())){
                List<Client> listClient=Model.getInstance().getListClientDAO().getListClient();
                listClient.removeIf(client -> client.getClientID().equals(clientHandler.getClientID()));
                String listClientJson=gson.toJson(listClient);
                clientHandler.writeMessage("listClient|"+listClientJson);
            }
        }
    }

    public void sendListGroup(ClientThreadHandle clientThreadHandle){
        List<Group> listGroup=Model.getInstance().getConversationDAO().getListGroup(clientThreadHandle.getClientID());
        String listGroupJson=gson.toJson(listGroup);
        clientThreadHandle.writeMessage("listGroup|"+listGroupJson);
    }

    public void sendNewGroup(String nameGroup,String message){
        String IDGroup=Model.getInstance().getConversationDAO().getIDGroup(nameGroup);
        List<String> listIDMember=Model.getInstance().getConversationDAO().getIDMembers(IDGroup);
        for(String IDMember:listIDMember){
            for(ClientThreadHandle clientHandler : clientHandlers){
                if(clientHandler.getClientID().equals(IDMember)){
                    clientHandler.writeMessage("newGroup|"+message);
                }
            }
        }
    }

}
