package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.chat_client.Model.Model;
import org.example.chat_client.Model.ShowPass;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePassController implements Initializable {
    @FXML
    private CheckBox show_pass;
    @FXML
    private Button submit_btn;
    @FXML
    private PasswordField password;
    @FXML
    private TextField password_shown;
    @FXML
    private PasswordField confirm_pass;
    @FXML
    private TextField confirm_pass_shown;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password_shown.setVisible(false);
        confirm_pass_shown.setVisible(false);
        show_pass.setOnAction(e-> showPass());
        submit_btn.setOnAction(e->SubmitChangePass());
    }

    public void showPass(){
        ShowPass.showPass(password_shown, confirm_pass_shown, password, confirm_pass);
    }

    public void SubmitChangePass(){
        if(password.isVisible() && confirm_pass.isVisible()){
            checkConfirmPass(password, confirm_pass);
        }else{
            checkConfirmPass(password_shown, confirm_pass_shown);
        }
    }

    private void checkConfirmPass(TextField password, TextField confirmPass) {
        if(password.getText().equals(confirmPass.getText())){
            String message=password.getText();
            Model.getInstance().getSocketClient().sendMessage(message);
            Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Confirmation password is incorrect");
            alert.showAndWait();
        }
    }

}
