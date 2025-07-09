package com.edutech.evaluation_service.Controller;

import com.edutech.evaluation_service.controller.EvaluationController;
import com.edutech.evaluation_service.model.Evaluation;
import com.edutech.evaluation_service.service.EvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EvaluationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluationService evaluationService;

    @Test
    void listarEvaluaciones_returnOK() throws Exception {
        Evaluation e = new Evaluation();
        e.setId(1L);
        e.setTitulo("Evaluación 1");

        when(evaluationService.listarEvaluaciones()).thenReturn(List.of(e));

        mockMvc.perform(get("/api/evaluaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Evaluación 1"));
    }

    @Test
    void obtenerEvaluacionPorId_returnOK() throws Exception {
        Evaluation e = new Evaluation();
        e.setId(1L);
        e.setTitulo("Evaluación 1");

        when(evaluationService.obtenerEvaluacionPorId(1L)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/api/evaluaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Evaluación 1"));
    }

    @Test
    void crearEvaluacion_returnCreated() throws Exception {
        Evaluation e = new Evaluation();
        e.setId(1L);
        e.setTitulo("Nueva Eval");

        when(evaluationService.crearEvaluacion(any(Evaluation.class), anyString())).thenReturn(e);

        String json = """
            {
              "titulo": "Nueva Eval",
              "descripcion": "Desc",
              "courseId": 1
            }
        """;

        mockMvc.perform(post("/api/evaluaciones")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Nueva Eval"));
    }

    @Test
    void actualizarEvaluacion_returnOK() throws Exception {
        Evaluation e = new Evaluation();
        e.setId(1L);
        e.setTitulo("Actualizado");

        when(evaluationService.actualizarEvaluacion(eq(1L), any(Evaluation.class), anyString())).thenReturn(Optional.of(e));

        String json = """
            {
              "titulo": "Actualizado",
              "descripcion": "Nueva descripción",
              "courseId": 1
            }
        """;

        mockMvc.perform(put("/api/evaluaciones/1")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Actualizado"));
    }

    @Test
    void eliminarEvaluacion_returnNoContent() throws Exception {
        when(evaluationService.eliminarEvaluacion(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/evaluaciones/1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerEvaluacionesPorCurso_returnOK() throws Exception {
        Evaluation e = new Evaluation();
        e.setId(1L);
        e.setTitulo("Curso Eval");

        when(evaluationService.listarEvaluacionesPorCurso(10L)).thenReturn(List.of(e));

        mockMvc.perform(get("/api/evaluaciones/curso/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Curso Eval"));
    }

    @Test
    void eliminarEvaluacionesPorCurso_returnOK() throws Exception {
        mockMvc.perform(delete("/api/evaluaciones/curso/10")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Evaluaciones eliminadas para el curso 10"));
    }
}
