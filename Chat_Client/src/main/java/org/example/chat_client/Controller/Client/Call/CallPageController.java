package org.example.chat_client.Controller.Client.Call;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.chat_client.Model.Caller;
import org.example.chat_client.Model.MessageListener;
import org.example.chat_client.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class CallPageController implements Initializable, MessageListener {
    @FXML
    private Button end_call_button;
    @FXML
    private ImageView img_ava;
    @FXML
    private Text user_name;

    @FXML
    private ImageView localView;
    @FXML
    private ImageView remoteView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        receive_button.setOnAction(e->receiveCall());
//        refuse_button.setOnAction(e->refuseCall());
        Model.getInstance().getMessageHandler().addMessageListener(this);
        end_call_button.setOnAction(e->stopCall());
    }

    @Override
    public void onMessageReceived(String message) {
        String[] messageParts = message.split("\\|");
        System.out.println(message+"callpage");
        if(message.startsWith("call-response")){
            if(messageParts[1].equals("accept")){
                String targetHost = messageParts[2];
                String targetID = messageParts[3];
                String targetName = messageParts[4];
                String targetAva = messageParts[5];
                Model.getInstance().targetCallObjectProperty().set(new Caller(targetID,targetHost,targetName,targetAva));
                Platform.runLater(()-> startCall(targetHost));
            }else if(message.startsWith("call-request")) {
                Platform.runLater(()->{
                    Model.getInstance().getViewFactory().closeStage((Stage) user_name.getScene().getWindow());
                    String targetCallName = Model.getInstance().targetCallObjectProperty().get().getName();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText(targetCallName+" không nhận cuộc gọi của bạn");
                    alert.showAndWait();
                });
            }else if(message.startsWith("stop-call")){
                Platform.runLater(()->{
                    stopCall();
                    Model.getInstance().getViewFactory().closeStage((Stage) user_name.getScene().getWindow());
                    String targetCallName = Model.getInstance().targetCallObjectProperty().get().getName();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Cuộc gọi của bạn và "+targetCallName+" đã kết thúc");
                    alert.showAndWait();
                    Model.getInstance().targetCallObjectProperty().set(null);
                });
            }
        } else if (message.startsWith("call-request")){
            System.out.println("Hiển thị nút nghe");
        }
    }

    // Trong phương thức startCall(String targetHost)
    private void startCall(String targetHost) {
        Model.getInstance().getSocketStreaming().start();
        Thread localPreviewThread = new Thread(() -> Model.getInstance().getSocketStreaming().videoCallCaptureLoop(localView));
        localPreviewThread.setDaemon(true);
        localPreviewThread.start();
        Thread sendVideoCallThread = new Thread(() -> Model.getInstance().getSocketStreaming().sendVideoCallLoop(targetHost));
        sendVideoCallThread.setDaemon(true);
        sendVideoCallThread.start();
        Thread sendAudioCallThread = new Thread(() -> Model.getInstance().getSocketStreaming().sendAudioCallLoop(targetHost));
        sendAudioCallThread.setDaemon(true);
        sendAudioCallThread.start();
        Thread receiveVideoCallThread = new Thread(() -> Model.getInstance().getSocketStreaming().receiveVideoCallLoop(remoteView));
        receiveVideoCallThread.setDaemon(true);
        receiveVideoCallThread.start();
        Thread receiveAudioCallThread = new Thread(() -> Model.getInstance().getSocketStreaming().receiveAudioCallLoop());
        receiveAudioCallThread.setDaemon(true);
        receiveAudioCallThread.start();
        // Lưu threads vào SocketStreaming để stop() có thể interrupt
        Model.getInstance().getSocketStreaming().setThreads(
                localPreviewThread, sendVideoCallThread, sendAudioCallThread,
                receiveVideoCallThread, receiveAudioCallThread
        );
    }
    // Trong stopCall(), gọi stop() trước khi đóng stage và gửi message
    public void stopCall() {
        String receiverID = Model.getInstance().targetCallObjectProperty().get().getClientID();
        Model.getInstance().getSocketStreaming().stop();  // Interrupt threads và cleanup
        Model.getInstance().getViewFactory().closeStage((Stage) img_ava.getScene().getWindow());
        Model.getInstance().getSocketClient().sendMessage("stop-call|" + receiverID);
        Model.getInstance().targetCallObjectProperty().set(null);
    }

    public void startCallFromOutside(){
        if(Model.getInstance().targetCallObjectProperty().get() != null){
            String targetHost = Model.getInstance().targetCallObjectProperty().get().getClientHost();
            Platform.runLater(()->startCall(targetHost));
        }
    }


//    public void stopCall(){
//        String receiverID = Model.getInstance().targetCallObjectProperty().get().getClientID();
//        Model.getInstance().getSocketStreaming().stop();
//        Model.getInstance().getViewFactory().closeStage((Stage) img_ava.getScene().getWindow());
//        Model.getInstance().getSocketClient().sendMessage("stop-call|"+receiverID);
//        Model.getInstance().targetCallObjectProperty().set(null);
//    }

    public void setImg_ava(String imageSend){
        Image image = new Image(imageSend);
        img_ava.setImage(image);
    }

    public void setUser_name(String user_name){
        this.user_name.setText(user_name);
    }

}
