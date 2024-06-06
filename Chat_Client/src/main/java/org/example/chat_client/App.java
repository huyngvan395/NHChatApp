package org.example.chat_client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.chat_client.Model.Model;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Model.getInstance().getViewFactory().showClientPage();
    }
}
