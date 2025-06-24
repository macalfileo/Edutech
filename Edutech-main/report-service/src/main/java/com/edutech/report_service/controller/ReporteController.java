package com.edutech.report_service.controller;

import com.edutech.report_service.dto.ReporteDTO;
import com.edutech.report_service.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(service.generarReporte(dto));
    }

    @GetMapping
    public List<ReporteDTO> listar() {
        return service.listarReportes();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportar(@PathVariable Long id) {
        byte[] data = service.generarPDF(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_" + id + ".pdf");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}