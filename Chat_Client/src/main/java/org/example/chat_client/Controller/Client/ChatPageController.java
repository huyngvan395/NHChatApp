package org.example.chat_client.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatPageController implements Initializable {
    @FXML
    private BorderPane chat_page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getMenuChatOptions().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case Bot -> {
                    chat_page.setCenter(Model.getInstance().getViewFactory().getMenuChatBot());
                    chat_page.setRight(Model.getInstance().getViewFactory().getMessageChatBoxBot());
                }
                case Single -> {
                    chat_page.setCenter(Model.getInstance().getViewFactory().getMenuChatSingle());
                    chat_page.setRight(Model.getInstance().getViewFactory().getMessageChatBox());
                }
                case Group -> {
                    chat_page.setCenter(Model.getInstance().getViewFactory().getMenuChatGroup());
                    chat_page.setRight(Model.getInstance().getViewFactory().getMessageChatBox());
                }
                default -> {
                    chat_page.setCenter(Model.getInstance().getViewFactory().getMenuChatSingle());
                    chat_page.setRight(Model.getInstance().getViewFactory().getMessageChatBox());
                }
            }
        });
    }
}
