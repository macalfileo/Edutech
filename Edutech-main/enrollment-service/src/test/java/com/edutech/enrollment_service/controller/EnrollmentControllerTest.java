
package com.edutech.enrollment_service.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.service.EnrollmentService;

@WebMvcTest(controllers = EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EnrollmentControllerTest {

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerInscripciones_returnOKAndJson() throws Exception {
        Enrollment e1 = new Enrollment(1L, 100L, 200L, "2024-05-10", "activo");
        Enrollment e2 = new Enrollment(2L, 101L, 200L, "2024-05-12", "activo");

        List<Enrollment> lista = Arrays.asList(e1, e2);
        when(enrollmentService.obtenerEnrollments()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/enrollments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userId").value(100L))
            .andExpect(jsonPath("$[1].courseId").value(200L));
    }

    @Test
    void crearInscripcion_returnCreatedAndJson() throws Exception {
        Enrollment entrada = new Enrollment(null, 100L, 200L, null, null);
        Enrollment salida = new Enrollment(1L, 100L, 200L, "2024-05-10", "activo");

        when(enrollmentService.crearEnrollment(any(Enrollment.class))).thenReturn(salida);

        mockMvc.perform(post("/api/v1/enrollments")
                .contentType("application/json")
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(entrada)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.estado").value("activo"));
    }

    @Test
    void crearInscripcion_duplicada_returnBadRequest() throws Exception {
        Enrollment entrada = new Enrollment(null, 100L, 200L, null, null);

        when(enrollmentService.crearEnrollment(any(Enrollment.class)))
            .thenThrow(new RuntimeException("El usuario ya está inscrito en este curso"));

        mockMvc.perform(post("/api/v1/enrollments")
                .contentType("application/json")
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(entrada)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").value("El usuario ya está inscrito en este curso"));
    }
}
