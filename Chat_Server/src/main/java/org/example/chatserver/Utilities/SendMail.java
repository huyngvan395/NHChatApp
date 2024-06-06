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
            String content= "<html><body style='font-family: Arial, sans-serif; background: #104E8B;color: white; border-radius:4px; margin:5px 180px'>"+
                            "<h2 style=' margin:10px 20px '>Your confirmation code is</h2>"+
                            "<h1 style=' letter-spacing:3px; display:inline-block; background-color:#8E5DF1; margin:0 20px; justify-content:center; border-radius: 5px'>"+code+"</h1>"+
                            "<h2 style=' margin:10px 20px'>Return to NH Chat App and enter the above code to change your password</h2>"+
                            "<body><html>";
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
