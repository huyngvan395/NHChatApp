package org.example.chat_client.Controller.Client.Message;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.chat_client.Model.Client;
import org.example.chat_client.Model.DateTimeFormatMessage;
import org.example.chat_client.Model.MessageListener;
import org.example.chat_client.Model.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.ResourceBundle;

public class MessageChatGroupBoxController implements Initializable, MessageListener {
    @FXML
    private VBox content_chat;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView image_avatar_group;
    @FXML
    private Label name_group;
    @FXML
    private TextArea Enter_message;
    @FXML
    public Button sendImage_btn;
    @FXML
    public Button sendFile_btn;
    @FXML
    public Button sendMessage_btn;

    private final String directoryImage="client_storage/Image";
    private final String directoryFile="client_storage/File";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Model.getInstance().targetGroupObjectProperty().get() != null){
            setGUIChat();
        }
        scrollToBottom();
        Model.getInstance().targetGroupObjectProperty().addListener(((observable, oldValue, newValue) ->{
            if(newValue!=null){
                setGUIChat();
            }
        }));
        sendMessage_btn.setOnAction(e->sendMessage());
        sendImage_btn.setOnAction(e->sendImage());
        sendFile_btn.setOnAction(e->sendFile());
        Model.getInstance().getMessageHandler().addMessageListener(this);
    }

    public void setName_group(String name_group) {
        this.name_group.setText(name_group);
    }

    public void setImage_avatar_group(String imagePath) {
        Image image = new Image(imagePath);
        image_avatar_group.setImage(image);
    }

    public void setGUIChat(){
        String image=Model.getInstance().targetGroupObjectProperty().get().getImage();
        String nameGroup=Model.getInstance().targetGroupObjectProperty().get().getName();
        if(image!=null){
            setImage_avatar_group(image);
        }
        setName_group(nameGroup);
        System.out.println(nameGroup);
    }

    private void sendMessage() {
        if(!Enter_message.getText().isEmpty()){
            LocalDateTime localDateTime = LocalDateTime.now();
            String timeShow= DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane messageContain= Model.getInstance().getViewFactory().getMessageSend(Enter_message.getText(), timeShow);
            content_chat.getChildren().add(messageContain);
            Model.getInstance().getSocketClient().sendMessage("group-message/"+Enter_message.getText().replace("\n", "<newline>")+"/"+Model.getInstance().targetGroupObjectProperty().get().getGroupID()+"/"+ localDateTime);
            Enter_message.clear();
            content_chat.requestLayout();
            Platform.runLater(()->scrollToBottom());
        }
    }

    private void sendImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(content_chat.getScene().getWindow());
        if(selectedFile!=null){
            File localDir = new File(directoryImage);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }

            File localFile = new File(localDir, selectedFile.getName());
            try {
                Files.copy(selectedFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = null;
            try {
                bytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String base64Image= Base64.getEncoder().encodeToString(bytes);
            LocalDateTime localDateTime = LocalDateTime.now();
            String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane imageContain= Model.getInstance().getViewFactory().getImageSend(timeShow, localFile.toURI().toString());
            content_chat.getChildren().addAll(imageContain);
            Model.getInstance().getSocketClient().sendMessage("group-image|"+base64Image+"|"+localFile.getName()+"|"+ localFile.toURI()+"|"+Model.getInstance().targetGroupObjectProperty().get().getGroupID()+"|"+localDateTime);
            content_chat.requestLayout();
            Platform.runLater(()->scrollToBottom());
        }
    }

    private void sendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll();
        File selectedFile = fileChooser.showOpenDialog(content_chat.getScene().getWindow());
        if(selectedFile!=null){
            File localDir = new File(directoryFile);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }
            File localFile = new File(localDir, selectedFile.getName());
            try {
                Files.copy(selectedFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = null;
            try {
                bytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String base64File=java.util.Base64.getEncoder().encodeToString(bytes);
            LocalDateTime localDateTime = LocalDateTime.now();
            String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane fileContain= Model.getInstance().getViewFactory().getFileSend(timeShow, localFile.getName());
            content_chat.getChildren().addAll(fileContain);
            Model.getInstance().getSocketClient().sendMessage("group-file|"+base64File+"|"+localFile.getName()+"|"+localFile.toURI()+"|"+Model.getInstance().targetGroupObjectProperty().get().getGroupID()+"|"+localDateTime);
            content_chat.requestLayout();
            Platform.runLater(()->scrollToBottom());
        }
    }

    private void scrollToBottom(){
        scrollPane.setVvalue(1.0);
    }

    @Override
    public void onMessageReceived(String message) {
        String targetGroupID;
        if(Model.getInstance().targetGroupObjectProperty().get()!=null){
            targetGroupID=Model.getInstance().targetGroupObjectProperty().get().getGroupID();
        } else {
            targetGroupID = null;
        }
        System.out.println(message+"chat-single");
        if(targetGroupID!=null){
            Platform.runLater(()->{
                String[] messageParts=message.split("/");
                String[] messageParts1=message.split("\\|");
                if(message.startsWith("group-message")){
                    if(targetGroupID.equals(messageParts[1])){
                        System.out.println("Đã gửi tới");
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts[3]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        Client clientSend=null;
                        for(Client client:Model.getInstance().getListClient()){
                            if(client.getClientID().equals(messageParts[4])){
                                clientSend=client;
                            }
                        }
                        AnchorPane messagePane=Model.getInstance().getViewFactory().getMessageGroupReceive(clientSend.getName(),messageParts[2].replace("<newline>", "\n"),timeShow, clientSend.getImage());
                        content_chat.getChildren().add(messagePane);
                        content_chat.requestLayout();
                        Platform.runLater(()->scrollToBottom());
                    }
                }else if(message.startsWith("group-image")){
                    if(targetGroupID.equals(messageParts1[1])){
                        System.out.println("Receive success");
                        String base64Image=messageParts1[2];
                        String nameImage=messageParts1[3];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                        File localDir=new File(directoryImage);
                        if(!localDir.exists()){
                            localDir.mkdir();
                        }
                        File imageFile=new File(localDir,nameImage);
                        try {
                            Files.write(imageFile.toPath(), imageBytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Client clientSend=null;
                        for(Client client:Model.getInstance().getListClient()){
                            if(client.getClientID().equals(messageParts[4])){
                                clientSend=client;
                            }
                        }
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts1[4]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane imagePane=Model.getInstance().getViewFactory().getImageGroupReceive(clientSend.getName(),imageFile.toURI().toString(),timeShow, clientSend.getImage());
                        content_chat.getChildren().addAll(imagePane);
                        content_chat.requestLayout();
                        Platform.runLater(()->scrollToBottom());
                    }
                }else if(message.startsWith("group-file")){
                    if(targetGroupID.equals(messageParts1[1])){
                        System.out.println("Receive success");
                        String base64File=messageParts1[2];
                        String nameFile=messageParts1[3];
                        byte[] fileBytes = Base64.getDecoder().decode(base64File);
                        File localDir=new File(directoryFile);
                        if(!localDir.exists()){
                            localDir.mkdir();
                        }
                        File file=new File(localDir,nameFile);
                        try{
                            Files.write(file.toPath(), fileBytes);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        Client clientSend=null;
                        for(Client client:Model.getInstance().getListClient()){
                            if(client.getClientID().equals(messageParts[4])){
                                clientSend=client;
                            }
                        }
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts1[4]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane filePane=Model.getInstance().getViewFactory().getFileGroupReceive(clientSend.getName(),file.getName(),timeShow, clientSend.getImage());
                        content_chat.getChildren().addAll(filePane);
                        content_chat.requestLayout();
                        Platform.runLater(()->scrollToBottom());
                    }
                }
            });
        }
    }
}
