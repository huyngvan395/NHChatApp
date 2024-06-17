package org.example.chat_client.Controller.Client.File;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class FileSendController implements Initializable {
    @FXML
    private Label file_name;
    @FXML
    private Label time_created;
    @FXML
    private Button download;

    private  String directory="client_storage/File";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        download.setOnAction(e->Download());
    }

    public void setFileName(String file_name) {
        this.file_name.setText(file_name);
    }

    public void setTimeCreated(String time_created) {
        this.time_created.setText(time_created);
    }

    public void Download(){
        String filePath = directory + "/" + file_name.getText();

        String userHome = System.getProperty("user.home");
        File downloadsDirectory = new File(userHome + "/Downloads");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        fileChooser.setInitialDirectory(downloadsDirectory);

        fileChooser.setInitialFileName(file_name.getText());

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {

                File fileToDownload = new File(filePath);

                Path targetPath = selectedFile.toPath();

                Files.copy(fileToDownload.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File downloaded successfully to: " + targetPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
