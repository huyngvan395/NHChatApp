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
import org.example.chat_client.Model.Group;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.ClientGroupConversationCellFactory;
import org.example.chat_client.View.ClientOnlineCellFactory;
import org.example.chat_client.View.MenuChatOptions;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuChatGroupController implements Initializable {
    @FXML
    private TextField search_field;
    @FXML
    private Button search_btn;
    @FXML
    private ListView<Client> listView_ClientOnline;
    @FXML
    private ListView<Group> listView_Group;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setItemListView_ClientOnline();
        listenerOnlineList();
        setItemListView_Group();
        listenerEventClickGroupCell();
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

    public void setItemListView_Group() {
        ObservableList<Group> listGroup=Model.getInstance().getGroupList();
        listView_Group.setItems(listGroup);
        listView_Group.setCellFactory(listView->new ClientGroupConversationCellFactory());
    }

    public void listenerEventClickGroupCell(){
        listView_Group.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                setGroupTarget(newValue);
                Model.getInstance().getViewFactory().getMenuChatOptions().set(MenuChatOptions.ReloadGroup);
            });
        });
    }

    public void setGroupTarget(Group group){
        Model.getInstance().targetGroupObjectProperty().set(group);
    }
}
