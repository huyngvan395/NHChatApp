package org.example.chat_client.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.example.chat_client.Controller.Client.ClientOnlineController;
import org.example.chat_client.Model.Client;

import java.io.IOException;

public class ClientOnlineCellFactory extends ListCell<Client> {

    @Override
    protected void updateItem(Client item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Client/client_online.fxml"));
            try{
                setGraphic(fxmlLoader.load());
                ClientOnlineController clientOnlineController = fxmlLoader.getController();
                if(!(item.getImage()==null)){
                    clientOnlineController.setImage_avatar(item.getImage());
                }
                clientOnlineController.setName(item.getName());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
