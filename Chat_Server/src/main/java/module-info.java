module Chat.Server {
    requires com.google.gson;
    requires jakarta.mail;
    requires java.net.http;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires cloudinary.core;
    opens org.example.chatserver.Model to com.google.gson;

}