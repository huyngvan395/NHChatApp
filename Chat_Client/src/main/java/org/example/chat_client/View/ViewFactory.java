package org.example.chat_client.View;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.chat_client.Controller.Client.File.FileGroupReceiveController;
import org.example.chat_client.Controller.Client.File.FileSendController;
import org.example.chat_client.Controller.Client.File.FileSingleReceiveController;
import org.example.chat_client.Controller.Client.Image.ImageGroupReceiveController;
import org.example.chat_client.Controller.Client.Image.ImageSendController;
import org.example.chat_client.Controller.Client.Image.ImageSingleReceiveController;
import org.example.chat_client.Controller.Client.Message.MessageGroupReceiveController;
import org.example.chat_client.Controller.Client.Message.MessageSendController;
import org.example.chat_client.Controller.Client.Message.MessageSingleReceiveController;
import org.example.chat_client.Model.Model;

import java.io.IOException;

public class ViewFactory {

    public ViewFactory() {
        this.MenuSettingOptions = new SimpleObjectProperty<>();
        this.MenuChatOptions=new SimpleObjectProperty<>();
        this.LoginOptions=new SimpleObjectProperty<>();
        this.ChatOption=new SimpleObjectProperty<>();
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
        if(Model.getInstance().getCurrentClient()==null){
            stage.setTitle("NH Chat App");
        }else{
            stage.setTitle("NH Chat App - "+ Model.getInstance().getCurrentClient().getName());
        }
        stage.setResizable(false);
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }

//    ---------------------Login Page---------------------------
    private AnchorPane Signup;
    private AnchorPane Login;
    private AnchorPane ForgotPass;
    private AnchorPane Verification;
    private AnchorPane ChangePassword;
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

    public AnchorPane getForgotPass(){
        try{
            ForgotPass=new FXMLLoader(getClass().getResource("/FXML/Login/forgot_pass.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return ForgotPass;
    }

    public AnchorPane getChangePass(){
        try{
            ChangePassword=new FXMLLoader(getClass().getResource("/FXML/Login/change_pass.fxml")).load();
        }catch(IOException e){
            e.printStackTrace();
        }
        return ChangePassword;
    }

    public AnchorPane getVerification(){
        try{
            Verification=new FXMLLoader(getClass().getResource("/FXML/Login/verification.fxml")).load();
        }catch(IOException e){
            e.printStackTrace();
        }
        return Verification;
    }

    public ObjectProperty<LoginOptions> getLoginOptions() {
        return LoginOptions;
    }

//    ---------------------Client Page--------------------------
//    Menu main
    private AnchorPane MenuChatBot;
    private AnchorPane MenuChatSingle;
    private AnchorPane MenuChatGroup;
    private BorderPane MessageChatBoxBot;
    private BorderPane MessageChatBox;
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

    public BorderPane getMessageChatBox() {
        try{
            MessageChatBox=new FXMLLoader(getClass().getResource("/FXML/Client/message_chat_single_box.fxml")).load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return MessageChatBox;
    }

    public BorderPane getMessageChatBoxBot() {
        try{
            MessageChatBoxBot=new FXMLLoader(getClass().getResource("/FXML/Client/message_chat_box_bot.fxml")).load();
        }catch(IOException e){
            e.printStackTrace();
        }
        return MessageChatBoxBot;
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

//    -------------------------menu-chat----------------------------
    private final ObjectProperty<ChatOption> ChatOption;

    public ObjectProperty<ChatOption> getChatOption(){
        return ChatOption;
    }

//    --------------------------message-contain--------------------------------
//    text
    private AnchorPane MessageSend;
    private AnchorPane MessageSingleReceive;
    private AnchorPane MessageGroupReceive;

    public AnchorPane getMessageSend(String message, String time_created){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Message/message_send.fxml"));
            MessageSend=fxmlLoader.load();
            MessageSendController messageSendController=fxmlLoader.getController();
            messageSendController.setTimeCreated(time_created);
            messageSendController.setMessage(message);
        }catch(IOException e){
            e.printStackTrace();
        }
        return MessageSend;
    }

    public AnchorPane getMessageSingleReceive(String message, String time_created){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Message/message_single_receive.fxml"));
            MessageSingleReceive=fxmlLoader.load();
            MessageSingleReceiveController messageSingleReceiveController=fxmlLoader.getController();
            messageSingleReceiveController.setTimeCreated(time_created);
            messageSingleReceiveController.setMessage(message);
        }catch (IOException e){
            e.printStackTrace();
        }
        return MessageSingleReceive;
    }

    public AnchorPane getMessageGroupReceive(String time_created, String message, String image_avatar){
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Message/message_group_receive.fxml"));
            MessageGroupReceive=fxmlLoader.load();
            MessageGroupReceiveController messageGroupReceiveController=fxmlLoader.getController();
            messageGroupReceiveController.setTimeCreated(time_created);
            messageGroupReceiveController.setMessage(message);
            messageGroupReceiveController.setImage(image_avatar);
        }catch(IOException e){
            e.printStackTrace();
        }
        return MessageGroupReceive;
    }

//    image
    private AnchorPane ImageSend;
    private AnchorPane ImageSingleReceive;
    private AnchorPane ImageGroupReceive;

    public AnchorPane getImageSend(String time_created, String image) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Image/image_send.fxml"));
            ImageSend=fxmlLoader.load();
            ImageSendController imageSendController=fxmlLoader.getController();
            imageSendController.setTime_created(time_created);
            imageSendController.setImage_contain(image);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ImageSend;
    }

    public AnchorPane getImageSingleReceive( String image, String time_created) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Image/image_single_receive.fxml"));
            ImageSingleReceive=fxmlLoader.load();
            ImageSingleReceiveController imageSingleReceiveController=fxmlLoader.getController();
            imageSingleReceiveController.setTime_created(time_created);
            imageSingleReceiveController.setImage_contain(image);
        }catch(IOException e){
            e.printStackTrace();
        }
        return ImageSingleReceive;
    }

    public AnchorPane getImageGroupReceive(String time_created, String image, String image_avatar) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/Image/image_group_receive.fxml"));
            ImageGroupReceive=fxmlLoader.load();
            ImageGroupReceiveController imageGroupReceiveController=fxmlLoader.getController();
            imageGroupReceiveController.setTimeCreated(time_created);
            imageGroupReceiveController.setImageAvatar(image_avatar);
            imageGroupReceiveController.setImageContain(image);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ImageGroupReceive;
    }

//    file
    private AnchorPane FileSend;
    private AnchorPane FileSingleReceive;
    private AnchorPane FileGroupReceive;

    public AnchorPane getFileSend(String time_created, String filename) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/File/file_send.fxml"));
            FileSend=fxmlLoader.load();
            FileSendController fileSendController=fxmlLoader.getController();
            fileSendController.setTimeCreated(time_created);
            fileSendController.setFileName(filename);
        }catch (IOException e){
            e.printStackTrace();
        }
        return FileSend;
    }

    public AnchorPane getFileSingleReceive(String time_created, String filename) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/File/file_single_receive.fxml"));
            FileSingleReceive=fxmlLoader.load();
            FileSingleReceiveController fileSingleReceiveController=fxmlLoader.getController();
            fileSingleReceiveController.setTimeCreated(time_created);
            fileSingleReceiveController.setFileName(filename);
        }catch (IOException e){
            e.printStackTrace();
        }
        return FileSingleReceive;
    }

    public AnchorPane getFileGroupReceive(String time_created, String filename, String image_avatar) {
        try{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/File/file_group_receive.fxml"));
            FileGroupReceive=fxmlLoader.load();
            FileGroupReceiveController fileGroupReceiveController=fxmlLoader.getController();
            fileGroupReceiveController.setTimeCreated(time_created);
            fileGroupReceiveController.setFileName(filename);
            fileGroupReceiveController.setImage_avatar(image_avatar);
        }catch (IOException e){
            e.printStackTrace();
        }
        return FileGroupReceive;
    }
}
