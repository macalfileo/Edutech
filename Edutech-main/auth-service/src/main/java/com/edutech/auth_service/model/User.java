package com.edutech.auth_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // Metodos, setters, getters, tostring
@Entity // Se reconoce como una entidad JPA
@Table(name = "users") // Nombre de la tabla para la base de datos
@NoArgsConstructor // Constructor vacio
@AllArgsConstructor // Constructor con todos los datos

public class User {
    @Id // Identificador clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrementable en la base de datos
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // Campo obligatorio en base de datos, debe ser único y longitud max de 50 caracteres
    @NotBlank(message = "El nombre de usuario no puede estar vacío") // Para no aceptar espacio vacios
    @Size(min = 4, message = "El nombre de usuario debe tener al menos 4 caracteres") // Para que el username(nombre de usuario) no sea demasiado corto
    private String username;

    @Column(nullable = false, unique = true, length = 50) // Para la recuperación de contraseña y notificaciones
    @NotBlank(message = "El email no puede estar vacío") // Para no aceptar espacio vacios
    @Email(message = "Debe ingresar un correo válido") // Para una validacion con el email
    private String email;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "La contraseña no debe estar vacía") // Para no aceptar espacio vacios
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") // Para que la contraseña no sea demasiado corta
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn; // Fecha exacta en la que se creo

    @UpdateTimestamp
    private LocalDateTime actualizadoEn; // Fecha exacta en la que se actualizo

    @ManyToOne // Muchos usuarios pueden tener un solo rol
    @JoinColumn(name = "rol_id") // Tabla user, tendra una llave foranea
    @JsonIgnoreProperties("usuarios") // Previene ciclos infinitos
    private Rol rol;


}