package org.example.chat_client.Controller.Client.File;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FileSingleReceiveController implements Initializable {
    @FXML
    private Label file_name;
    @FXML
    private Label time_created;
    @FXML
    private Button download;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFileName(String file_name) {
        this.file_name.setText(file_name);
    }

    public void setTimeCreated(String time_created) {
        this.time_created.setText(time_created);
    }

    public void Download(){

    }
}
