package org.example.chat_client.Controller.Login;

import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.chat_client.Model.Model;
import org.example.chat_client.Model.ShowPass;
import org.example.chat_client.View.LoginOptions;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private ImageView image_avatar;
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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password_shown.setVisible(false);
        confirm_pass_shown.setVisible(false);
        login.setOnAction(e-> Login());
        signup_btn.setOnAction(e->Signup());
        show_pass.setOnAction(e->showPass());
    }

    public void Signup(){
        if(name.getText().isEmpty() || ID.getText().isEmpty() || password.getText().isEmpty() || confirm_pass.getText().isEmpty() || email.getText().isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        }else{
            String ID_value=ID.getText();
            String name_value=name.getText();
            String password_value=password.isVisible()? password.getText() : password_shown.getText();
            String email_value=email.getText();
            String message="signup/"+ID_value+"/"+name_value+"/"+email_value+"/"+password_value;
            Model.getInstance().getSocketClient().sendMessage(message);
            if(Model.getInstance().getSocketClient().receiveResponse().endsWith("success")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Signup Successful");
                alert.showAndWait();
                Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Signup Failed");
                alert.showAndWait();
            }
        }
    }

    public void showPass(){
        ShowPass.showPass(password_shown, confirm_pass_shown, password, confirm_pass);
    }

    public void Login(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
    }
}
