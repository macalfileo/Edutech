package com.edutech.community_service.model;

import lombok.Data;

@Data
public class Post {
    private Long id;
    private String author;
    private String message;
}