package com.edutech.userprofile_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Metodos, setters, getters, tostring
@Table(name = "perfiles_usuario") // Nombre de la tabla para la base de datos
@Entity // Se reconoce como una entidad JPA
@NoArgsConstructor // Constructor vacio
@AllArgsConstructor // Constructor con todos los datos

public class UserProfile {
    @Id // Identificador clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrementable en la base de datos
    private Long id;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 4, message = "El nombre debe tener al menos 4 caracteres")
    private String nombre;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 4, message = "El apellido debe tener al menos 4 caracteres")
    private String apellido;

    @Column(nullable = false, unique = true, length = 12)
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\+?\\d{9,12}", message = "El teléfono debe tener entre 9 y 12 digitos")
    private String telefono;

    @Column(length = 50)
    private String direccion;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ingresar un correo válido")
    private String email;

}