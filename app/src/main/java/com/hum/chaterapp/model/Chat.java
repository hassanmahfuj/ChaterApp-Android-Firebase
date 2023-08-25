package com.hum.chaterapp.model;

import java.util.HashMap;

public class Chat {
    String chatId;
    String name;
    Message lastMessage;
    HashMap<String, Boolean> participants;
    String type;

    public Chat() {
        lastMessage = new Message();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public HashMap<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, Boolean> participants) {
        this.participants = participants;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
