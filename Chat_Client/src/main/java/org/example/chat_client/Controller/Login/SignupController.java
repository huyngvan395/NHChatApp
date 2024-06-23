package org.example.chat_client.Controller.Login;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.chat_client.Model.Model;
import org.example.chat_client.Model.ShowPass;
import org.example.chat_client.Ultilities.Security;
import org.example.chat_client.View.LoginOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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
    private final String directoryImageAvatar="client_storage/Image_Avatar";
    private String base64ImageAvatar;
    private String avatarName;
    private String pathAvatar;
    private File imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password_shown.setVisible(false);
        confirm_pass_shown.setVisible(false);
        login.setOnAction(e-> Login());
        signup_btn.setOnAction(e->Signup());
        show_pass.setOnAction(e->showPass());
        image_avatar.setOnMouseClicked(e->{
            setImage_Avatar();
        });
    }

    public void Signup(){
        if(name.getText().isEmpty() || ID.getText().isEmpty() || password.getText().isEmpty() || confirm_pass.getText().isEmpty() || email.getText().isEmpty() || base64ImageAvatar==null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields and choose avatar");
            alert.showAndWait();
        }else{
            String ID_value=ID.getText();
            String name_value=name.getText();
            String password_value=password.isVisible()? Security.enCode(password.getText()) : Security.enCode(password_shown.getText());
            String email_value=email.getText();
            saveAvatar(this.imageFile);
            String message="signup|"+ID_value+"|"+name_value+"|"+email_value+"|"+password_value+"|"+base64ImageAvatar+"|"+avatarName+"|"+pathAvatar;
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

    public void setImage_Avatar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg", "*.gif"));
        File imageFile = fileChooser.showOpenDialog(image_avatar.getScene().getWindow());
        if(imageFile != null){
            byte[] bytes=null;
            try{
                bytes = Files.readAllBytes(imageFile.toPath());
            }catch (IOException e){
                e.printStackTrace();
            }
            this.base64ImageAvatar= Base64.getEncoder().encodeToString(bytes);
            Image image =new Image(imageFile.toURI().toString());
            image_avatar.setImage(image);
            this.imageFile=imageFile;
        }
    }

    public void saveAvatar(File imageFile){
        File localDir = new File(directoryImageAvatar);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        String fileExtension=imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
        this.avatarName=ID.getText()+"_avatar"+fileExtension;
        File localImage = new File(localDir, avatarName);
        pathAvatar=localImage.toURI().toString();
        try{
            Files.copy(imageFile.toPath() , localImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showPass(){
        ShowPass.showPass(password_shown, confirm_pass_shown, password, confirm_pass);
    }

    public void Login(){
        Model.getInstance().getViewFactory().getLoginOptions().set(LoginOptions.Login);
    }
}
