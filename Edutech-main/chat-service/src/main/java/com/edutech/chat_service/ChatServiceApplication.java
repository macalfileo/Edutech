package com.edutech.chat_service;

import com.edutech.chat_service.model.Message;
import com.edutech.chat_service.service.ChatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ChatServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);

        ChatService chatService = new ChatService();

        Message message = new Message();
        message.setId(1L);
        message.setSender("Carlos");
        message.setContent("Hola, ¿cómo están?");

        chatService.sendMessage(message);
        chatService.getAllMessages().forEach(m -> {
            System.out.println(m.getSender() + ": " + m.getContent());
        });
    }
}