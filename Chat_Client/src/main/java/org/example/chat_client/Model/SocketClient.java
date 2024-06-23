package org.example.chat_client.Model;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private final Socket socket;
    private final BufferedReader inText;
    private final BufferedWriter outText;

    public SocketClient() throws IOException {
        this.socket = new Socket("localhost", 8888);
        System.out.println("Client connected");
        this.inText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outText = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message)  {
        try {
            outText.write(message);
            outText.newLine();
            outText.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveResponse(){
        try {
            return inText.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
