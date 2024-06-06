package org.example.chatserver.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ChatBotAPI {
    public static String sendMessageToAPI(String message) {
        // Tạo yêu cầu API với tin nhắn được truyền vào
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chat-gpt26.p.rapidapi.com/"))
                .header("x-rapidapi-key", "cae62bb29emsh2b7fbec48db98b5p1d0127jsn88fa792857bc")
                .header("x-rapidapi-host", "chat-gpt26.p.rapidapi.com")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}]}"))
                .build();

        // Gửi yêu cầu và nhận phản hồi từ API
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
//         in ra chuỗi phản hồi json
//        System.out.println(response.body());
//        phân tích chuỗi và lấy phần content
        return getMessageFromChatGPTResponse(response.body());
    }

    public static String getMessageFromChatGPTResponse(String response) {
        try {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray choices = jsonObject.getAsJsonArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                if (message != null) {
                    return message.get("content").getAsString();
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return "Lỗi";
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        boolean check=true;
//        while(check){
//            String question;
//            Scanner scanner = new Scanner(System.in);
//            question = scanner.nextLine();
//            if(question.equals("exit")){
//                check=false;
//            }
//            else{
//                System.out.println(sendMessageToAPI(question));
//            }
//
//        }
//    }
}
