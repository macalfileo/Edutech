package com.edutech.notification_service.service;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.repository.NotificationRepository;
import com.edutech.notification_service.webclient.AuthClient;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthClient authClient;

    public List<Notification> obtenerTodas() {
        return notificationRepository.findAll();
    }

    public Notification obtenerPorId(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
    }

    public List<Notification> obtenerPorUsuario(Long usuarioId) {
        return notificationRepository.findByUsuarioId(usuarioId);
    }

    public List<Notification> obtenerNoLeidas(Long usuarioId) {
        return notificationRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
    }

    public Notification crear(String authHeader, String titulo, String mensaje, Long usuarioId, String tipo) {
        if (titulo == null || mensaje == null || tipo == null || usuarioId == null) {
            throw new RuntimeException("Todos los campos son obligatorios");
        }

        // Validar que el usuario existe (o tiene permisos válidos)
        if (!authClient.usuarioExiste(authHeader, usuarioId)) {
            throw new RuntimeException("Usuario destino no válido: " + usuarioId);
        }

        Notification noti = new Notification();
        noti.setTitulo(titulo);
        noti.setMensaje(mensaje);
        noti.setUserId(usuarioId);;
        noti.setTipo(tipo);

        return notificationRepository.save(noti);
    }

    public Notification marcarComoLeida(Long id) {
        Notification noti = obtenerPorId(id);
        noti.setLeida(true);
        return notificationRepository.save(noti);
    }

    public String eliminar(Long id) {
        Notification noti = obtenerPorId(id);
        notificationRepository.delete(noti);
        return "Notificación eliminada";
    }
}