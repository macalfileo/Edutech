
package com.edutech.course_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.service.ContenidoService;
import com.edutech.course_service.service.CourseService;
import com.edutech.course_service.service.ModuloService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseControllerTest {

    @MockBean private CourseService courseService;
    @MockBean private ModuloService moduloService;
    @MockBean private ContenidoService contenidoService;

    @Autowired private MockMvc mockMvc;

    @Test // Obtener todos los cursos
    void getCursos_returnOK() throws Exception {
        Course c = new Course(); c.setId(1L); c.setTitulo("Java");
        when(courseService.obtenerCourses()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/v1/courses/cursos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("Java"));
    }

    @Test // Obtener contenidos por módulo
    void getContenidosPorModulo_returnOK() throws Exception {
        Contenido cont = new Contenido(); cont.setId(1L); cont.setTitulo("Intro");
        when(contenidoService.obtenerContenidosPorModulo(1L)).thenReturn(List.of(cont));

        mockMvc.perform(get("/api/v1/courses/modulos/1/contenidos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("Intro"));
    }

    @Test // Crear módulo
    void crearModulo_returnCreated() throws Exception {
        Modulo m = new Modulo();
        m.setId(1L);
        m.setTitulo("Módulo 1");

        when(moduloService.crearModulo(anyString(), anyString(), anyString(), anyInt(), anyLong()))
            .thenReturn(m);

        String body = """
            {
                "titulo": "Módulo 1",
                "descripcion": "Desc",
                "orden": 1,
                "curso": { "id": 1 }
            }
            """;

        mockMvc.perform(post("/api/v1/courses/modulos")
                .header("Authorization", "Bearer fakeToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.titulo").value("Módulo 1"));
    }


    @Test // Crear curso inválido
    void crearCurso_datosInvalidos_return403() throws Exception {
        when(courseService.crearCourse(anyString(), any(), any(), anyLong(), anyInt(), any()))
            .thenThrow(new RuntimeException("Instructor no encontrado"));

        String body = """
            {
                "titulo": "Curso X",
                "descripcion": "Descripción",
                "instructorId": 99,
                "duracionMinutos": 60,
                "categoria": "Programación"
            }
            """;

        mockMvc.perform(post("/api/v1/courses/cursos")
                .header("Authorization", "Bearer fakeToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").value("Instructor no encontrado"));
    }


    @Test // Eliminar contenido con token
    void eliminarContenido_returnOK() throws Exception {
        when(contenidoService.eliminarContenido(eq("Bearer token123"), eq(1L)))
            .thenReturn("Contenido eliminado");

        mockMvc.perform(delete("/api/v1/courses/contenidos/1")
            .header("Authorization", "Bearer token123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Contenido eliminado"));
    }

}
