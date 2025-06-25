package com.edutech.payment_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/api/pagos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

     //Crear pago
    @Operation(summary = "Crear un nuevo pago", description = "Genera un nuevo pago con la información proporcionada.")
    @ApiResponse(responseCode = "200", description = "Pago creado exitosamente", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping
    public ResponseEntity<Payment> crear(@RequestBody Payment pago) {
        Payment nuevo = paymentService.crearPago(pago);
        return ResponseEntity.ok(nuevo);
    }

    //Obtener pago por ID
    @Operation(summary = "Obtener un pago por ID", description = "Devuelve el pago correspondiente al ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Pago encontrado", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Payment> obtenerPorId(@PathVariable Long id) {
        return paymentService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Listar todos los pagos
    @Operation(summary = "Listar todos los pagos", description = "Devuelve una lista de todos los pagos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Payment.class))))
    @GetMapping
    public List<Payment> listar() {
        return paymentService.listarPagos();
    }

    //Actualizar pago
    @Operation(summary = "Actualizar pago", description = "Actualiza la información del pago correspondiente al ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente", content = @Content(schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Payment> actualizar(@PathVariable Long id, @RequestBody Payment pago) {
        return paymentService.actualizarPago(id, pago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    //Eliminar pago
    @Operation(summary = "Eliminar un pago", description = "Elimina un pago según el ID proporcionado.")
    @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (paymentService.eliminarPago(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }








    

}
