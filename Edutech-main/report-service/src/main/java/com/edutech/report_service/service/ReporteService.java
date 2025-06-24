package com.edutech.report_service.service;

import com.edutech.report_service.dto.ReporteDTO;
import com.edutech.report_service.model.Reporte;
import com.edutech.report_service.repository.ReporteRepository;
import com.edutech.report_service.util.GeneradorPDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repo;

    public ReporteDTO generarReporte(ReporteDTO dto) {
        Reporte r = new Reporte();
        r.setTipo((String) dto.getTipo());
        r.setContenido((String) dto.getContenido());
        r.setFechaGeneracion(LocalDate.now());
        repo.save(r);
        return dto;
    }

    public List<ReporteDTO> listarReportes() {
        return repo.findAll().stream()
            .map(r -> {
                ReporteDTO dto = new ReporteDTO();
                dto.setTipo(r.getTipo());
                dto.setContenido(r.getContenido());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public byte[] generarPDF(Long id) {
        Reporte reporte = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return GeneradorPDF.generarPDF(reporte);
    }
}