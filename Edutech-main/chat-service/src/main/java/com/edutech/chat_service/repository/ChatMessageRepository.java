package com.edutech.chat_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.chat_service.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByCourseId(Long courseId);

    List<ChatMessage> findByUserId(Long userId);

    List<ChatMessage> findByCursoIdOrderByFechaEnvioAsc(Long cursoId);
}