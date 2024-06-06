module org.example.chat_client {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.chat_client to javafx.fxml;
    opens org.example.chat_client.Controller.Client;
    opens org.example.chat_client.Controller.Client.Menu;
    opens org.example.chat_client.Controller.Client.File;
    opens org.example.chat_client.Controller.Client.Image;
    opens org.example.chat_client.Controller.Client.Message;
    opens org.example.chat_client.Controller.Client.Setting;
    opens org.example.chat_client.Controller.Login;
    exports org.example.chat_client;
    exports org.example.chat_client.Controller.Client;
    exports org.example.chat_client.Controller.Client.Image;
    exports org.example.chat_client.Controller.Client.Message;
    exports org.example.chat_client.Controller.Client.File;
    exports org.example.chat_client.Controller.Login;
    exports org.example.chat_client.Controller.Client.Setting;
    exports org.example.chat_client.Controller.Client.Menu;
}