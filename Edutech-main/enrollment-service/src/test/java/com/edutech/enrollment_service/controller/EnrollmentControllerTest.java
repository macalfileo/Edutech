
package com.edutech.enrollment_service.controller;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EnrollmentControllerTest {

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerInscripciones_returnOKAndJson() throws Exception {
        Enrollment e1 = new Enrollment(1L, 10L, 100L, LocalDateTime.now(), "ACTIVO", 90, 6.5, true);
        Enrollment e2 = new Enrollment(2L, 11L, 101L, LocalDateTime.now(), "COMPLETADO", 100, 7.0, true);

        List<Enrollment> lista = Arrays.asList(e1, e2);
        when(enrollmentService.obtenerEnrollments()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/enrollments"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].userId").value(10L))
               .andExpect(jsonPath("$[1].estado").value("COMPLETADO"));
    }

    @Test
    void crearInscripcion_returnCreated() throws Exception {
        Enrollment entrada = new Enrollment(null, 12L, 102L, null, "ACTIVO", 0, null, false);
        Enrollment salida = new Enrollment(3L, 12L, 102L, LocalDateTime.now(), "ACTIVO", 0, null, false);

        String authHeader = "Bearer fake-jwt-token";

        when(enrollmentService.crearEnrollment(any(Enrollment.class), eq(authHeader))).thenReturn(salida);

        mockMvc.perform(post("/api/v1/enrollments")
               .header("Authorization", authHeader) // <- Simula el header
               .contentType(MediaType.APPLICATION_JSON)
               .content(new ObjectMapper().writeValueAsString(entrada)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void obtenerInscripcionPorId_returnOK() throws Exception {
        Enrollment inscripcion = new Enrollment(4L, 13L, 103L, LocalDateTime.now(), "ACTIVO", 20, null, false);
        when(enrollmentService.obtenerEnrollmentPorId(4L)).thenReturn(inscripcion);

        mockMvc.perform(get("/api/v1/enrollments/4"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userId").value(13L));
    }

    @Test
    void eliminarInscripcion_returnOKMessage() throws Exception {
        when(enrollmentService.eliminarEnrollment(5L)).thenReturn("Inscripción eliminada");

        mockMvc.perform(delete("/api/v1/enrollments/5"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").value("Inscripción eliminada"));
    }

    @Test
    void actualizarInscripcion_returnOKAndUpdated() throws Exception {
        Enrollment salida = new Enrollment(6L, 14L, 104L, LocalDateTime.now(), "COMPLETADO", 100, 7.0, true);

        when(enrollmentService.actualizarEnrollment(6L, 100, 7.0, "COMPLETADO", true)).thenReturn(salida);

        mockMvc.perform(put("/api/v1/enrollments/6")
               .param("estado", "COMPLETADO")
               .param("progreso", "100")
               .param("notaFinal", "7.0")
               .param("certificadoEmitido", "true"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.estado").value("COMPLETADO"))
               .andExpect(jsonPath("$.notaFinal").value(7.0));
    }

    @Test
    void obtenerPorUsuario_returnOK() throws Exception {
        Enrollment e1 = new Enrollment(7L, 15L, 200L, LocalDateTime.now(), "ACTIVO", 30, null, false);
        when(enrollmentService.obtenerPorUsuario(15L)).thenReturn(List.of(e1));

        mockMvc.perform(get("/api/v1/enrollments/usuario/15"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].userId").value(15L));
    }

    @Test
    void obtenerPorCurso_returnOK() throws Exception {
        Enrollment e1 = new Enrollment(8L, 20L, 300L, LocalDateTime.now(), "ACTIVO", 50, 5.0, false);
        when(enrollmentService.obtenerPorCurso(300L)).thenReturn(List.of(e1));

        mockMvc.perform(get("/api/v1/enrollments/curso/300"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].courseId").value(300L));
    }
}
