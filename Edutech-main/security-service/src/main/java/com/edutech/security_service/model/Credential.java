package com.edutech.security_service.model;

import lombok.Data;

@Data
public class Credential {
    private Long id;
    private String username;
    private String password;
}