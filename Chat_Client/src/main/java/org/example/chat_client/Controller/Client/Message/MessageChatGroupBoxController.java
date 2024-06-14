package org.example.chat_client.Controller.Client.Message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageChatGroupBoxController implements Initializable {
    @FXML
    private ImageView image_avatar_group;
    @FXML
    private Label name_group;
    @FXML
    private TextArea Enter_message;
    @FXML
    public Button sendImage_btn;
    @FXML
    public Button sendFile_btn;
    @FXML
    public Button sendMessage_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
