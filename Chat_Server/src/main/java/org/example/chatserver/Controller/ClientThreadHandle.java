package org.example.chatserver.Controller;

import com.google.gson.Gson;
import org.example.chatserver.Model.Client;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientThreadHandle implements Runnable{
    private final Socket clientSocket;
    private BufferedReader inText;
    private BufferedWriter outText;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private boolean running;
    private String clientID;

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
            this.inputStream = new BufferedInputStream(clientSocket.getInputStream());
            this.outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Client thread started");
        try {
            String message;
            while (running && (message = inText.readLine()) != null) {
                System.out.println(message+"\n");
                String[] messageParts = message.split("/");
                if(message.startsWith("forgot-pass")){
                    handleForgotPass(messageParts);
                }
                else if (message.startsWith("login")) {
                    handleLogin(messageParts);
                }
                else if(message.startsWith("signup")){
                    handleSignup(messageParts);
                }
                else if(message.startsWith("single-message")){
                    handleSingleMessage(messageParts);
                }
                else if(message.startsWith("single-file")){
                    handleSingleFile(messageParts);
                }
                else if(message.startsWith("single-image")){
                    handleSingleImage(messageParts);
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
//                    String image= messageParts[6];
        Model.getInstance().getAccountDAO().addClient(ID, name, email, encodePass);
        this.writeMessage("signup/" + ID + "/success");
    }

    public void handleSingleMessage(String[] messageParts) throws IOException{
        String SenderID = messageParts[2];
        String messageSendFromClient = messageParts[3];
        String ReceiverID = messageParts[4];
        String timeSend= messageParts[5];
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        if(!Model.getInstance().getConversationDAO().checkConversationExists(SenderID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(SenderID, ReceiverID);
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, messageSendFromClient, null,null,LocalDateTime.parse(timeSend));
            Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        }else{
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, messageSendFromClient, null,null,LocalDateTime.parse(timeSend));
        }
    }

    public void handleSingleFile(String[] messageParts) throws IOException{
        String SenderID = messageParts[2];
        String messageSendFromClient = messageParts[3];
        String ReceiverID = messageParts[4];
        String timeSend= messageParts[5];
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        if(!Model.getInstance().getConversationDAO().checkConversationExists(SenderID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(SenderID, ReceiverID);
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, null, messageSendFromClient,null,LocalDateTime.parse(timeSend));
            Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        }else{
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, null, messageSendFromClient,null,LocalDateTime.parse(timeSend));
        }
    }

    public void handleSingleImage(String[] messageParts) throws IOException{
        String SenderID = messageParts[2];
        String messageSendFromClient = messageParts[3];
        String ReceiverID = messageParts[4];
        String timeSend= messageParts[5];
        Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        if(!Model.getInstance().getConversationDAO().checkConversationExists(SenderID, ReceiverID)){
            Model.getInstance().getConversationDAO().addConversationSingle(SenderID, ReceiverID);
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, null, null,messageSendFromClient,LocalDateTime.parse(timeSend));
            Model.getInstance().getClientThreadManager().singleChat(ReceiverID, messageSendFromClient);
        }else{
            String ID=Model.getInstance().getConversationDAO().getConversationSingleID(SenderID, ReceiverID);
            Model.getInstance().getMessageDAO().addMessageSingle(ID, SenderID, null, null,messageSendFromClient,LocalDateTime.parse(timeSend));
        }
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
        String messageResponse= "response/"+response;
        writeMessage(messageResponse);
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

    public void receiveFile(){
        try{
            DataInputStream dis=new DataInputStream(inputStream);
            String fileName=dis.readUTF();
            long fileSize=dis.readLong();

            Path filePath= Paths.get("image_storage/File", fileName);
            Files.copy(dis,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void receiveImage(){
        try{
            DataInputStream dis=new DataInputStream(inputStream);
            String fileName=dis.readUTF();
            long fileSize=dis.readLong();

            Path imagePath= Paths.get("image_storage/Image", fileName);
            Files.copy(dis,imagePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void transferFile(String fileName){
        File file=new File("image_storage/File", fileName);
        try{
            DataOutputStream dos=new DataOutputStream(outputStream);
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());
            FileInputStream fis=new FileInputStream(file);
            byte[] buffer=new byte[4096];
            int bytesRead;
            while((bytesRead=fis.read(buffer))!=-1){
                dos.write(buffer,0,bytesRead);
            }
            dos.flush();
            dos.close();
            fis.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void transferImage(String imageName){
        File image=new File("image_storage/Image", imageName);
        try{
            DataOutputStream dos=new DataOutputStream(outputStream);
            dos.writeUTF(image.getName());
            dos.writeLong(image.length());
            FileInputStream fis=new FileInputStream(image);
            byte[] buffer=new byte[4096];
            int bytesRead;
            while((bytesRead=fis.read(buffer))!=-1){
                dos.write(buffer,0,bytesRead);
            }
            dos.flush();
            dos.close();
            fis.close();
        }catch(IOException e){
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
}
