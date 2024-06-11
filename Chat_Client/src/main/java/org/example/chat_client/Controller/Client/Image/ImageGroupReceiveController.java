package org.example.chat_client.Controller.Client.Image;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageGroupReceiveController implements Initializable {
    @FXML
    private Label time_created;
    @FXML
    private Label name;
    @FXML
    private Button download;
    @FXML
    private ImageView image_avatar;
    @FXML
    private ImageView image_contain;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setTimeCreated(String time_created) {
        this.time_created.setText(time_created);
    }

    public void setImageAvatar(String imageSend) {
        Image image=new Image(imageSend);
        this.image_avatar.setImage(image);
    }

    public void setImageContain(String imageSend) {
        Image image=new Image(imageSend);
        this.image_contain.setImage(image);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void Download(){

    }
}
