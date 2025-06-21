package com.edutech.userprofile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UserprofileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserprofileServiceApplication.class, args);

    }
}