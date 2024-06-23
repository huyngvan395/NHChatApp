package org.example.chatserver.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ChatBotAPI {
    public static String sendMessageToAPI(String message) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_Key");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chatgpt-42.p.rapidapi.com/geminipro"))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "chatgpt-42.p.rapidapi.com")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"messages\":[{\"role\":\"user\",\"content\":\""+message+"\"}],\"temperature\":0.9,\"top_k\":5,\"top_p\":0.9,\"max_tokens\":256,\"web_access\":false}"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.body());
        return getMessageFromChatGPTResponse(response.body());
    }

    public static String getMessageFromChatGPTResponse(String response) {
        try {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            if (jsonObject.has("result")) {
                return jsonObject.get("result").getAsString();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return "Lỗi khi phân tích phản hồi JSON";
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
