package com.example.campusmatch.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String id;
    private String senderId;
    private String text;
    private String timestamp;
    private boolean isSentByMe;

    public ChatMessage() {
    }

    public ChatMessage(String id, String senderId, String text, String timestamp, boolean isSentByMe) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.isSentByMe = isSentByMe;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }
}
