package com.edutech.notification_service.service;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.repository.NotificationRepository;
import com.edutech.notification_service.webclient.AuthClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void crearNotificacion_valida() {
        Notification n = new Notification();
        n.setTitulo("Nuevo curso");
        n.setMensaje("Se ha publicado un nuevo curso");
        n.setUserId(10L);
        n.setTipo("CURSO");

        when(authClient.usuarioExiste("token123", 10L)).thenReturn(true);
        when(notificationRepository.save(any(Notification.class))).thenReturn(n);

        Notification result = notificationService.crear("token123", "Nuevo curso", "Se ha publicado un nuevo curso", 10L, "CURSO");

        assertEquals("Nuevo curso", result.getTitulo());
        assertEquals("CURSO", result.getTipo());
    }

    @Test
    void crearNotificacion_usuarioInvalido() {
        when(authClient.usuarioExiste("token123", 88L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            notificationService.crear("token123", "Alerta", "Mensaje", 88L, "SISTEMA");
        });

        assertEquals("Usuario destino no válido: 88", ex.getMessage());
    }

    @Test
    void obtenerPorId_existe() {
        Notification n = new Notification();
        n.setId(1L);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(n));

        Notification result = notificationService.obtenerPorId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_noExiste() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            notificationService.obtenerPorId(99L);
        });

        assertEquals("Notificación no encontrada con ID: 99", ex.getMessage());
    }

    @Test
    void marcarComoLeida_actualizaEstado() {
        Notification n = new Notification();
        n.setId(1L);
        n.setLeida(false);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(n));
        when(notificationRepository.save(any(Notification.class))).thenReturn(n);

        Notification result = notificationService.marcarComoLeida(1L);

        assertTrue(result.isLeida());
    }

    @Test
    void eliminar_existente() {
        Notification n = new Notification();
        n.setId(1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(n));

        String result = notificationService.eliminar(1L);

        verify(notificationRepository).delete(n);
        assertEquals("Notificación eliminada", result);
    }

    @Test
    void obtenerPorUsuario_devuelveLista() {
        Notification n1 = new Notification();
        Notification n2 = new Notification();
        when(notificationRepository.findByUsuarioId(5L)).thenReturn(List.of(n1, n2));

        List<Notification> result = notificationService.obtenerPorUsuario(5L);
        assertEquals(2, result.size());
    }

    @Test
    void obtenerNoLeidas_devuelveLista() {
        when(notificationRepository.findByUsuarioIdAndLeidaFalse(3L)).thenReturn(List.of(new Notification()));
        List<Notification> result = notificationService.obtenerNoLeidas(3L);
        assertEquals(1, result.size());
    }

    @Test
    void obtenerTodas_devuelveListaCompleta() {
        when(notificationRepository.findAll()).thenReturn(List.of(new Notification(), new Notification()));
        List<Notification> result = notificationService.obtenerTodas();
        assertEquals(2, result.size());
    }
}