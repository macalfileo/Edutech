package com.edutech.community_service;

import com.edutech.community_service.model.Post;
import com.edutech.community_service.service.CommunityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CommunityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityServiceApplication.class, args);

        CommunityService communityService = new CommunityService();

        Post post = new Post();
        post.setId(1L);
        post.setAuthor("Ana Torres");
        post.setMessage("¿Alguien resolvió la tarea 3?");

        communityService.addPost(post);
        communityService.getAllPosts().forEach(p -> {
            System.out.println(p.getAuthor() + ": " + p.getMessage());
        });
    }
}