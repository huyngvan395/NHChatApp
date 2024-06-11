package org.example.chat_client.Model;

import java.time.LocalDate;

public class Client {
    private String clientID;
    private String name;
    private String email;
    private String image;
    public Client(String clientID, String name, String email, String image) {
        this.clientID = clientID;
        this.name = name;
        this.email = email;
        this.image = image;
    }
    public String getClientID() {
        return clientID;
    }
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
