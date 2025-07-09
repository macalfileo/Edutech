package com.edutech.payment_service.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "pagos")
@Data
public class Payment  {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pago", example = "1")
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario que realiza el pago", example = "1")
    private Long userId;

    @NotNull(message = "El ID de la inscripción es obligatorio")
    @Column(name = "enrollment_id", nullable = false)
    @Schema(description = "ID de la inscripción asociada al pago", example = "5")
    private Long enrollmentId;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    @Size(max = 100)
    @Column(name = "usuario", nullable = false)
    @Schema(description = "Nombre del usuario que realiza el pago", example = "Juan Pérez")
    private String usuario;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.0", message = "El monto debe ser mayor que 0")
    @Digits(integer = 10, fraction = 0, message = "El monto debe ser un número entero en pesos chilenos")
    @Column(name = "monto", nullable = false)
    @Schema(description = "Monto del pago en pesos chilenos (CLP)", example = "10000")
    private Integer monto; // CLP no usa decimales

    @NotBlank(message = "El método de pago es obligatorio")
    @Column(name = "metodo_pago", nullable = false)
    @Schema(description = "Método de pago utilizado (ej. tarjeta, transferencia)", example = "tarjeta")
    private String metodoPago; // tarjeta, transferencia, etc.

    @Column(name = "estado", nullable = false)
    @Schema(description = "Estado del pago (ej. pendiente, pagado, rechazado)", example = "pendiente")
    private String estado = "pendiente"; // pendiente, pagado, rechazado

    @Column(name = "fecha_pago", nullable = false)
    @Schema(description = "Fecha y hora en que se realizó el pago", example = "2025-06-24T12:34:00")
    private LocalDateTime fechaPago = LocalDateTime.now();
}