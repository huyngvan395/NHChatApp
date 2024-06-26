package org.example.chat_client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.chat_client.Model.Model;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Model.getInstance().getViewFactory().showLoginPage();
    }

    public void stop() throws Exception {
        super.stop();
        Model.getInstance().getSocketClient().sendMessage("remove");
        Model.getInstance().setRunning(false);
        Model.getInstance().getMessageResponseQueue().put("stopThread");
    }

}
