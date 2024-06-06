package org.example.chat_client.View;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    public ViewFactory() {
        this.MenuSettingOptions = new SimpleObjectProperty<>();
        this.MenuChatOptions=new SimpleObjectProperty<>();
        this.LoginOptions=new SimpleObjectProperty<>();
    }

    public void showLoginPage(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Login/form.fxml"));
        createStage(fxmlLoader);
    }

    public void showClientPage(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Client/chat_page.fxml"));
        createStage(fxmlLoader);
    }

    public void showClientSetting(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Client/Setting/client_setting_page.fxml"));
        createStage(fxmlLoader);
    }

    private void createStage(FXMLLoader fxmlLoader) {
        Scene scene=null;
        try{
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("NH Chat App");
        stage.setResizable(false);
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }

//    ---------------------Login Page---------------------------
    private AnchorPane Signup;
    private AnchorPane Login;
    private final ObjectProperty<LoginOptions> LoginOptions;

    public AnchorPane getSignup() {
        try{
            Signup=new FXMLLoader(getClass().getResource("/FXML/Login/signup.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return Signup;
    }

    public AnchorPane getLogin() {
        try{
            Login=new FXMLLoader(getClass().getResource("/FXML/Login/login.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return Login;
    }

    public ObjectProperty<LoginOptions> getLoginOptions() {
        return LoginOptions;
    }

//    ---------------------Client Page--------------------------
//    Menu main
    private AnchorPane MenuChatBot;
    private AnchorPane MenuChatSingle;
    private AnchorPane MenuChatGroup;
    private final ObjectProperty<MenuChatOptions> MenuChatOptions;

    public AnchorPane getMenuChatBot() {
        try{
            MenuChatBot=new FXMLLoader(getClass().getResource("/FXML/Client/Menu/menu_chat_bot.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return MenuChatBot;
    }

    public AnchorPane getMenuChatSingle() {
        try{
            MenuChatSingle=new FXMLLoader(getClass().getResource("/FXML/Client/Menu/menu_chat_single.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return MenuChatSingle;
    }

    public AnchorPane getMenuChatGroup() {
        try{
            MenuChatGroup=new FXMLLoader(getClass().getResource("/FXML/Client/Menu/menu_chat_group.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return MenuChatGroup;
    }

    public ObjectProperty<MenuChatOptions> getMenuChatOptions() {
        return MenuChatOptions;
    }

//    Menu setting
    private AnchorPane PersonalInformation;
    private AnchorPane SecurityAccount;
    private final ObjectProperty<MenuSettingOptions> MenuSettingOptions;

    public AnchorPane getPersonalInformation() {
        try{
            PersonalInformation=new FXMLLoader(getClass().getResource("/FXML/Client/Setting/personal_info.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return PersonalInformation;
    }

    public AnchorPane getSecurityAccount() {
        try{
            SecurityAccount=new FXMLLoader(getClass().getResource("/FXML/Client/Setting/security_account.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return SecurityAccount;
    }

    public ObjectProperty<MenuSettingOptions> getMenuSettingOptions() {
        return MenuSettingOptions;
    }
}
