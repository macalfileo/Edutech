package com.edutech.notification_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.notification_service.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsuarioId(Long usuarioId);

    List<Notification> findByUsuarioIdAndLeidaFalse(Long usuarioId);

    List<Notification> findByTipo(String tipo);
    
}
