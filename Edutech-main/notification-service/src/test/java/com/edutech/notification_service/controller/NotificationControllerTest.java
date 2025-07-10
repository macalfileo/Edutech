package com.edutech.notification_service.controller;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private NotificationService notificationService;

    private final String jsonBody = """
        {
            "titulo": "Aviso",
            "mensaje": "Tienes una nueva tarea.",
            "usuarioId": 15,
            "tipo": "EVALUACION"
        }
    """;

    @Test // Crear notificación
    void crearNotificacion_valido() throws Exception {
        Notification noti = new Notification();
        noti.setTitulo("Aviso");
        noti.setMensaje("Tienes una nueva tarea.");
        noti.setUsuarioId(15L);
        noti.setTipo("EVALUACION");

        when(notificationService.crear(any(), any(), any(), any(), any())).thenReturn(noti);

        mockMvc.perform(post("/api/v1/notificaciones")
                .header("Authorization", "token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.titulo").value("Aviso"));
    }

    @Test // Obtener por ID
    void obtenerPorId_existe() throws Exception {
        Notification n = new Notification();
        n.setId(1L); n.setTitulo("Hola"); n.setMensaje("Mensaje"); n.setUsuarioId(10L);
        when(notificationService.obtenerPorId(1L)).thenReturn(n);

        mockMvc.perform(get("/api/v1/notificaciones/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Hola"));
    }

    @Test // Obtener por usuario
    void obtenerPorUsuario_retornaLista() throws Exception {
        Notification n = new Notification();
        n.setUsuarioId(15L); n.setTitulo("Recordatorio");
        when(notificationService.obtenerPorUsuario(15L)).thenReturn(List.of(n));

        mockMvc.perform(get("/api/v1/notificaciones/usuario/15"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("Recordatorio"));
    }

    @Test // No leídas por usuario
    void obtenerNoLeidas_retornaLista() throws Exception {
        Notification n = new Notification();
        n.setUsuarioId(15L); n.setTitulo("Pendiente"); n.setLeida(false);
        when(notificationService.obtenerNoLeidas(15L)).thenReturn(List.of(n));

        mockMvc.perform(get("/api/v1/notificaciones/usuario/15/no-leidas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("Pendiente"));
    }

    @Test // Marcar como leída
    void marcarComoLeida_devuelveActualizada() throws Exception {
        Notification n = new Notification();
        n.setId(1L); n.setLeida(true); n.setTitulo("Aviso leído");
        when(notificationService.marcarComoLeida(1L)).thenReturn(n);

        mockMvc.perform(put("/api/v1/notificaciones/1/leida"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.leida").value(true));
    }

    @Test // Eliminar notificación
    void eliminarNotificacion_existente() throws Exception {
        when(notificationService.eliminar(1L)).thenReturn("Notificación eliminada");

        mockMvc.perform(delete("/api/v1/notificaciones/1"))
            .andExpect(status().isNoContent());
    }

    @Test // Eliminar notificación no existente
    void eliminarNotificacion_noExiste() throws Exception {
        doThrow(new RuntimeException("No encontrada")).when(notificationService).eliminar(99L);

        mockMvc.perform(delete("/api/v1/notificaciones/99"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("No encontrada"));
    }
}