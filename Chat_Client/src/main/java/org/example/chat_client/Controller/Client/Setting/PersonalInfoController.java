package org.example.chat_client.Controller.Client.Setting;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalInfoController implements Initializable {
    @FXML
    private TextField ID;
    @FXML
    private TextField Name;
    @FXML
    private TextField Email;
    @FXML
    private Button update_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setID();
        setName();
        setEmail();
    }

    public void setID() {
        this.ID.setText(Model.getInstance().getCurrentClient().getClientID());
    }

    public void setName() {
        this.Name.setText(Model.getInstance().getCurrentClient().getName());
    }

    public void setEmail() {
        this.Email.setText(Model.getInstance().getCurrentClient().getEmail());
    }

}
