package com.edutech.report_service.controller;

import com.edutech.report_service.dto.ReporteDTO;
import com.edutech.report_service.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReportService service;

    @Operation(summary = "Crear un nuevo reporte", description = "Genera un reporte en base a los datos proporcionados.")
    @ApiResponse(
        responseCode = "200",
        description = "Reporte creado exitosamente",
        content = @Content(schema = @Schema(implementation = ReporteDTO.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(service.generarReporte(dto));
    }

    @Operation(summary = "Obtener todos los reportes", description = "Devuelve una lista de todos los reportes generados.")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de reportes obtenida exitosamente",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReporteDTO.class)))
    )
    @ApiResponse(
        responseCode = "404",
        description = "No se encontraron reportes",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping
    public List<ReporteDTO> listar() {
        return service.listarReportes();
    }

    @Operation(summary = "Exportar reporte a PDF", description = "Genera un archivo PDF del reporte solicitado.")
    @ApiResponse(
        responseCode = "200",
        description = "Reporte PDF generado exitosamente",
        content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
    )
    @ApiResponse(
        responseCode = "404",
        description = "Reporte no encontrado",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportar(@PathVariable Long id) {
        byte[] data = service.generarPDF(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_" + id + ".pdf");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}