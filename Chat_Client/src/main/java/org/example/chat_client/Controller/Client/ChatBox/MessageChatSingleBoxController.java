package org.example.chat_client.Controller.Client.ChatBox;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.chat_client.Model.DateTimeFormatMessage;
import org.example.chat_client.Model.Message;
import org.example.chat_client.Model.MessageListener;
import org.example.chat_client.Model.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessageChatSingleBoxController implements Initializable, MessageListener {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox content_chat;
    @FXML
    private ImageView image_avatar;
    @FXML
    private Label name;
    @FXML
    private TextArea Enter_message;
    @FXML
    private Button sendImage_btn;
    @FXML
    private Button sendFile_btn;
    @FXML
    private Button sendMessage_btn;

    private final String directoryImage="client_storage/Image";
    private final String directoryFile="client_storage/File";


    @Override
    public void initialize(URL location, ResourceBundle resources){
        if(Model.getInstance().targetClientObjectProperty().get()!=null){
            setGUIChat();
            loadHistoryMessage();
        }
        content_chat.heightProperty().addListener(((observable, oldValue, newValue) ->{
            scrollToBottom();
        }));
        Model.getInstance().targetClientObjectProperty().addListener(((observable, oldValue, newValue) ->{
            if(newValue!=null){
                setGUIChat();
                loadHistoryMessage();
            }
        }));
        sendMessage_btn.setOnAction(e->sendMessage());
        sendImage_btn.setOnAction(e->sendImage());
        sendFile_btn.setOnAction(e->sendFile());
        Model.getInstance().getMessageHandler().addMessageListener(this);
    }

    private void setImage_avatar(String imageSend){
        Image image=new Image(imageSend);
        this.image_avatar.setImage(image);
    }

    public void setName(String name){
        this.name.setText(name);
    }

    public void setGUIChat(){
        String image=Model.getInstance().targetClientObjectProperty().get().getImage();
        String name=Model.getInstance().targetClientObjectProperty().get().getName();
        if(image!=null){
            setImage_avatar(image);
        }
        setName(name);
        System.out.println(name);
    }

    public void sendMessage() {
        if(!Enter_message.getText().isEmpty()){
            LocalDateTime localDateTime = LocalDateTime.now();
            String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane messageContain= Model.getInstance().getViewFactory().getMessageSend(Enter_message.getText(), timeShow);
            content_chat.getChildren().add(messageContain);
            Model.getInstance().getSocketClient().sendMessage("single-message/"+Enter_message.getText().replace("\n", "<newline>")+"/"+Model.getInstance().targetClientObjectProperty().get().getClientID()+"/"+ localDateTime);
            Enter_message.clear();
        }
    }

    public void sendImage(){
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
            String base64Image=java.util.Base64.getEncoder().encodeToString(bytes);
            LocalDateTime localDateTime = LocalDateTime.now();
            String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
            AnchorPane imageContain= Model.getInstance().getViewFactory().getImageSend(timeShow, localFile.toURI().toString());
            content_chat.getChildren().addAll(imageContain);
            Model.getInstance().getSocketClient().sendMessage("single-image|"+base64Image+"|"+localFile.getName()+"|"+ localFile.toURI()+"|"+Model.getInstance().targetClientObjectProperty().get().getClientID()+"|"+localDateTime);
        }
    }

    public void sendFile(){
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
            Model.getInstance().getSocketClient().sendMessage("single-file|"+base64File+"|"+localFile.getName()+"|"+localFile.toURI()+"|"+Model.getInstance().targetClientObjectProperty().get().getClientID()+"|"+localDateTime);
        }
    }

    @Override
    public void onMessageReceived(String message) {
        String currentClientID;
        if(Model.getInstance().getCurrentClient().getClientID()!=null){
            currentClientID=Model.getInstance().getCurrentClient().getClientID();
        }else{
            currentClientID=null;
        }
        String targetClientID;
        if(Model.getInstance().targetClientObjectProperty().get()!=null){
            targetClientID=Model.getInstance().targetClientObjectProperty().get().getClientID();
        } else {
            targetClientID = null;
        }
        System.out.println(message+"chat-single");
        if(targetClientID!=null  && currentClientID!=null){
            Platform.runLater(()->{
                String[] messageParts=message.split("/");
                String[] messageParts1=message.split("\\|");
                if(message.startsWith("single-message")){
                    if(currentClientID.equals(messageParts[1]) && targetClientID.equals(messageParts[4])){
                        System.out.println("Đã gửi tới");
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts[3]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane messagePane=Model.getInstance().getViewFactory().getMessageSingleReceive(messageParts[2].replace("<newline>", "\n"),timeShow);
                        content_chat.getChildren().add(messagePane);
                    }
                }else if(message.startsWith("single-image")){
                    if(currentClientID.equals(messageParts1[1]) && targetClientID.equals(messageParts1[5])){
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
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts1[4]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane imagePane=Model.getInstance().getViewFactory().getImageSingleReceive(imageFile.toURI().toString(),timeShow);
                        content_chat.getChildren().addAll(imagePane);
                    }
                }else if(message.startsWith("single-file")){
                    if(currentClientID.equals(messageParts1[1]) && targetClientID.equals(messageParts1[5])){
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
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts1[4]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane filePane=Model.getInstance().getViewFactory().getFileSingleReceive(file.getName(),timeShow);
                        content_chat.getChildren().addAll(filePane);
                    }
                }else if(message.startsWith("loadHistorySingle")){
                    content_chat.getChildren().clear();
                    content_chat.requestLayout();
                    if(Model.getInstance().getMessageListSingle()!=null){
                        List<Message> listMessage=Model.getInstance().getMessageListSingle();
                        for(Message messageOb: listMessage){
                            Platform.runLater(()->{
                                if(messageOb.getMessageType().equals("Text") && messageOb.getSenderID().equals(currentClientID)){
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    AnchorPane textPane=Model.getInstance().getViewFactory().getMessageSend(messageOb.getMessage().replace("<newline>","\n"), timeShow);
                                    content_chat.getChildren().add(textPane);
                                }else if(messageOb.getMessageType().equals("Text") && messageOb.getSenderID().equals(targetClientID)){
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    AnchorPane textPane=Model.getInstance().getViewFactory().getMessageSingleReceive(messageOb.getMessage().replace("<newline>","\n"), timeShow);
                                    content_chat.getChildren().add(textPane);
                                    System.out.println("Target");
                                }else if(messageOb.getMessageType().equals("Image") && messageOb.getSenderID().equals(currentClientID)) {
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    AnchorPane imagePane=Model.getInstance().getViewFactory().getImageSend(timeShow, messageOb.getMessage());
                                    content_chat.getChildren().add(imagePane);
                                }else if(messageOb.getMessageType().equals("Image") && messageOb.getSenderID().equals(targetClientID)){
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    AnchorPane imagePane=Model.getInstance().getViewFactory().getImageSingleReceive(messageOb.getMessage(), timeShow);
                                    content_chat.getChildren().add(imagePane);
                                }else if(messageOb.getMessageType().equals("File") && messageOb.getSenderID().equals(currentClientID)){
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    int lastSlashIndex=messageOb.getMessage().lastIndexOf("/");
                                    String fileName=messageOb.getMessage().substring(lastSlashIndex+1);
                                    AnchorPane filePane=Model.getInstance().getViewFactory().getFileSend(timeShow,fileName);
                                    content_chat.getChildren().add(filePane);
                                }else if(messageOb.getMessageType().equals("File") && messageOb.getSenderID().equals(targetClientID)){
                                    String timeShow=DateTimeFormatMessage.formatDateTime(LocalDateTime.parse(messageOb.getTimeSend()));
                                    int lastSlashIndex=messageOb.getMessage().lastIndexOf("/");
                                    String fileName=messageOb.getMessage().substring(lastSlashIndex+1);
                                    AnchorPane filePane=Model.getInstance().getViewFactory().getFileSingleReceive(fileName ,timeShow);
                                    content_chat.getChildren().add(filePane);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private void scrollToBottom(){
        Platform.runLater(() -> {
            content_chat.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    public void loadHistoryMessage(){
        if(Model.getInstance().targetClientObjectProperty().get()!=null){
            String targetClientID=Model.getInstance().targetClientObjectProperty().get().getClientID();
            Model.getInstance().getSocketClient().sendMessage("load_history_single/"+targetClientID);
        }
    }
}
