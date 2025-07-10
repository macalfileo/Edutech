package com.edutech.report_service.service;

import com.edutech.report_service.model.Report;
import com.edutech.report_service.repository.ReportRepository;
import com.edutech.report_service.webclient.AuthClient;
import com.edutech.report_service.webclient.EnrollmentClient;
import com.edutech.report_service.webclient.EvaluationClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock private ReportRepository reportRepository;
    @Mock private AuthClient authClient;
    @Mock private EnrollmentClient enrollmentClient;
    @Mock private EvaluationClient evaluationClient;

    @InjectMocks private ReportService reportService;

    @Test
    void crearReporte_valido() {
        Report r = new Report();
        r.setUserId(1L);
        r.setDescripcion("Descripción");
        r.setTipo("PROGRESO");

        when(authClient.usuarioExiste(1L)).thenReturn(true);
        when(reportRepository.save(any(Report.class))).thenReturn(r);

        Report result = reportService.crearReporte(r);

        assertEquals("PROGRESO", result.getTipo());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void crearReporte_usuarioNoExiste() {
        Report r = new Report();
        r.setUserId(999L);

        when(authClient.usuarioExiste(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reportService.crearReporte(r);
        });

        assertEquals("El usuario no existe.", ex.getMessage());
    }

    @Test
    void obtenerPorId_existe() {
        Report r = new Report();
        r.setId(1L);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(r));

        Report result = reportService.obtenerPorId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_noExiste() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reportService.obtenerPorId(1L);
        });

        assertEquals("Reporte no encontrado. ID: 1", ex.getMessage());
    }

    @Test
    void generarReporteProgreso_valido() {
        when(authClient.usuarioExiste(10L)).thenReturn(true);
        when(enrollmentClient.obtenerResumenProgreso(10L)).thenReturn("Progreso OK");
        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Report r = reportService.generarReporteProgreso(10L);

        assertEquals("PROGRESO", r.getTipo());
        assertTrue(r.getDescripcion().contains("Progreso OK"));
    }

    @Test
    void generarReporteDesempeno_valido() {
        when(authClient.usuarioExiste(7L)).thenReturn(true);
        when(evaluationClient.obtenerResumenEvaluaciones(7L)).thenReturn("Desempeño OK");
        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Report r = reportService.generarReporteDesempeno(7L);

        assertEquals("DESEMPEÑO", r.getTipo());
        assertTrue(r.getDescripcion().contains("Desempeño OK"));
    }

    @Test
    void eliminar_existe() {
        Report r = new Report();
        r.setId(5L);
        when(reportRepository.findById(5L)).thenReturn(Optional.of(r));

        String mensaje = reportService.eliminar(5L);
        assertEquals("Reporte eliminado correctamente", mensaje);
        verify(reportRepository).delete(r);
    }

    @Test
    void eliminar_noExiste() {
        when(reportRepository.findById(9L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reportService.eliminar(9L);
        });

        assertEquals("Reporte no encontrado", ex.getMessage());
    }

    @Test
    void obtenerTodos_returnLista() {
        when(reportRepository.findAll()).thenReturn(List.of(new Report(), new Report()));
        List<Report> lista = reportService.obtenerTodos();
        assertEquals(2, lista.size());
    }

    @Test
    void obtenerPorUsuario_ok() {
        when(reportRepository.findByUsuarioId(3L)).thenReturn(List.of(new Report(), new Report()));
        List<Report> lista = reportService.obtenerPorUsuario(3L);
        assertEquals(2, lista.size());
    }

    @Test
    void obtenerPorTipo_ok() {
        when(reportRepository.findByTipo("PROGRESO")).thenReturn(List.of(new Report()));
        List<Report> lista = reportService.obtenerPorTipo("PROGRESO");
        assertEquals(1, lista.size());
    }
}