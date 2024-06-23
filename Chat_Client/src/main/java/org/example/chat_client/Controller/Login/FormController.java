package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class FormController implements Initializable {
    @FXML
    private BorderPane form_page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getLoginOptions().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case Login -> form_page.setCenter(Model.getInstance().getViewFactory().getLogin());
                case Signup -> form_page.setCenter(Model.getInstance().getViewFactory().getSignup());
                case ForgotPassword,ReturnForgotPassword -> form_page.setCenter(Model.getInstance().getViewFactory().getForgotPass());
                case Verification,ReturnVerification -> form_page.setCenter(Model.getInstance().getViewFactory().getVerification());
                case ChangePassword -> form_page.setCenter(Model.getInstance().getViewFactory().getChangePass());
            }
        });
    }
}
