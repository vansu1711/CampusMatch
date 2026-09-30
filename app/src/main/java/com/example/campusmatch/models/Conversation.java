package com.example.campusmatch.models;

import java.io.Serializable;

public class Conversation implements Serializable {
    private String id;
    private UserProfile partnerProfile;
    private String lastMessage;
    private String lastMessageTime;
    private boolean hasUnread;

    public Conversation() {
    }

    public Conversation(String id, UserProfile partnerProfile, String lastMessage, String lastMessageTime, boolean hasUnread) {
        this.id = id;
        this.partnerProfile = partnerProfile;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.hasUnread = hasUnread;
    }

    public String getId() {
        return id;
    }

    public UserProfile getPartnerProfile() {
        return partnerProfile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isHasUnread() {
        return hasUnread;
    }

    public void setHasUnread(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }
}
