package com.edutech.enrollment_service.controller;

import com.edutech.enrollment_service.model.Enrollment;
import com.edutech.enrollment_service.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Operation(summary = "Obtener todas las inscripciones", description = "Devuelve una lista de todas las inscripciones registradas")
    @ApiResponse(responseCode = "200", description = "Inscripciones obtenidas correctamente",
            content = @Content(schema = @Schema(implementation = Enrollment.class)))
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAll() {
        List<Enrollment> lista = enrollmentService.obtenerEnrollments();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener inscripción por ID", description = "Devuelve una inscripción específica si existe")
    @ApiResponse(responseCode = "200", description = "Inscripción encontrada",
            content = @Content(schema = @Schema(implementation = Enrollment.class)))
    @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(enrollmentService.obtenerEnrollmentPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener inscripciones por usuario", description = "Lista las inscripciones asociadas a un usuario")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Enrollment>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.obtenerPorUsuario(userId));
    }

    @Operation(summary = "Obtener inscripciones por curso", description = "Lista los usuarios inscritos a un curso")
    @GetMapping("/curso/{courseId}")
    public ResponseEntity<List<Enrollment>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.obtenerPorCurso(courseId));
    }

    @Operation(summary = "Crear nueva inscripción", description = "Registra a un usuario en un curso si no está previamente inscrito")
    @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente",
            content = @Content(schema = @Schema(implementation = Enrollment.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o duplicados")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Enrollment enrollment) {
        try {
            Enrollment nueva = enrollmentService.crearEnrollment(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar inscripción", description = "Modifica progreso, estado o nota de una inscripción existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(required = false) Integer progreso,
                                    @RequestParam(required = false) Double notaFinal,
                                    @RequestParam(required = false) String estado,
                                    @RequestParam(required = false) Boolean certificadoEmitido) {
        try {
            return ResponseEntity.ok(enrollmentService.actualizarEnrollment(id, progreso, notaFinal, estado, certificadoEmitido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar inscripción", description = "Elimina una inscripción existente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(enrollmentService.eliminarEnrollment(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
