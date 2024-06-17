package org.example.chat_client.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.example.chat_client.Controller.Client.ChatGroupCellController;
import org.example.chat_client.Model.Group;

import java.io.IOException;

public class ClientGroupConversationCellFactory extends ListCell<Group> {

    @Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Client/chat_group_cell.fxml"));
            try{
                setGraphic(fxmlLoader.load());
                ChatGroupCellController controller = fxmlLoader.getController();
                if(item.getImage()!=null){
                    controller.setImage_group(item.getImage());
                }
                controller.setName_group(item.getName());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
