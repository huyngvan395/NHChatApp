package org.example.chat_client.Controller.Client.Message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageGroupReceiveController implements Initializable {
    @FXML
    private Label message;
    @FXML
    private Label name;
    @FXML
    private ImageView image_avatar;
    @FXML
    private Label time_created;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setImage(String imageSend) {
        Image image=new Image(imageSend);
        this.image_avatar.setImage(image);
    }

    public void setTimeCreated(String time_created) {
        this.time_created.setText(time_created);
    }
}
