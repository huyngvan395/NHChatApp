package org.example.chat_client.Controller.Client.Image;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class ImageSendController implements Initializable {
    @FXML
    private ImageView image_contain;
    @FXML
    private Label time_created;
    @FXML
    private Button download;

    private String imagePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        download.setOnAction(e->download());
    }

    public void setImage_contain(String imageSend) {
        Image image= new Image(imageSend);
        this.image_contain.setImage(image);
        this.imagePath=imageSend;
    }

    public void setTime_created(String time_created) {
        this.time_created.setText(time_created);
    }

    public void download(){
        if (imagePath != null) {
            try {
                URL url = new URL(imagePath);
                InputStream inputStream = url.openStream();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );

                String userDownloads = System.getProperty("user.home") + "\\Downloads";
                File initialDirectory = new File(userDownloads);
                fileChooser.setInitialDirectory(initialDirectory);
                String defaultFileName = new File(url.getFile()).getName();
                fileChooser.setInitialFileName(defaultFileName);

                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
