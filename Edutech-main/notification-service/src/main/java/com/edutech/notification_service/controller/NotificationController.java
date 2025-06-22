package com.edutech.notification_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/send")
    public ResponseEntity<Notification> enviarNotificacion(@RequestBody Notification notification) {
        Notification nueva = notificationService.enviarNotificacion(notification);
        return ResponseEntity.ok(nueva);

    }
    @GetMapping
    public ResponseEntity<List<Notification>> obtenerTodas(){
        return ResponseEntity.ok(notificationService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification>obtenerPorid(@PathVariable Long id){
        return ResponseEntity.ok(notificationService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> actualizar(@PathVariable Long id, @RequestBody Notification actualizada){
   
        Notification notification = notificationService.actualizarNotificacion(
            id, 
            actualizada.getReceptor(),
            actualizada.getMensaje()
        );
        return ResponseEntity.ok(notification);
    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable long id){
        String resultado = notificationService.eliminarNotificacion(id);
        return ResponseEntity.ok(resultado);
    }
    


}
