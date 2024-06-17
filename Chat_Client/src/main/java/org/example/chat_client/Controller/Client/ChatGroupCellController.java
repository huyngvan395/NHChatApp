package org.example.chat_client.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatGroupCellController implements Initializable {
    @FXML
    private ImageView image_group;
    @FXML
    private Label name_group;
    @FXML
    private Label last_message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setImage_group(String imagePath){
        Image image = new Image(imagePath);
        image_group.setImage(image);
    }

    public void setName_group(String name){
        name_group.setText(name);
    }

    public void setLast_message(String last_message){
        this.last_message.setText(last_message);
    }
}
