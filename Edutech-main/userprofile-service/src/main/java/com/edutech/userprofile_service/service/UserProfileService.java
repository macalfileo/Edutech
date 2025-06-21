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
    public UserProfile crearUserProfile(String name, String email, String telefono) {
        if (name == null || email == null || telefono == null) {
            throw new RuntimeException("Nombre, email y telefono son obligatorios");
        }

        if(userProfileRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya se encuentra registrado");
        }

        if (userProfileRepository.exitsByTelefono(telefono)) {
            throw new RuntimeException("El telefono ya se encuentra registrado");
        }

        UserProfile userprofile = new UserProfile();
        userprofile.setName(name);
        userprofile.setEmail(email);
        userprofile.setTelefono(telefono);

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
    public UserProfile actualizarUserProfile(Long id, String name, String email, String telefono) {
        UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Perfil de usuario no encontrado"));

        if (name != null && !name.trim().isEmpty()) {
            userProfile.setName(name);; 
        }

        if (email != null && !email.trim().isEmpty()) {
           userProfile.setEmail(email);
        }

        return userProfileRepository.save(userProfile);
    }

    // Eliminar perfil
    public String eliminarUserProfile(Long id){
        UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Perfil de usuario no encontrado "+ id));
       
        //if (id == 1 || id == 2 || id == 3) {
          //  throw new RuntimeException("No se puede eliminar este usuario base del sistema");    
        //}
        userProfileRepository.delete(userProfile);
        return "Perfil de usuario eliminado";
    }
}