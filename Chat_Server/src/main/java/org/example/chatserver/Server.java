package org.example.chatserver;

import org.example.chatserver.Controller.ClientThreadHandle;
import org.example.chatserver.Controller.ClientThreadManager;
import org.example.chatserver.Model.Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 8888;

    public static void main(String[] args)  {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        System.out.println("Server is running on port: " + PORT);
        try {
            serverSocket = new ServerSocket(PORT);
            while(true){
                Socket connection = serverSocket.accept();
                ClientThreadHandle clientThreadHandle = new ClientThreadHandle(connection);
                Model.getInstance().getClientThreadManager().addClientHandler(clientThreadHandle);
                threadPool.execute(clientThreadHandle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
