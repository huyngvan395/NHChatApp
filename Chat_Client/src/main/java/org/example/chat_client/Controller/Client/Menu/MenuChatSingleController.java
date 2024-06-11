package org.example.chat_client.Controller.Client.Menu;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.chat_client.Model.Client;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.ClientOnlineCellFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuChatSingleController implements Initializable {
    @FXML
    private TextField search_field;
    @FXML
    private Button search_btn;
    @FXML
    private ListView<Client> listView_ClientOnline;
    @FXML
    private ListView<Client> listView_ClientCell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setItemListView_ClientOnline();
        Model.getInstance().updateClientOnline();
        listenerOnlineList();
    }

    public void setItemListView_ClientOnline() {
        ObservableList<Client> list = Model.getInstance().getClientOnlineList();
        listView_ClientOnline.setItems(list);
        listView_ClientOnline.setCellFactory(listView->new ClientOnlineCellFactory());
    }

    public void listenerOnlineList(){
        Platform.runLater(()->{
            Model.getInstance().getClientOnlineList().addListener((ListChangeListener<? super Client>) change->{
                setItemListView_ClientOnline();
            });
        });
    }

}
