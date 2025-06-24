package com.edutech.notification_service.service;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarNotificacion_Valida() {
        Notification notification = new Notification(null, "usuario@example.com", "Mensaje de prueba", null, null);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.enviarNotificacion(notification);

        assertNotNull(result);
        assertEquals("Mensaje de prueba", result.getMensaje());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testEnviarNotificacion_MensajeVacio() {
        Notification notification = new Notification(null, "usuario@example.com", "   ", null, null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.enviarNotificacion(notification);
        });
        assertEquals("El mensaje de la notificación no puede estar vacío", exception.getMessage());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testObtenerTodas() {
        List<Notification> lista = Arrays.asList(
                new Notification(1L, "user1@example.com", "msg1", null, null),
                new Notification(2L, "user2@example.com", "msg2", null, null)
        );
        when(notificationRepository.findAll()).thenReturn(lista);

        List<Notification> resultado = notificationService.obtenerTodas();

        assertEquals(2, resultado.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Existente() {
        Notification noti = new Notification(1L, "user@example.com", "mensaje", null, null);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(noti));

        Notification resultado = notificationService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("mensaje", resultado.getMensaje());
    }

    @Test
    void testObtenerPorId_Inexistente() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            notificationService.obtenerPorId(99L);
        });
        assertTrue(ex.getMessage().contains("Notificación no encontrada"));
    }

    @Test
    void testEliminarNotificacion() {
        Notification noti = new Notification(1L, "user@example.com", "mensaje", null, null);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(noti));

        String mensaje = notificationService.eliminarNotificacion(1L);

        assertEquals("Notificación eliminada correctamente", mensaje);
        verify(notificationRepository).delete(noti);
    }

    @Test
    void testEliminarNotificacion_Inexistente() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            notificationService.eliminarNotificacion(99L);
        });
        assertTrue(ex.getMessage().contains("Notificación no encontrada"));
    }

    @Test
    void testActualizarNotificacion() {
        Notification noti = new Notification(1L, "user@example.com", "mensaje viejo", null, null);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(noti));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification actualizada = notificationService.actualizarNotificacion(1L, "nuevo@receptor.com", "mensaje nuevo");

        assertEquals("nuevo@receptor.com", actualizada.getReceptor());
        assertEquals("mensaje nuevo", actualizada.getMensaje());
    }

    @Test
    void testActualizarNotificacion_SoloMensaje() {
        Notification noti = new Notification(1L, "user@example.com", "mensaje viejo", null, null);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(noti));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification actualizada = notificationService.actualizarNotificacion(1L, null, "nuevo mensaje");

        assertEquals("user@example.com", actualizada.getReceptor());
        assertEquals("nuevo mensaje", actualizada.getMensaje());
    }

    @Test
    void testActualizarNotificacion_SoloReceptor() {
        Notification noti = new Notification(1L, "user@example.com", "mensaje viejo", null, null);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(noti));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification actualizada = notificationService.actualizarNotificacion(1L, "nuevo@correo.com", null);

        assertEquals("nuevo@correo.com", actualizada.getReceptor());
        assertEquals("mensaje viejo", actualizada.getMensaje());
    }
}