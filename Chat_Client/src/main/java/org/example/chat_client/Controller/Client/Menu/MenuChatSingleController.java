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
import org.example.chat_client.View.ClientSingleConversationCellFactory;
import org.example.chat_client.View.ClientOnlineCellFactory;
import org.example.chat_client.View.MenuChatOptions;

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
        setItemListView_Client();
        listenerOnlineList();
        listenerEventClickClientCell();
    }

    public void setItemListView_ClientOnline() {
        ObservableList<Client> listOnline = Model.getInstance().getClientOnlineList();
        listView_ClientOnline.setItems(listOnline);
        listView_ClientOnline.setCellFactory(listView->new ClientOnlineCellFactory());
    }

    public void listenerOnlineList(){
        Model.getInstance().getClientOnlineList().addListener((ListChangeListener<? super Client>) change -> {
            Platform.runLater(()->{
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                        setItemListView_ClientOnline();
                    }
                }
            });
        });
    }

    public void setItemListView_Client(){
        ObservableList<Client> list = Model.getInstance().getListClient();
        listView_ClientCell.setItems(list);
        listView_ClientCell.setCellFactory(listView->new ClientSingleConversationCellFactory());
    }

    public void listenerEventClickClientCell(){
        listView_ClientCell.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->{
            Platform.runLater(()->{
                setInfoTargetClient(newValue);
                System.out.println(Model.getInstance().targetClientObjectProperty().get().getName());
                Model.getInstance().getViewFactory().getMenuChatOptions().set(MenuChatOptions.ReloadSingle);
            });
        }));
    }

    private void setInfoTargetClient(Client client) {
        Model.getInstance().targetClientObjectProperty().set(client);
    }

}
