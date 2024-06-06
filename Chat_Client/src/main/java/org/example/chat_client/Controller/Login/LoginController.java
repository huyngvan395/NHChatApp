package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private TextField password_shown;
    @FXML
    private PasswordField password;
    @FXML
    private Button login_btn;
    @FXML
    private CheckBox show_pass;
    @FXML
    private Hyperlink signup;
    @FXML
    private Hyperlink forgot_pass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signup.setOnAction(event -> SignUp());
    }

    public void SignUp(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Signup);
    }

}
