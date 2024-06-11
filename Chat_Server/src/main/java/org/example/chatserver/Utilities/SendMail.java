package org.example.chatserver.Utilities;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

public class SendMail {
    private static final String email="nhchatapp@gmail.com";
    private static final String password="cvswshhcgqzpcdsk";

    public static String randomCode(){
        StringBuilder code=new StringBuilder();
        for(int i=0;i<6;i++){
            Random random=new Random();
            code.append(random.nextInt(0,9));
        }
        return code.toString();
    }

    private static Properties getProperties() {
        Properties pros=new Properties();
        pros.put("mail.smtp.auth", "true");
        pros.put("mail.smtp.starttls.enable", "true");
        pros.put("mail.smtp.host", "smtp.gmail.com");
        pros.put("mail.smtp.port", "587");
        return pros;
    }

    public static void sendCode(String user_mail,String code){
        Properties pros = getProperties();

        Session session=Session.getInstance(pros, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        });

        try {
            Message message= new MimeMessage(session);
            message.setFrom(new InternetAddress(email,"NH Chat App"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user_mail));
            message.setSubject("Verification Code");
            String content=  "<body style='font-family: Arial, sans-serif; background: #104E8B;color: white; border-radius:4px;'>" +
                    "<div style='width: 300px; margin:auto; padding:20px 0'>" +
                    "<h2 style='text-align: center;'>Your confirmation code is</h2>" +
                    "<div style='text-align: center; background-color:#8E5DF1; border-radius: 5px; padding: 10px;'>" +
                    "<h1 style='letter-spacing: 3px; display: inline-block; margin: 0;'>"+code+"</h1>" +
                    "</div>" +
                    "<h2 style='text-align: center;color:white'>Return to NH Chat App and enter the above code to change your password</h2>" +
                    "</div>" +
                    "</body>";
            message.setContent(content,"text/html");
            Transport.send(message);
//            System.out.println("Message sent successfully");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        sendCode("ngvanhuy0000@gmail.com",randomCode());
//    }
}
