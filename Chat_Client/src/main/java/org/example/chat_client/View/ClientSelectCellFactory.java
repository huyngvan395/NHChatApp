package org.example.chat_client.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.example.chat_client.Controller.Client.ClientCellSelectController;
import org.example.chat_client.Model.Client;

import java.io.IOException;

public class ClientSelectCellFactory extends ListCell<Client> {

    @Override
    protected void updateItem(Client item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Client/client_cell_select.fxml"));
            try{
                setGraphic(fxmlLoader.load());
                ClientCellSelectController controller=fxmlLoader.getController();
                if(item.getImage()!=null){
                    controller.setImage_avatar(item.getImage());
                }
                controller.setName(item.getName());
                if(isSelected()){
                    controller.getSelect().setSelected(true);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
