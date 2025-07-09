package com.edutech.payment_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edutech.payment_service.controller.PaymentController;
import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void crearPago_returnCreated() throws Exception {
        Payment pago = new Payment();
        pago.setId(1L);
        pago.setUserId(1L);
        pago.setEnrollmentId(5L);
        pago.setUsuario("Juan Pérez");
        pago.setMonto(10000);
        pago.setMetodoPago("tarjeta");
        pago.setEstado("pendiente");
        pago.setFechaPago(LocalDateTime.now());

        when(paymentService.crearPago(any(Payment.class), eq("Bearer token123"))).thenReturn(pago);

        String body = """
        {
            "userId": 1,
            "enrollmentId": 5,
            "usuario": "Juan Pérez",
            "monto": 10000,
            "metodoPago": "tarjeta",
            "estado": "pendiente"
        }
        """;

        mockMvc.perform(post("/api/v1/payments")
                .header("Authorization", "Bearer token123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuario").value("Juan Pérez"));
    }

    @Test
    void obtenerPagoPorId_returnOk() throws Exception {
        Payment pago = new Payment();
        pago.setId(1L);
        pago.setUsuario("Ana");

        when(paymentService.obtenerPagoPorId(1L)).thenReturn(Optional.of(pago));

        mockMvc.perform(get("/api/v1/payments/1")
                .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.usuario").value("Ana"));
    }

    @Test
    void obtenerPagos_returnList() throws Exception {
        Payment pago = new Payment();
        pago.setId(1L);
        pago.setUsuario("Carlos");

        when(paymentService.obtenerPagos()).thenReturn(List.of(pago));

        mockMvc.perform(get("/api/v1/payments")
                .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].usuario").value("Carlos"));
    }

    @Test
    void actualizarPago_returnUpdated() throws Exception {
        Payment pago = new Payment();
        pago.setId(1L);
        pago.setEstado("pagado");

        when(paymentService.actualizarEstado(eq(1L), any(Payment.class)))
            .thenReturn(Optional.of(pago));

        String json = """
        {
            "estado": "pagado"
        }
        """;

        mockMvc.perform(put("/api/v1/payments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("pagado"));
    }

    @Test
    void eliminarPago_returnOk() throws Exception {
        doNothing().when(paymentService).eliminarPago(1L);

        mockMvc.perform(delete("/api/v1/payments/1")
                .header("Authorization", "Bearer admin"))
            .andExpect(status().isOk())
            .andExpect(content().string("Pago eliminado correctamente"));
    }
}