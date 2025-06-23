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

import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/api/pagos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

     //Crear pago
    @PostMapping
    public ResponseEntity<Payment> crear(@RequestBody Payment pago) {
        Payment nuevo = paymentService.crearPago(pago);
        return ResponseEntity.ok(nuevo);
    }

    //Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> obtenerPorId(@PathVariable Long id) {
        return paymentService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Listar todos los pagos
    @GetMapping
    public List<Payment> listar() {
        return paymentService.listarPagos();
    }

        //Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<Payment> actualizar(@PathVariable Long id, @RequestBody Payment pago) {
        return paymentService.actualizarPago(id, pago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    //Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (paymentService.eliminarPago(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }








    

}
