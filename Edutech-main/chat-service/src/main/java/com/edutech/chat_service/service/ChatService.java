package com.edutech.chat_service.service;

import com.edutech.chat_service.model.Message;
import java.util.ArrayList;
import java.util.List;

public class ChatService {
    private final List<Message> messages = new ArrayList<>();

    public void sendMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getAllMessages() {
        return messages;
    }
}