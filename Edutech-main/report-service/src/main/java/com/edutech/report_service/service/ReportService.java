package com.edutech.report_service.service;

import com.edutech.report_service.model.Report;
import com.edutech.report_service.repository.ReportRepository;
import com.edutech.report_service.webclient.AuthClient;
import com.edutech.report_service.webclient.EvaluationClient;
import com.edutech.report_service.webclient.EnrollmentClient;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private EvaluationClient evaluationClient;

    @Autowired
    private EnrollmentClient enrollmentClient;

    public Report crearReporte(Report reporte) {
        if (reporte.getUserId() == null) {
            throw new RuntimeException("El ID del usuario es obligatorio.");
        }

        if (!authClient.usuarioExiste(reporte.getUserId())) {
            throw new RuntimeException("El usuario no existe.");
        }

        if (reporte.getDescripcion() == null || reporte.getDescripcion().isBlank()) {
            throw new RuntimeException("La descripción no puede estar vacía.");
        }

        if (reporte.getTipo() == null || reporte.getTipo().isBlank()) {
            throw new RuntimeException("El tipo de reporte es obligatorio.");
        }

        return reportRepository.save(reporte);
    }

    public List<Report> obtenerTodos() {
        return reportRepository.findAll();
    }

    public Report obtenerPorId(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado. ID: " + id));
    }

    public List<Report> obtenerPorUsuario(Long usuarioId) {
        return reportRepository.findByUsuarioId(usuarioId);
    }

    public List<Report> obtenerPorTipo(String tipo) {
        return reportRepository.findByTipo(tipo.toUpperCase());
    }

    public String eliminar(Long id) {
        Report reporte = reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reportRepository.delete(reporte);
        return "Reporte eliminado correctamente";
    }

    // Generar reporte de progreso desde EnrollmentService
    public Report generarReporteProgreso(Long usuarioId) {
        if (!authClient.usuarioExiste(usuarioId)) {
            throw new RuntimeException("Usuario no válido para reporte de progreso.");
        }

        String descripcion = enrollmentClient.obtenerResumenProgreso(usuarioId);

        Report reporte = new Report();
        reporte.setUserId(usuarioId);
        reporte.setTipo("PROGRESO");
        reporte.setDescripcion(descripcion);

        return reportRepository.save(reporte);
    }

    // Generar reporte de desempeño desde EvaluationService
    public Report generarReporteDesempeno(Long usuarioId) {
        if (!authClient.usuarioExiste(usuarioId)) {
            throw new RuntimeException("Usuario no válido para reporte de desempeño.");
        }

        String descripcion = evaluationClient.obtenerResumenEvaluaciones(usuarioId);

        Report reporte = new Report();
        reporte.setUserId(usuarioId);
        reporte.setTipo("DESEMPEÑO");
        reporte.setDescripcion(descripcion);

        return reportRepository.save(reporte);
    }
}