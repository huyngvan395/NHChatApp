package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPassController implements Initializable {
    @FXML
    private Button return_Login;
    @FXML
    private TextField email;
    @FXML
    private Button send_code_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        send_code_btn.setOnAction(event -> sendCode());
        return_Login.setOnAction(event -> return_Login());
    }

    public void sendCode() {
        if(!email.getText().isEmpty()){
            String emailValue=email.getText();
            String message="forgot-pass|"+emailValue;
            Model.getInstance().getSocketClient().sendMessage(message);
            String messageResponse=Model.getInstance().getSocketClient().receiveResponse();
            System.out.println(messageResponse);
            if(messageResponse.startsWith("success")){
                Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Verification);
            }else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This email is not registered with any account");
                alert.showAndWait();
            }
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter email field");
            alert.showAndWait();
        }
    }

    public void return_Login(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
    }


}
