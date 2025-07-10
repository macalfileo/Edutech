package com.edutech.report_service.controller;

import com.edutech.report_service.model.Report;
import com.edutech.report_service.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Crear un reporte manual", description = "Crea un reporte indicando usuario, tipo y descripción.")
    @ApiResponse(responseCode = "201", description = "Reporte creado", content = @Content(schema = @Schema(implementation = Report.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Report reporte) {
        try {
            Report nuevo = reportService.crearReporte(reporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener todos los reportes", description = "Devuelve todos los reportes del sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de reportes", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Report.class))))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Report>> listar() {
        return ResponseEntity.ok(reportService.obtenerTodos());
    }

    @Operation(summary = "Obtener reporte por ID", description = "Devuelve el reporte con el ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(schema = @Schema(implementation = Report.class)))
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener reportes por usuario", description = "Devuelve todos los reportes generados para un usuario.")
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Report.class))))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Report>> porUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reportService.obtenerPorUsuario(usuarioId));
    }

    @Operation(summary = "Obtener reportes por tipo", description = "Filtra los reportes según su tipo (ej: PROGRESO, DESEMPEÑO).")
    @ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Report.class))))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Report>> porTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(reportService.obtenerPorTipo(tipo));
    }

    @Operation(summary = "Generar reporte de progreso", description = "Crea automáticamente un reporte basado en inscripciones del usuario.")
    @ApiResponse(responseCode = "201", description = "Reporte de progreso generado", content = @Content(schema = @Schema(implementation = Report.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @PostMapping("/generar/progreso/{usuarioId}")
    public ResponseEntity<?> generarProgreso(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reportService.generarReporteProgreso(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Generar reporte de desempeño", description = "Crea un reporte automático del rendimiento del usuario.")
    @ApiResponse(responseCode = "201", description = "Reporte de desempeño generado", content = @Content(schema = @Schema(implementation = Report.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @PostMapping("/generar/desempeno/{usuarioId}")
    public ResponseEntity<?> generarDesempeno(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reportService.generarReporteDesempeno(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte por ID.")
    @ApiResponse(responseCode = "200", description = "Reporte eliminado")
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportService.eliminar(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}