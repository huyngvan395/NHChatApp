package org.example.chat_client.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellSelectController implements Initializable {
    @FXML
    private RadioButton select_radio;
    @FXML
    private ImageView image_avatar;
    @FXML
    private Label name_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setImage_avatar(String imageClient){
        Image image=new Image(imageClient);
        image_avatar.setImage(image);
    }

    public void setName(String name){
        name_lbl.setText(name);
    }

    public RadioButton getSelect() {
        return select_radio;
    }
}
