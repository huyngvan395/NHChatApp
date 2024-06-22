package org.example.chat_client.Controller.Client.Setting;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
        update_button.setOnAction(event -> update());
    }

    public void update(){
        if(!Name.getText().isEmpty() || !Email.getText().isEmpty()){
            Model.getInstance().getSocketClient().sendMessage("update-info/"+ID.getText()+"/"+Name.getText()+"/"+Email.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Info");
            alert.setHeaderText(null);
            alert.setContentText("Update Information Successfully");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Info");
            alert.setHeaderText(null);
            alert.setContentText("Error Updating Info");
            alert.showAndWait();
        }
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
