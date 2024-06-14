package org.example.chat_client.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.example.chat_client.Controller.Client.ChatCellController;
import org.example.chat_client.Model.Client;

import java.io.IOException;

public class ClientConversationCellFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client item, boolean empty){
        super.updateItem(item, empty);
        if(item==null || empty){
            setGraphic(null);
            setText(null);
        }else{
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/FXML/Client/chat_cell.fxml"));
            try{
                setGraphic(fxmlLoader.load());
                ChatCellController chatCellController=fxmlLoader.getController();
                chatCellController.setName(item.getName());
                if(item.getImage()!=null){
                    chatCellController.setImage_avatar(item.getImage());
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
