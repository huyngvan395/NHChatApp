package org.example.chat_client.Controller.Client;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.chat_client.Model.Client;
import org.example.chat_client.Model.Model;
import org.example.chat_client.View.ClientSelectCellFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class CreateGroupPageController implements Initializable {
    @FXML
    private ImageView image_group;
    @FXML
    private Button create_btn;
    @FXML
    private TextField group_name;
    @FXML
    private ListView<Client> listView_client;
    private String base64ImageGroup;
    private String directoryImageGroup="client_storage/Image_Group";
    private String imageGroupName;
    private String pathImageGroup;
    private final Gson gson = new Gson();
    private String nameImage;
    private File imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView_client.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setListClient();
        listView_client.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();

            while (node != null && node != listView_client && !(node instanceof ListCell)) {
                node = node.getParent();
            }

            if (node instanceof ListCell) {
                evt.consume();

                ListCell<Client> cell = (ListCell<Client>) node;
                ListView<Client> lv = cell.getListView();

                lv.requestFocus();

                if (!cell.isEmpty()) {

                    int index = cell.getIndex();
                    if (cell.isSelected()) {
                        lv.getSelectionModel().clearSelection(index);
                    } else {
                        lv.getSelectionModel().select(index);
                    }
                }
            }
        });
        image_group.setOnMouseClicked(evt -> setImage_group());
        create_btn.setOnAction(e->createGroup());
    }

    public void setListClient(){
        ObservableList<Client> list= Model.getInstance().getListClient();
        listView_client.setItems(list);
        listView_client.setCellFactory(cell->new ClientSelectCellFactory());

    }

    public void createGroup(){
        if(!group_name.getText().isEmpty() && listView_client.getSelectionModel().getSelectedItems().size()>1 && !base64ImageGroup.isEmpty()){
            String nameGroup = group_name.getText();
            ObservableList<Client> selectedItems = listView_client.getSelectionModel().getSelectedItems();
//            ObservableList<Client> list= FXCollections.observableArrayList(selectedItems);
            List<Client> list = new ArrayList<>(selectedItems);
            list.add(Model.getInstance().getCurrentClient());
            String listClientAddGroupJSon=gson.toJson(list);
            saveImageGroup(this.imageFile);
            Model.getInstance().getSocketClient().sendMessage("create_group|"+nameGroup+"|"+pathImageGroup+"|"+base64ImageGroup+"|"+listClientAddGroupJSon+"|"+imageGroupName);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Create Group");
            alert.setHeaderText(null);
            alert.setContentText("Create Group Successfully");
            alert.showAndWait();
            Model.getInstance().getViewFactory().closeStage((Stage)create_btn.getScene().getWindow());
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a group name, add group image and add at least 2 people ");
            alert.showAndWait();
        }
    }

    public void setImage_group(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image group");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png","*.jpeg", "*.gif"));
        File imageFile = fileChooser.showOpenDialog(image_group.getScene().getWindow());
        if(imageFile != null){
            byte[] bytes=null;
            try{
                bytes = Files.readAllBytes(imageFile.toPath());
            }catch (IOException e){
                e.printStackTrace();
            }
            this.base64ImageGroup= Base64.getEncoder().encodeToString(bytes);
            Image image =new Image(imageFile.toURI().toString());
            this.image_group.setImage(image);
            this.imageFile=imageFile;
        }
    }

    public void saveImageGroup(File imageFile){
        File localDir = new File(directoryImageGroup);
        if(!localDir.exists()){
            localDir.mkdir();
        }
        File localImage = new File(localDir, imageFile.getName());
        this.pathImageGroup=localImage.toURI().toString();
        this.imageGroupName=imageFile.getName();
        try{
            Files.copy(imageFile.toPath() , localImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
