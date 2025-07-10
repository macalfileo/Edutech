package com.edutech.report_service.controller;

import com.edutech.report_service.model.Report;
import com.edutech.report_service.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearReporte_status201() throws Exception {
        Report report = new Report();
        report.setUserId(1L);
        report.setDescripcion("test");
        report.setTipo("PROGRESO");

        when(reportService.crearReporte(any())).thenReturn(report);

        mockMvc.perform(post("/api/v1/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(report)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tipo").value("PROGRESO"));
    }

    @Test
    void obtenerPorId_status200() throws Exception {
        Report r = new Report();
        r.setId(1L);
        r.setTipo("DESEMPENO");

        when(reportService.obtenerPorId(1L)).thenReturn(r);

        mockMvc.perform(get("/api/v1/reportes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipo").value("DESEMPENO"));
    }

    @Test
    void obtenerPorId_noEncontrado() throws Exception {
        when(reportService.obtenerPorId(2L)).thenThrow(new RuntimeException("Reporte no encontrado"));

        mockMvc.perform(get("/api/v1/reportes/2"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Reporte no encontrado"));
    }

    @Test
    void listarTodos_status200() throws Exception {
        when(reportService.obtenerTodos()).thenReturn(List.of(new Report(), new Report()));

        mockMvc.perform(get("/api/v1/reportes"))
            .andExpect(status().isOk());
    }

    @Test
    void eliminarReporte_exitoso() throws Exception {
        when(reportService.eliminar(10L)).thenReturn("Reporte eliminado correctamente");

        mockMvc.perform(delete("/api/v1/reportes/10"))
            .andExpect(status().isOk())
            .andExpect(content().string("Reporte eliminado correctamente"));
    }

    @Test
    void generarReporteProgreso_status201() throws Exception {
        Report r = new Report();
        r.setTipo("PROGRESO");

        when(reportService.generarReporteProgreso(3L)).thenReturn(r);

        mockMvc.perform(post("/api/v1/reportes/generar/progreso/3"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tipo").value("PROGRESO"));
    }

    @Test
    void generarReporteDesempeno_status201() throws Exception {
        Report r = new Report();
        r.setTipo("DESEMPENO");

        when(reportService.generarReporteDesempeno(4L)).thenReturn(r);

        mockMvc.perform(post("/api/v1/reportes/generar/desempeno/4"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tipo").value("DESEMPENO"));
    }
}
