package org.example.chat_client.Controller.Client.Message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageSingleReceiveController implements Initializable {
    @FXML
    private Label message;
    @FXML
    private Label time_created;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setTimeCreated(String time_created) {
        this.time_created.setText(time_created);
    }
}
