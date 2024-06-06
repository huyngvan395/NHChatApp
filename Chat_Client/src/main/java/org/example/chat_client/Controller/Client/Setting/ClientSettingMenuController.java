package org.example.chat_client.Controller.Client.Setting;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.MenuSettingOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientSettingMenuController implements Initializable {
    @FXML
    private Button delete_acc;
    @FXML
    private Button security;
    @FXML
    private Button personal_info;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personal_info.setOnAction(e-> PersonalInfo());
        security.setOnAction(e-> Security());
        delete_acc.setOnAction(e-> DeleteAccount());
    }

    public void PersonalInfo(){
        Model.getInstance().getViewFactory().getMenuSettingOptions().set(MenuSettingOptions.PersonalInfo);
    }

    public void Security(){
        Model.getInstance().getViewFactory().getMenuSettingOptions().set(MenuSettingOptions.Security);
    }

    public void DeleteAccount(){

    }
}
