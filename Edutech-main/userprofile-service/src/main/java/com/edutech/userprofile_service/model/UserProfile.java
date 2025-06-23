package com.edutech.userprofile_service.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Table(name = "perfiles_usuario")
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 4, max = 30, message = "El nombre debe tener entre 4 y 30 caracteres")
    private String nombre;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 4, max = 30, message = "El apellido debe tener entre 4 y 30 caracteres")
    private String apellido;

    @Size(max = 255, message = "La URL de la foto no puede exceder los 255 caracteres")
    @Pattern(regexp = "^(https?|ftp)://[^\s/$.?#].[^\s]*$", message = "La URL de la foto debe ser válida") // Validación para URL de foto de perfil
    private String fotoPerfil;

    @Size(max = 255, message = "La biografía no puede exceder los 255 caracteres")
    private String biografia;

    @Column(nullable = false, unique = true, length = 12)
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "\\+?\\d{9,12}", message = "El número de teléfono debe contener entre 9 y 12 dígitos")
    private String telefono; // Número de teléfono del usuario, debe ser único y tener entre 9 y 12 dígitos

    @Enumerated(EnumType.STRING) // Enum para el género, se almacena como cadena
    @NotNull(message = "El género no puede estar vacío")
    private Genero genero;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada") // Validación para que la fecha de nacimiento sea en el pasado
    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    private LocalDate fechaNacimiento; // Fecha de nacimiento del usuario, debe ser una fecha pasada

    @Column(nullable = false, length = 50) // Longitud máxima de la dirección
    @Size(max = 50, message = "La dirección no puede exceder los 50 caracteres")
    @NotBlank(message = "La dirección no puede estar vacía") // Validación para que la dirección no esté vacía
    private String direccion; // Dirección del usuario, opcional

    @NotNull(message = "La preferencia de notificaciones no puede estar vacía")
    private Boolean notificaciones; // Preferencia de notificaciones del usuario, no puede ser nula

    
}