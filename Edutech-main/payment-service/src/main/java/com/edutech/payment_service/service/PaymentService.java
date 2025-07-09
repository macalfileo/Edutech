package com.edutech.payment_service.service;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.repository.PaymentRepository;
import com.edutech.payment_service.webclient.AuthClient;
import com.edutech.payment_service.webclient.EnrollmentClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private EnrollmentClient enrollmentClient;

    //Crear un nuevo pago
    public Payment crearPago(Payment pago, String authHeader) {
        // Validar existencia de usuario
        if (!authClient.usuarioExiste(pago.getUserId(), authHeader)) {
            throw new RuntimeException("El usuario no existe o no tiene permisos.");
        }

        // Validar existencia de inscripción
        if (!enrollmentClient.inscripcionExiste(pago.getEnrollmentId(), authHeader)) {
            throw new RuntimeException("La inscripción no existe.");
        }

        return paymentRepository.save(pago);
    }

    // Obtener todos los pagos
    public List<Payment> obtenerPagos() {
        return paymentRepository.findAll();
    }

    // Obtener pago por ID
    public Optional<Payment> obtenerPagoPorId(Long id) {
        return paymentRepository.findById(id);
    }

    // Obtener pagos por usuario
    public List<Payment> obtenerPorUsuario(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    // Obtener pagos por inscripción
    public List<Payment> obtenerPorInscripcion(Long enrollmentId) {
        return paymentRepository.findByEnrollmentId(enrollmentId);
    }

     //Actualizar pago existente
    public Optional<Payment> actualizarEstado(Long id, Payment pago) {
        return paymentRepository.findById(id).map(p -> {
            p.setEstado(pago.getEstado()); // Solo actualiza el estado
            return paymentRepository.save(p);
        });
    }
 
    //Eliminar pago por ID
    public void eliminarPago(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pago no encontrado");
        }
    }



}