package org.example.chat_client.Controller.Client.Message;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.chat_client.Model.DateTimeFormatMessage;
import org.example.chat_client.Model.MessageListener;
import org.example.chat_client.Model.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessageChatSingleBoxController implements Initializable, MessageListener {
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
        Model.getInstance().targetClientObjectProperty().addListener(((observable, oldValue, newValue) ->{
            if(newValue!=null){
                setGUIChat();
            }
        }));
        sendMessage_btn.setOnAction(e->sendMessage());
        sendImage_btn.setOnAction(e->sendImage());
        Model.getInstance().getMessageHandler().addMessageListener(this);
    }

    private void setImage_avatar(String imageSend){
        Image image=new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageSend)));
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
            Model.getInstance().getSocketClient().sendMessage("single-message/"+Enter_message.getText()+"/"+Model.getInstance().targetClientObjectProperty().get().getClientID()+"/"+ localDateTime);
            Enter_message.clear();
        }
    }

    public void sendImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog((Stage)content_chat.getScene().getWindow() );
        if(selectedFile!=null){
            File localDir = new File(directoryImage);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }

            // Lưu file vào local storage
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

    @Override
    public void onMessageReceived(String message) {
        String currentClientID=Model.getInstance().getCurrentClient().getClientID();
        String targetClientID;
        if(Model.getInstance().targetClientObjectProperty().get()!=null){
            targetClientID=Model.getInstance().targetClientObjectProperty().get().getClientID();
        } else {
            targetClientID = null;
        }
        System.out.println(message+"chat-single");
        if(targetClientID!=null){
            Platform.runLater(()->{
                String[] messageParts=message.split("/");
                String[] messageParts1=message.split("\\|");
                if(message.startsWith("single-message")){
                    if(currentClientID.equals(messageParts[1]) && targetClientID.equals(messageParts[4])){
                        System.out.println("Đã gửi tới");
                        LocalDateTime localDateTime=LocalDateTime.parse(messageParts[3]);
                        String timeShow=DateTimeFormatMessage.formatDateTime(localDateTime);
                        AnchorPane messagePane=Model.getInstance().getViewFactory().getMessageSingleReceive(messageParts[2],timeShow);
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
                }
            });
        }

    }
}
