package com.edutech.notification_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.springframework.http.MediaType;
// import org.glassfish.jaxb.runtime.v2.schemagen.xmlschema.List;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.edutech.notification_service.model.Notification;
import com.edutech.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEnviarNotificacion() throws Exception {
        Notification noti = new Notification(null,"user@example.com", "mensaje de prueba" , null, null);
        when(notificationService.enviarNotificacion(any(Notification.class))).thenReturn(noti);

        mockMvc.perform(post("/api/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noti)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receptor").value("user@example.com"));
    }

     @Test
    void testObtenerTodas() throws Exception {
        List<Notification> lista = Arrays.asList(
                new Notification(1L, "uno@correo.com", "msg1", null, null),
                new Notification(2L, "dos@correo.com", "msg2", null, null)
        );
        when(notificationService.obtenerTodas()).thenReturn(lista);

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerPorId() throws Exception {
        Notification noti = new Notification(1L, "user@example.com", "mensaje", null, null);
        when(notificationService.obtenerPorId(1L)).thenReturn(noti);

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("mensaje"));
    }
    @Test
    void testActualizar() throws Exception {
        Notification actualizada = new Notification(1L, "nuevo@correo.com", "nuevo mensaje", null, null);
        when(notificationService.actualizarNotificacion(Mockito.eq(1L), any(), any())).thenReturn(actualizada);

        mockMvc.perform(put("/api/notifications/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("nuevo mensaje"));
    }

    @Test
    void testEliminar() throws Exception {
        when(notificationService.eliminarNotificacion(1L)).thenReturn("Notificación eliminada correctamente");

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación eliminada correctamente"));
    }





    }







