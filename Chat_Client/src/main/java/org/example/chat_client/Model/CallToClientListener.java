package org.example.chat_client.Model;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

public class CallToClientListener implements MessageListener{
    @Override
    public void onMessageReceived(String message) {
        String[] messageParts = message.split("\\|");
        System.out.println(message+" callToClientListener");
        if(message.startsWith("call-request")){
            Platform.runLater(()->{
                String clientHost = messageParts[1];
                String clientId = messageParts[2];
                String senderName = messageParts[3];
                String senderAva = messageParts[4];
                Model.getInstance().targetCallObjectProperty().set(new Caller(clientId, clientHost, senderName, senderAva));
                AnchorPane ReceiveCallPage = Model.getInstance().getViewFactory().getReceiveCallPage(senderName,senderAva);
                Model.getInstance().getViewFactory().showCallForm(ReceiveCallPage,"HL Chat App Call");
            });
        }
    }
}
