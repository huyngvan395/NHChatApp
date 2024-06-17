package org.example.chatserver.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.chatserver.Model.Client;
import org.example.chatserver.Model.Group;
import org.example.chatserver.Model.Model;
import org.example.chatserver.Server;
import org.example.chatserver.Utilities.ChatBotAPI;
import org.example.chatserver.Utilities.Security;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ClientThreadHandle implements Runnable{
    private final Socket clientSocket;
    private BufferedReader inText;
    private BufferedWriter outText;
    private boolean running;
    private String clientID;
    private final String directoryImage="server_storage/Image";
    private final String directoryFile="server_storage/File";
    private final String directoryImageAvatar="server_storage/Image_Avatar";
    private final String directoryImageGroup="server_storage/Image_Group";
    private final Gson gson = new Gson();

    public ClientThreadHandle(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.running = true;
        this.clientID = "";
    }

    @Override
    public void run() {
        try{
            this.inText = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.outText = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//            this.inputStream = new BufferedInputStream(clientSocket.getInputStream());
//            this.outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Client thread started");
        try {
            String message;
            while (running && (message = inText.readLine()) != null) {
                System.out.println(message+"\n");
                String[] messageParts = message.split("/");
                String[] messageParts1 = message.split("\\|");
                if(message.startsWith("forgot-pass")){
                    handleForgotPass(messageParts);
                }
                else if (message.startsWith("login")) {
                    handleLogin(messageParts);
                }
                else if(message.startsWith("signup")){
                    handleSignup(messageParts1);
                }
                else if(message.startsWith("single-message")){
                    handleSingleMessage(messageParts);
                }
                else if(message.startsWith("single-file")){
                    handleSingleFile(messageParts1);
                }
                else if(message.startsWith("single-image")){
                    handleSingleImage(messageParts1);
                }
                else if(message.startsWith("change-pass")){
                    handleChangePass(messageParts);
                }
                else if(message.startsWith("update-info")){
                    handleUpdateInfo(messageParts);
                }
                else if(message.startsWith("chatbot")){
                    handleChatBotMessage(messageParts);
                }
                else if(message.startsWith("logout")){
                    Model.getInstance().getClientThreadManager().sendRemoveClient("removeClientOnline/"+clientID);
                    Model.getInstance().removeClientOnlineList(clientID);
                    this.clientID="";
                }
                else if(message.startsWith("create_group")){
                    handleCreateGroup(messageParts1);
                }
                else if(message.startsWith("group-message")){
                    handleGroupMessage(messageParts);
                }
                else if(message.startsWith("group-file")){
                    handleGroupFile(messageParts1);
                }
                else if(message.startsWith("group-image")){
                    handleGroupImage(messageParts1);
                }
                else if(message.startsWith("remove")){
                    running=false;
                    Model.getInstance().getClientThreadManager().removeClientHandler(this);
                    Model.getInstance().removeClientOnlineList(clientID);
                    Model.getInstance().getClientThreadManager().sendRemoveClient("removeClientOnline/"+clientID);
                }
            }
        }catch (SocketException e){
            System.out.println("Client disconnected");
            Model.getInstance().getClientThreadManager().removeClientHandler(this);
            Model.getInstance().removeClientOnlineList(clientID);
        } catch (IOException|SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleForgotPass(String[] messageParts) throws IOException{
        String email = messageParts[1];
        if(Model.getInstance().getAccountDAO().checkEmail(email)){
            String code=Model.getInstance().getClientThreadManager().sendCode(email);
            writeMessage("success");
            boolean checkcode=false;
            while(!checkcode){
                String messageSubmitCode=inText.readLine();
                if(messageSubmitCode.equals(code)){
                    writeMessage("change-pass");
                    String messageChangePass=inText.readLine();
                    String encodeChangePass=Security.enCode(messageChangePass);
                    Model.getInstance().getAccountDAO().updatePassViaEmail(email, encodeChangePass);
                    checkcode=true;
                }else{
                    writeMessage("code-wrong");
                }
            }

        }else{
            writeMessage("email-fail");
        }
    }

    public void handleLogin(String[] messageParts) throws IOException, SQLException {
        String email = messageParts[1];
        String password = messageParts[2];
        String encodePass=Security.enCode(password);

        if (Model.getInstance().getAccountDAO().Login(email, encodePass)) {
            ResultSet rs=Model.getInstance().getAccountDAO().getClientInfo(email);
            String name="";
            String image="";
            if(rs.next()){
                clientID=rs.getString("ClientID");
                name=rs.getString("Name");
                image=rs.getString("Image");
            }
            String messageToClient = "login/" + clientID +"/" + name+"/" + email +"/"+ image + "/success";
            Client client=new Client(clientID,name,email,image);
            this.writeMessage(messageToClient);
            Model.getInstance().getClientThreadManager().sendInfoNewClientToOthers(client);
            Model.getInstance().getClientThreadManager().sendListOnlineClient(this);
            Model.getInstance().addClientOnlineList(client);
            Model.getInstance().getClientThreadManager().sendListClient(this);
            Model.getInstance().getClientThreadManager().sendListGroup(this);
            System.out.println("Client logged in and info sent to others");
        } else {
            String messageToClient = "login-failed";
            writeMessage(messageToClient);
        }
    }

    public void handleSignup(String[] messageParts) throws IOException {
        String ID = messageParts[1];
        String name= messageParts[2];
        String email= messageParts[3];
        String password= messageParts[4];
        String encodePass= Security.enCode(password);
        String image= messageParts[7];
        File localDir=new File(directoryImageAvatar);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        byte[] imageBytes=Base64.getDecoder().decode(messageParts[5]);
        File imageAvatarClient=new File(directoryImageAvatar, messageParts[6]);
        try{
            Files.write(imageAvatarClient.toPath(), imageBytes);
        }catch(IOException e){
            e.printStackTrace();
        }
        Model.getInstance().getAccountDAO().addClient(ID, name, email, encodePass, image);
        this.writeMessage("signup/" + ID + "/success");
    }

    public void handleSingleMessage(String[] messageParts) throws IOException{
        String messageSendFromClient = messageParts[1];
        String ReceiverID = messageParts[2];
        String timeSend= messageParts[3];
        String messageSend="single-message/"+ReceiverID+"/"+messageSendFromClient+"/"+timeSend+"/"+clientID;
        if(!Model.getInstance().getConversationDAO().checkConversationExist(clientID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(clientID, ReceiverID);
        }
        String ID=Model.getInstance().getConversationDAO().getConversationSingleID(clientID, ReceiverID);
        Model.getInstance().getMessageDAO().addMessageSingle(ID, clientID, messageSendFromClient, "Text",LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSend);

    }

    public void handleSingleFile(String[] messageParts) throws IOException{
        String base64File = messageParts[1];
        String nameFile = messageParts[2];
        String filePath= messageParts[3];
        String ReceiverID = messageParts[4];
        String timeSend= messageParts[5];
        byte[] fileBytes = Base64.getDecoder().decode(base64File);
        File localDir=new File(directoryFile);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File file=new File(localDir,nameFile);
        Files.write(file.toPath(), fileBytes);
        String messageSend="single-file|"+ReceiverID+"|"+base64File+"|"+nameFile+"|"+timeSend+"|"+clientID;
        System.out.println(Model.getInstance().getConversationDAO().checkConversationExist(clientID, ReceiverID));
        if(!Model.getInstance().getConversationDAO().checkConversationExist(clientID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(clientID, ReceiverID);
        }
        String ID=Model.getInstance().getConversationDAO().getConversationSingleID(clientID, ReceiverID);
        Model.getInstance().getMessageDAO().addMessageSingle(ID, clientID, filePath,"File",LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSend);
    }

    public void handleSingleImage(String[] messageParts) throws IOException{
        String base64Image = messageParts[1];
        String nameImage = messageParts[2];
        String imagePath= messageParts[3];
        String ReceiverID = messageParts[4];
        String timeSend= messageParts[5];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        File localDir=new File(directoryImage);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File imageFile=new File(localDir,nameImage);
        Files.write(imageFile.toPath(), imageBytes);
        String messageSend="single-image|"+ReceiverID+"|"+base64Image+"|"+nameImage+"|"+timeSend+"|"+clientID;
        System.out.println(Model.getInstance().getConversationDAO().checkConversationExist(clientID, ReceiverID));
        if(!Model.getInstance().getConversationDAO().checkConversationExist(clientID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(clientID, ReceiverID);
        }
        String ID=Model.getInstance().getConversationDAO().getConversationSingleID(clientID, ReceiverID);
        Model.getInstance().getMessageDAO().addMessageSingle(ID, clientID, imagePath, "Image",LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSend);
    }

    public void handleChangePass(String[] messageParts){
        String SenderID= messageParts[2];
        String password= messageParts[3];
        String encodePass= Security.enCode(password);
        Model.getInstance().getAccountDAO().updatePassViaClientID(SenderID, encodePass);
    }

    public void handleUpdateInfo(String[] messageParts) throws IOException{
        String SenderID= messageParts[1];
        String name=messageParts[2];
        LocalDate date=LocalDate.parse(messageParts[3]);
        String email=messageParts[4];
        Model.getInstance().getAccountDAO().updateInfo(SenderID, name, date, email);
    }

    public void handleChatBotMessage(String[] messageParts) throws IOException{
        String response= ChatBotAPI.sendMessageToAPI(messageParts[1]);
        String safeResponse = response.replace("\n", "<br>").replace("\r", " ").replace("*", " ");
        LocalDateTime localDateTime = LocalDateTime.now();
        String timeShow=formatDateTime(localDateTime);
        String messageResponse= "chatbot_response/"+safeResponse+"/"+timeShow;
        Model.getInstance().getClientThreadManager().chatbot(clientID, messageResponse);
        System.out.println(messageResponse);
    }

    public void handleCreateGroup(String[] messageParts) throws IOException{
        String nameGroup= messageParts[1];
        String imagePath= messageParts[2];
        String base64Image= messageParts[3];
        String listClientInGroup= messageParts[4];
        String nameImage=messageParts[5];
        Model.getInstance().getConversationDAO().createConversationGroup(nameGroup, imagePath);
        String IDGroup=Model.getInstance().getConversationDAO().getIDGroup(nameGroup);
        List<Client> listClient=gson.fromJson(listClientInGroup, new TypeToken<List<Client>>(){}.getType());;
        for(Client c:listClient){
            Model.getInstance().getConversationDAO().addMemberGroup(IDGroup, c.getClientID());
        }
        Group group=new Group(IDGroup, nameGroup, imagePath);
        String groupJSon=gson.toJson(group);
        String message=groupJSon+"|"+nameImage+"|"+base64Image;
        Model.getInstance().getClientThreadManager().sendNewGroup(nameGroup, message);
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        File localDir=new File(directoryImageGroup);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File imageFile=new File(localDir,nameImage);
        Files.write(imageFile.toPath(), imageBytes);
    }

    public void handleGroupMessage(String[] messageParts) throws IOException{
        String messageSendFromClient= messageParts[1];
        String groupID= messageParts[2];
        String timeSend=messageParts[3];
        String messageSend="group-message/"+groupID+"/"+messageSendFromClient+"/"+timeSend+"/"+clientID;
        Model.getInstance().getMessageDAO().addMessageGroup(groupID, clientID, messageSendFromClient, "Text", LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().groupChat(groupID, clientID, messageSend);
    }

    public void handleGroupImage(String[] messageParts) throws IOException{
        String base64Image = messageParts[1];
        String nameImage = messageParts[2];
        String imagePath= messageParts[3];
        String groupID = messageParts[4];
        String timeSend= messageParts[5];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        File localDir=new File(directoryImage);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File imageFile=new File(localDir,nameImage);
        Files.write(imageFile.toPath(), imageBytes);
        String messageSend="single-image|"+groupID+"|"+base64Image+"|"+nameImage+"|"+timeSend+"|"+clientID;
        Model.getInstance().getMessageDAO().addMessageGroup(groupID,clientID,imagePath,"Image",LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().groupChat(groupID,clientID,messageSend);
    }

    public void handleGroupFile(String[] messageParts) throws IOException{
        String base64File = messageParts[1];
        String nameFile = messageParts[2];
        String filePath= messageParts[3];
        String groupID = messageParts[4];
        String timeSend= messageParts[5];
        byte[] fileBytes = Base64.getDecoder().decode(base64File);
        File localDir=new File(directoryFile);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File file=new File(localDir,nameFile);
        Files.write(file.toPath(), fileBytes);
        String messageSend="group-file|"+groupID+"|"+base64File+"|"+nameFile+"|"+timeSend+"|"+clientID;
        Model.getInstance().getMessageDAO().addMessageGroup(groupID,clientID,filePath,"File",LocalDateTime.parse(timeSend));
        Model.getInstance().getClientThreadManager().groupChat(groupID,clientID,messageSend);
    }

    public void writeMessage(String message) {
        try {
            outText.write(message);
            outText.newLine();
            outText.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
        running = false;
        try {
            inText.close();
            outText.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientID() {
        return clientID;
    }

    public static String formatDateTime(LocalDateTime targetDateTime) {

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(targetDateTime, now);
        String formattedDateTime;
        if (duration.toDays() == 0) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() == 1) {
            formattedDateTime = "yesterday " + targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() > 1 && duration.toDays() <= 7) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("E HH:mm"));
        } else {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        return formattedDateTime;
    }
}
