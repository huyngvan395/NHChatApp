package org.example.chat_client.Model;

public class Caller {
    private String clientID;
    private String clientHost;
    private String name;
    private String ava;

    public Caller(String clientID, String clientHost, String name, String ava) {
        this.clientID = clientID;
        this.clientHost = clientHost;
        this.name = name;
        this.ava = ava;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }
}
