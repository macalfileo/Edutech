package com.edutech.security_service;

import com.edutech.security_service.model.Credential;
import com.edutech.security_service.service.SecurityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SecurityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);

        SecurityService service = new SecurityService();

        Credential cred = new Credential();
        cred.setId(1L);
        cred.setUsername("admin");
        cred.setPassword("1234");

        String encrypted = service.encryptPassword(cred.getPassword());
        System.out.println("Contraseña cifrada para '" + cred.getUsername() + "': " + encrypted);
    }
}