package com.edutech.community_service.repository;

import com.edutech.community_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCourseIdOrderByFechaCreacionDesc(Long courseId);

    List<Post> findByUserIdOrderByFechaCreacionDesc(Long userId);
}