package com.edutech.payment_service.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.repository.PaymentRepository;
import com.edutech.payment_service.service.PaymentService;
import com.edutech.payment_service.webclient.AuthClient;
import com.edutech.payment_service.webclient.EnrollmentClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private AuthClient authClient;
    @Mock private EnrollmentClient enrollmentClient;

    @InjectMocks private PaymentService paymentService;

    @Test
    void crearPago_valido() {
        Payment pago = new Payment();
        pago.setId(1L);
        pago.setUserId(1L);
        pago.setEnrollmentId(5L);
        pago.setUsuario("Juan Pérez");
        pago.setMonto(10000);
        pago.setMetodoPago("tarjeta");
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());
        when(authClient.usuarioExiste(1L, "token123")).thenReturn(true);
        when(enrollmentClient.inscripcionExiste(5L, "token123")).thenReturn(true);
        when(paymentRepository.save(any(Payment.class))).thenReturn(pago);

        Payment resultado = paymentService.crearPago(pago, "token123");
        assertEquals(pago.getMonto(), resultado.getMonto());
    }

    @Test
    void obtenerPagoPorId_existente() {
        Payment pago = new Payment();
        pago.setId(1L);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pago));

        Optional<Payment> resultado = paymentService.obtenerPagoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obtenerPagos_lista() {
        Payment pago = new Payment();
        pago.setId(1L);
        when(paymentRepository.findAll()).thenReturn(List.of(pago));

        List<Payment> lista = paymentService.obtenerPagos();

        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorUsuario_existente() {
        Payment pago = new Payment();
        pago.setUserId(1L);
        when(paymentRepository.findByUserId(1L)).thenReturn(List.of(pago));

        List<Payment> lista = paymentService.obtenerPorUsuario(1L);

        assertEquals(1, lista.size());
    }

    @Test
    void obtenerPorInscripcion_existente() {
        Payment pago = new Payment();
        pago.setEnrollmentId(5L);
        when(paymentRepository.findByEnrollmentId(5L)).thenReturn(List.of(pago));

        List<Payment> lista = paymentService.obtenerPorInscripcion(5L);

        assertEquals(1, lista.size());
    }

    @Test
    void actualizarPago_existente() {
        Payment existente = new Payment();
        existente.setId(1L);
        existente.setEstado("pendiente");

        Payment actualizacion = new Payment();
        actualizacion.setEstado("pagado");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(paymentRepository.save(any(Payment.class))).thenReturn(actualizacion);

        Payment pagoActualizado = new Payment();
        pagoActualizado.setEstado("pagado");

        Optional<Payment> result = paymentService.actualizarEstado(1L, pagoActualizado);

        assertTrue(result.isPresent());
        assertEquals("pagado", result.get().getEstado());
    }

    @Test
    void eliminarPago_existente() {
        when(paymentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> paymentService.eliminarPago(1L));

        verify(paymentRepository, times(1)).deleteById(1L); // ✅ Verifica deleteById
    }


    @Test
    void eliminarPago_noExiste() {
        when(paymentRepository.existsById(2L)).thenReturn(false);  // correcto

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            paymentService.eliminarPago(2L);
        });

        assertEquals("Pago no encontrado", ex.getMessage());
    }


    @Test
    void crearPago_usuarioNoExiste() {
        Payment pago = new Payment();
        pago.setUserId(99L);
        pago.setEnrollmentId(5L);

        when(authClient.usuarioExiste(eq(99L), eq("Bearer token"))).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            paymentService.crearPago(pago, "Bearer token");
        });

        assertEquals("El usuario no existe o no tiene permisos.", ex.getMessage());
    }
}
