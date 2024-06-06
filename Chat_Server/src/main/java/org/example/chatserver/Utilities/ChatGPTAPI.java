package org.example.chatserver.Utilities;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ChatGPTAPI {

    // Phương thức để gửi yêu cầu đến API ChatGPT với đoạn prompt đầu vào
    public static String chatGPT(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "sk-proj-quDAvXhBVEGZl5bUabqyT3BlbkFJlirsGrioDnCKgxzbcApL";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // Nội dung yêu cầu
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Kiểm tra mã phản hồi và đọc phản hồi
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // In ra phản hồi để kiểm tra
//            System.out.println("Response from API: " + response.toString());

            return getMessageFromChatGPTResponse(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi: " + e.getMessage();
        }
    }

    // Phương thức lấy dữ liệu phản hồi
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

//    public static void main(String[] args) {
//        boolean check=true;
//        while(check){
//            String question;
//            Scanner scanner = new Scanner(System.in);
//            question = scanner.nextLine();
//            if(question.equals("exit")){
//                check=false;
//            }
//            else{
//                System.out.println(chatGPT(question));
//            }
//
//        }
//    }
}
