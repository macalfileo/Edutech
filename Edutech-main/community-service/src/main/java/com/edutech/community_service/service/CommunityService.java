package com.edutech.community_service.service;

import com.edutech.community_service.model.Post;
import java.util.ArrayList;
import java.util.List;

public class CommunityService {
    private final List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getAllPosts() {
        return posts;
    }
}