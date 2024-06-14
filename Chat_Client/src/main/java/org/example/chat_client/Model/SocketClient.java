package org.example.chat_client.Model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    private final Socket socket;
    private final BufferedReader inText;
    private final BufferedWriter outText;
//    private final BufferedInputStream inStream;
    private final BufferedOutputStream outStream;

    public SocketClient() throws IOException {
        this.socket = new Socket("localhost", 8888);
        System.out.println("Client connected");
        this.inText = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outText = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        this.inStream = new BufferedInputStream(socket.getInputStream());
        this.outStream = new BufferedOutputStream(socket.getOutputStream());
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

    public void sendFileSingle(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try{
                File directory = new File("client_storage/File");
                File targetFile = new File(directory, selectedFile.getName());
                byte[] fileNameBytes = selectedFile.getName().getBytes(StandardCharsets.UTF_8);
                outStream.write(fileNameBytes.length);
                outStream.write(fileNameBytes);

                FileInputStream fis = new FileInputStream(selectedFile);
                FileOutputStream fos = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                    fos.write(buffer, 0, bytesRead);
                }
                outStream.flush();
                outStream.close();
                fis.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendImageSingle(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try{
                File directory = new File("storage/Image");
                File targetFile = new File(directory, selectedFile.getName());
                byte[] fileNameBytes = selectedFile.getName().getBytes(StandardCharsets.UTF_8);
                outStream.write(fileNameBytes.length);
                outStream.write(fileNameBytes);

                FileInputStream fis = new FileInputStream(selectedFile);
                FileOutputStream fos = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                    fos.write(buffer, 0, bytesRead);
                }
                outStream.flush();
                outStream.close();
                fis.close();
                fos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
