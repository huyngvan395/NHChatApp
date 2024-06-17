package org.example.chat_client.Model;

import java.time.LocalDateTime;

public class Message {
    private String senderID;
    private String message;
    private String messageType;
    private String timeSend;

    public Message(String senderID, String message, String messageType, String timeSend){
        this.senderID=senderID;
        this.message=message;
        this.messageType=messageType;
        this.timeSend=timeSend;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }
}
