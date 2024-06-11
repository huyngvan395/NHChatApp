package org.example.chat_client.Controller.Client.Message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.MenuChatOptions;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessageChatBoxController implements Initializable {
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getMenuChatOptions().addListener((observable, oldValue, newValue) -> {
            switch (newValue){
                case Group -> setChatGroup();
                case Single -> setChatSingle();
            }
        });
    }

    public void setImage_avatar(String imageSend){
        Image image=new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageSend)));
        this.image_avatar.setImage(image);
    }

    public void setName(String name){
        this.name.setText(name);
    }


    public void setChatGroup(){

    }

    public void setChatSingle(){

    }
}
