package com.edutech.chat_service.model;

import lombok.Data;

@Data
public class Message {
    private Long id;
    private String sender;
    private String content;
}