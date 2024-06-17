package org.example.chat_client.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatSingleCellController implements Initializable {
    @FXML
    private ImageView image_avatar;
    @FXML
    private Label name;
    @FXML
    private Label last_message;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setName(String nameValue){
        this.name.setText(nameValue);
    }

    public void setImage_avatar(String imagePath){
        Image image=new Image(imagePath);
        image_avatar.setImage(image);
    }

}
