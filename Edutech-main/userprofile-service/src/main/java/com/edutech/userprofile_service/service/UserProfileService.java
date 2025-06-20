package com.edutech.userprofile_service.service;

import com.edutech.userprofile_service.model.UserProfile;
import com.edutech.userprofile_service.repository.UserProfileRepository;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository; 

    // Crear perfil
    public UserProfile crearUserProfile(String nombre, String apellido, String email, String telefono, String direccion) {
        if (nombre == null || apellido == null || email == null || telefono == null) {
            throw new RuntimeException("Nombre, apellido, email y telefono son obligatorios");
        }

        if(userProfileRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya se encuentra registrado");
        }

        if (userProfileRepository.existsByTelefono(telefono)) {
            throw new RuntimeException("El telefono ya se encuentra registrado");
        }

        UserProfile userprofile = new UserProfile();
        userprofile.setNombre(nombre);
        userprofile.setApellido(apellido);
        userprofile.setEmail(email);
        userprofile.setTelefono(telefono);
        userprofile.setDireccion(direccion);

        return userProfileRepository.save(userprofile);
    }

    // Obtener todos los perfiles
    public List<UserProfile> obtenerUserProfile(){
        return userProfileRepository.findAll();
    }

    // Obtener por id
    public UserProfile obtenerUserProfilePorId(Long id){
        return userProfileRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Perfil de usuario no encontrado Id: "+ id));
    }

    // Actualizar perfil
    public UserProfile actualizarUserProfile(Long id, String nombre, String apellido, String email, String telefono, String direccion) {
        UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Perfil de usuario no encontrado"));

        if (nombre != null && !nombre.trim().isEmpty()) {
            userProfile.setNombre(nombre);; 
        }

        if (apellido != null && !apellido.trim().isEmpty()) {
            userProfile.setApellido(apellido);
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!email.equals(userProfile.getEmail()) && userProfileRepository.existsByEmail(email)) { // Compara si el nuevo email es diferente al actual.
                throw new RuntimeException("Ya existe un usuario con ese correo");
            }
            userProfile.setEmail(email);
        }

        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!telefono.equals(userProfile.getTelefono()) && userProfileRepository.existsByTelefono(telefono)) { // Compara si el nuevo telefono es diferente al actual.
                throw new RuntimeException("Ya existe un usuario con ese telefono");
            }
            userProfile.setTelefono(telefono);
        }

        if (direccion != null && !direccion.trim().isEmpty()) {
            userProfile.setDireccion(direccion);
        }

        return userProfileRepository.save(userProfile);
    }

    // Eliminar perfil
    public String eliminarUserProfile(Long id){
        UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Perfil de usuario no encontrado "+ id));
       
        userProfileRepository.delete(userProfile);
        return "Perfil de usuario eliminado";
    }
}