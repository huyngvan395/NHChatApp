package org.example.chat_client.Controller.Client.Call;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.chat_client.Model.Client;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class ReceiveCallPageController implements Initializable {
    @FXML
    private ImageView img_ava;
    @FXML
    private Text user_name;
    @FXML
    private Button receive_button;
    @FXML
    private Button refuse_button;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refuse_button.setOnAction(e->refuseCall());
        receive_button.setOnAction(e->receiveCall());
    }

    public void receiveCall(){
        String targetHost = Model.getInstance().targetCallObjectProperty().get().getClientHost();
        String receiverID = Model.getInstance().targetCallObjectProperty().get().getClientID();
        Client currentClient = Model.getInstance().getCurrentClient();
//        startCall(targetHost);
        Model.getInstance().getSocketClient().sendMessage("call-response|accept|"+receiverID+"|"+currentClient.getName()+"|"+currentClient.getImage());
        Model.getInstance().getViewFactory().closeStage((Stage) receive_button.getScene().getWindow());
        String senderName = Model.getInstance().targetCallObjectProperty().get().getName();
        String senderAva = Model.getInstance().targetCallObjectProperty().get().getAva();
        AnchorPane callPage = Model.getInstance().getViewFactory().getCallPage(senderName,senderAva, "received-call");
        System.out.println("Chuyển trang thành công");
        Model.getInstance().getViewFactory().showCallForm(callPage,"HL Chat App Call");
    }

    public void refuseCall(){
        String receiverID="";
        if(Model.getInstance().targetCallObjectProperty().get() != null){
            receiverID = Model.getInstance().targetCallObjectProperty().get().getClientID();
        }
        Model.getInstance().getSocketClient().sendMessage("call-response|refuse|"+receiverID);
        Model.getInstance().targetCallObjectProperty().set(null);
    }

    public void setImg_ava(String imgAva){
        Image image = new Image(imgAva);
        img_ava.setImage(image);
    }

    public void setUser_name(String userName){
        user_name.setText(userName);
    }

}
