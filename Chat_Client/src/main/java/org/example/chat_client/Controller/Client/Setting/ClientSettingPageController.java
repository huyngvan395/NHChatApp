package org.example.chat_client.Controller.Client.Setting;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientSettingPageController implements Initializable {
    @FXML
    private BorderPane setting_page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getMenuSettingOptions().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case Security -> setting_page.setCenter(Model.getInstance().getViewFactory().getSecurityAccount());
                case PersonalInfo -> setting_page.setCenter(Model.getInstance().getViewFactory().getPersonalInformation());
            }
        });
    }
}
