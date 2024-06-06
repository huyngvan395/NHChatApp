package org.example.chat_client.Controller.Client.Menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.MenuChatOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuOptionsController implements Initializable {
    @FXML
    private Button chat_single;
    @FXML
    private Button chat_group;
    @FXML
    private Button chat_bot;
    @FXML
    private Button logout;
    @FXML
    private Button client_personal;
    @FXML
    private Button create_group;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chat_single.setOnAction(event -> ChatSingle());
        chat_group.setOnAction(event -> ChatGroup());
        chat_bot.setOnAction(event -> ChatBot());
        logout.setOnAction(event -> Logout());
        client_personal.setOnAction(event -> ClientPersonal());
    }

    public void ChatSingle(){
        Model.getInstance().getViewFactory().getMenuChatOptions().set(MenuChatOptions.Single);
    }

    public void ChatGroup(){
        Model.getInstance().getViewFactory().getMenuChatOptions().set(MenuChatOptions.Group);
    }

    public void ChatBot(){
        Model.getInstance().getViewFactory().getMenuChatOptions().set(MenuChatOptions.Bot);
    }

    public void Logout(){
        Model.getInstance().getViewFactory().closeStage((Stage) logout.getScene().getWindow());
        Model.getInstance().getViewFactory().showLoginPage();
    }

    public void ClientPersonal(){
        Model.getInstance().getViewFactory().showClientSetting();
    }

}
