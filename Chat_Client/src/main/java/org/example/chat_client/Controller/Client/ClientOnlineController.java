package org.example.chat_client.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientOnlineController implements Initializable {
    @FXML
    private ImageView image_avatar;
    @FXML
    private Label name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setImage_avatar(String imageSend){
        Image image = new Image(imageSend,true);
        this.image_avatar.setImage(image);
    }

    public void setName(String name){
        this.name.setText(name);
    }
}
