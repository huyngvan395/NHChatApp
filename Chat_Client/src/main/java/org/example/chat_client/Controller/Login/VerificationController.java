package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class VerificationController implements Initializable {
    @FXML
    private Hyperlink resend_code;
    @FXML
    private Button submit_btn;
    @FXML
    private TextField c1;
    @FXML
    private TextField c2;
    @FXML
    private TextField c3;
    @FXML
    private TextField c4;
    @FXML
    private TextField c5;
    @FXML
    private TextField c6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTextField(c1,c2);
        setupTextField(c2,c3);
        setupTextField(c3,c4);
        setupTextField(c4,c5);
        setupTextField(c5,c6);
        submit_btn.setOnAction(e-> checkVerificationCode());
    }

    public void setupTextField(TextField current, TextField next){
        current.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if(current.getText().length()==1 ){
                keyEvent.consume();
            }
        });
        current.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length()==1 && next!=null){
                next.requestFocus();
            }
        });
    }

    public void checkVerificationCode(){
        String code=c1.getText()+c2.getText()+c3.getText()+c4.getText()+c5.getText()+c6.getText();
        Model.getInstance().getSocketClient().sendMessage(code);
        String response=Model.getInstance().getSocketClient().receiveResponse();
        if(response.startsWith("change-pass")){
            Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.ChangePassword);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Verification code is incorrect");
            alert.showAndWait();
            resetTextFields();
        }
    }

    private void resetTextFields() {
        c1.setText("");
        c2.setText("");
        c3.setText("");
        c4.setText("");
        c5.setText("");
        c6.setText("");
        // Set focus vào TextField đầu tiên sau khi reset
        c1.requestFocus();
    }

}
