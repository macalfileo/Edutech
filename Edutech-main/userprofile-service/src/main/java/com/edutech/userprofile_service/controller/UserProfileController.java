package com.edutech.userprofile_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.userprofile_service.model.Genero;
import com.edutech.userprofile_service.model.UserProfile;
import com.edutech.userprofile_service.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/usuario_perfil")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Endpoint para consultar todos los perfiles
    @Operation(summary = "Obtener todos los perfiles", description = "Retorna todos los perfiles de usuario registrados.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfiles encontrados", 
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(
        responseCode = "204", 
        description = "No hay perfiles disponibles")
    @GetMapping("/perfiles")
    public ResponseEntity<List<UserProfile>> getPerfiles() {
        List<UserProfile> perfiles = userProfileService.obtenerUserProfile();
        if (perfiles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(perfiles);
    }
    
    // Endpoint para consultar por id los perfiles
    @Operation(summary = "Obtener perfil por ID", description = "Devuelve un perfil si el ID existe.")
    @ApiResponse(responseCode = "200", 
    description = "Perfil encontrado", 
    content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(
        responseCode = "404", 
        description = "Perfil no encontrado")
    @GetMapping("/perfiles/{id}")
    public ResponseEntity<?> obtenerUserProfilePorId(@PathVariable Long id) {
        try {
            UserProfile userProfile = userProfileService.obtenerUserProfilePorId(id);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener perfil por userId (desde AuthService)
    @Operation(summary = "Obtener perfil por ID de usuario", description = "Devuelve el perfil asociado al userId si existe.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfil encontrado", 
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(
        responseCode = "404", 
        description = "Perfil no encontrado")
    @GetMapping("/perfiles/user/{userId}")
    public ResponseEntity<?> obtenerPorUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userProfileService.obtenerUserProfilePorUserId(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para consultar perfiles por nombre
    @Operation(summary = "Buscar perfiles por nombre", description = "Retorna perfiles cuyo nombre coincida.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfiles encontrados")
    @GetMapping("/perfiles/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(userProfileService.buscarPorNombre(nombre));
    }

    // Endpoint para consultar perfiles por apellido
    @Operation(summary = "Buscar perfiles por apellido", description = "Retorna perfiles cuyo apellido coincida.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfiles encontrados")
    @GetMapping("/perfiles/apellido/{apellido}")
    public ResponseEntity<?> buscarPorApellido(@PathVariable String apellido) {
        return ResponseEntity.ok(userProfileService.buscarPorApellido(apellido));
    }

    // Endpoint para consultar perfiles por género
    @Operation(summary = "Buscar perfiles por género", description = "Retorna perfiles que coincidan con el género especificado.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfiles encontrados")
    @GetMapping("/perfiles/genero/{genero}")
    public ResponseEntity<?> buscarPorGenero(@PathVariable Genero genero) {
        return ResponseEntity.ok(userProfileService.buscarPorGenero(genero));
    }
    
    @Operation(summary = "Crear nuevo perfil", description = "Crea un nuevo perfil de usuario.")
    @ApiResponse(
        responseCode = "201", 
        description = "Perfil creado exitosamente", 
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(
        responseCode = "400", 
        description = "Datos inválidos")
    // Endpoint para crear un perfil nuevo
    @PostMapping("/perfiles")
    public ResponseEntity<?> crearUserProfile(@RequestBody UserProfile userProfile) {
        try {
            UserProfile nuevo = userProfileService.crearPerfil(userProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar perfil", description = "Actualiza los datos de un perfil de usuario.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfil actualizado exitosamente", 
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(
        responseCode = "404", 
        description = "Perfil no encontrado")
    // Endpoint para actualizar un perfil
    @PutMapping("/perfiles/{id}")
    public ResponseEntity<?> actualizarUserProfile(@PathVariable Long id, @RequestBody UserProfile userProfile) {
        try {
            UserProfile actualizado = userProfileService.actualizarUserProfile(id, userProfile);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para eliminar un perfil y el usuario asociado
    @Operation(summary = "Eliminar perfil", description = "Elimina un perfil de usuario por ID.")
    @ApiResponse(
        responseCode = "200", 
        description = "Perfil eliminado correctamente")
    @ApiResponse(
        responseCode = "500", 
        description = "Error interno del servidor")
    @DeleteMapping("/perfiles/{id}")
    public ResponseEntity<?> eliminarPerfil(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userProfileService.eliminarUserProfile(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}