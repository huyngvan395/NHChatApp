package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private DatePicker dob;
    @FXML
    private TextField name;
    @FXML
    private TextField ID;
    @FXML
    private PasswordField confirm_pass;
    @FXML
    private TextField confirm_pass_shown;
    @FXML
    private Hyperlink login;
    @FXML
    private CheckBox show_pass;
    @FXML
    private Button signup_btn;
    @FXML
    private PasswordField password;
    @FXML
    private TextField password_shown;
    @FXML
    private TextField email;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setOnAction(e-> Login());
    }

    public void Login(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
    }
}
