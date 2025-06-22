package com.edutech.notification_service.service;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.repository.NotificationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service // componente d servicio de spring
@Transactional //para manejar operaciones atomicas 

public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification enviarNotificacion(Notification notification){
        if (notification.getMensaje()== null || notification.getMensaje().trim().isEmpty()){
            throw new RuntimeException("El mensaje de la notificación no puede estar vacío");     
        }
        return  notificationRepository.save(notification);

    }

    public List<Notification> obtenerTodas() {
        return notificationRepository.findAll();
    }

    public Notification obtenerPorId(Long id){
        return notificationRepository.findById(id)
                   .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

    }

    public String eliminarNotificacion(Long id){
        Notification notificacion = notificationRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
         notificationRepository.delete(notificacion);
         return "Notificación eliminada correctamente";
                             
    }

    public Notification actualizarNotificacion(Long id, String receptor, String mensaje ){
        Notification notificacion = notificationRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Notificación no entrada con ID: " + id));

        if (receptor != null && !receptor.trim().isEmpty()) {
            notificacion.setReceptor(receptor);
            
        }
        if (mensaje != null && !mensaje.trim().isEmpty()){
            notificacion.setMensaje(mensaje);
        }
        
        return notificationRepository.save(notificacion);
                  
    }



}