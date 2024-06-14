package org.example.chat_client.Controller.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.chat_client.Model.Client;
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
        forgot_pass.setOnAction(event -> ForgotPass());
        show_pass.setOnAction(e->showPass());
        login_btn.setOnAction(event -> Login());
    }

    public void Login(){
        String emailValue = email.getText();
        String passValue = password.isVisible()? password.getText(): password_shown.getText();
        if(emailValue.isEmpty() || passValue.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        }else{
            String message="login/"+emailValue+"/"+passValue;
            Model.getInstance().getSocketClient().sendMessage(message);
            String response=Model.getInstance().getSocketClient().receiveResponse();
            System.out.println(response+"\n");
            if(response.endsWith("success")){
                String[] messageParts=response.split("/");
                Client client =new Client(messageParts[1], messageParts[2], messageParts[3], messageParts[4]);
                Model.getInstance().setCurrentClient(client);
                Model.getInstance().getViewFactory().closeStage((Stage)signup.getScene().getWindow());
                Model.getInstance().getViewFactory().showClientPage();
                if(!Model.getInstance().isRunning()){
                    Model.getInstance().setRunning(true);
                }
                Model.getInstance().startMessageReader();
                Model.getInstance().processMessages();
            }
        }
    }

    public void showPass(){
        if(password.isVisible()){
            password_shown.setText(password.getText());
            password_shown.setVisible(true);
            password.setVisible(false);
        }else{
            password.setText(password_shown.getText());
            password_shown.setVisible(false);
            password.setVisible(true);
        }
    }

    public void SignUp(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Signup);
    }

    public void ForgotPass(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.ForgotPassword);
    }

}
