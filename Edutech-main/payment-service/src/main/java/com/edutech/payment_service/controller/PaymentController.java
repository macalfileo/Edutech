package com.edutech.payment_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

     //Crear pago
    @Operation(summary = "Crear un nuevo pago", description = "Registra un nuevo pago si la inscripción y el usuario son válidos.")
    @ApiResponse(responseCode = "201", description = "Pago creado exitosamente", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "400", description = "Error de validación o entidades no encontradas", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<?> crearPago(@RequestBody Payment pago, @RequestHeader("Authorization") String token) {
        try {
            Payment nuevo = paymentService.crearPago(pago, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Obtener pago por ID
    @Operation(summary = "Obtener un pago por ID", description = "Devuelve el pago correspondiente al ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Pago encontrado", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> obtenerPorId(@PathVariable Long id) {
        return paymentService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Obtener todos los pagos
    @Operation(summary = "Obtener todos los pagos", description = "Retorna una lista de todos los pagos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos", content = @Content(schema = @Schema(implementation = Payment.class)))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Payment>> listarPagos() {
        return ResponseEntity.ok(paymentService.obtenerPagos());
    }

    //Obtener pagos por usuario
    @Operation(summary = "Obtener pagos por usuario", description = "Devuelve todos los pagos asociados a un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Pagos encontrados", content = @Content(schema = @Schema(implementation = Payment.class)))
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Payment>> pagosPorUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.obtenerPorUsuario(userId));
    }

    //Obtener pagos por inscripción
    @Operation(summary = "Obtener pagos por inscripción", description = "Devuelve los pagos asociados a una inscripción específica.")
    @ApiResponse(responseCode = "200", description = "Pagos encontrados", content = @Content(schema = @Schema(implementation = Payment.class)))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_CURSOS')")
    @GetMapping("/inscripcion/{enrollmentId}")
    public ResponseEntity<List<Payment>> pagosPorInscripcion(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(paymentService.obtenerPorInscripcion(enrollmentId));
    }

    //Actualizar pago
    @Operation(summary = "Actualizar pago", description = "Actualiza la información del pago correspondiente al ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Payment> actualizar(@PathVariable Long id, @RequestBody Payment pago) {
        return paymentService.actualizarEstado(id, pago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Eliminar pago
    @Operation(summary = "Eliminar un pago", description = "Elimina un pago existente por ID.")
    @ApiResponse(responseCode = "200", description = "Pago eliminado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        try {
            paymentService.eliminarPago(id);
            return ResponseEntity.ok("Pago eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }








    

}
