package org.example.chatserver;

import org.example.chatserver.Controller.ClientThreadHandle;
import org.example.chatserver.Controller.ClientThreadManager;
import org.example.chatserver.Model.Model;

import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT_TCP = 8888;
    private static final int PORT_UDP=9999;


    public static void main(String[] args)  {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        DatagramSocket datagramSocket = null;
        System.out.println("Server is running on port: " + PORT_TCP);
        try {
            serverSocket = new ServerSocket(PORT_TCP);
//            datagramSocket = new DatagramSocket(PORT_UDP);
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
