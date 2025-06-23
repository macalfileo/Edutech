package com.edutech.payment_service.service;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }
    //Crear un nuevo pago
    public Payment CrearPago(Payment pago){
        return paymentRepository.save(pago);
    }
    // Obtener pago por ID
    public Optional<Payment> obtenerPagoPorId(Long id) {
        return paymentRepository.findById(id);
    }
    
    //Listar todos los pagos
    public List<Payment> listarPagos() {
        return paymentRepository.findAll();
    }

     //Actualizar pago existente
    public Optional<Payment> actualizarPago(Long id, Payment nuevoPago) {
        return paymentRepository.findById(id).map(pagoExistente -> {
            pagoExistente.setUsuario(nuevoPago.getUsuario());
            pagoExistente.setMonto(nuevoPago.getMonto());
            pagoExistente.setMetodoPago(nuevoPago.getMetodoPago());
            pagoExistente.setEstado(nuevoPago.getEstado());
            pagoExistente.setFechaPago(nuevoPago.getFechaPago());
            return paymentRepository.save(pagoExistente);
        });
    }

    
    //Eliminar pago por ID
    public boolean eliminarPago(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Object getAllPayments() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPayments'");
    }
    public void processPayment(Payment payment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }
    public Payment crearPago(Payment pago) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearPago'");
    }


}