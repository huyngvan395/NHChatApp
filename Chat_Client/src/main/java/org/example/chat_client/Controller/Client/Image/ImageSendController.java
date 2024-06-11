package org.example.chat_client.Controller.Client.Image;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageSendController implements Initializable {
    @FXML
    private ImageView image_contain;
    @FXML
    private Label time_created;
    @FXML
    private Button download;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setImage_contain(String imageSend) {
        Image image= new Image(imageSend);
        this.image_contain.setImage(image);
    }

    public void setTime_created(String time_created) {
        this.time_created.setText(time_created);
    }

    public void download(){

    }
}
