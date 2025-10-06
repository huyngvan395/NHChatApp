package org.example.chat_client.Controller.Client.Call;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class CallFormController implements Initializable {
    @FXML
    private BorderPane form_call;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setForm_call(AnchorPane pane){
        form_call.setCenter(pane);
    }
}
