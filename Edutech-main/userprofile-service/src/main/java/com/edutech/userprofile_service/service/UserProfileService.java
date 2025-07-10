package com.edutech.userprofile_service.service;

import com.edutech.userprofile_service.model.UserProfile;
import com.edutech.userprofile_service.repository.UserProfileRepository;
import com.edutech.userprofile_service.webclient.AuthClient;
import com.edutech.userprofile_service.webclient.MediaClient;
import com.edutech.userprofile_service.webclient.NotificationClient;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository; 

    @Autowired
    private AuthClient authClient;

    @Autowired 
    private MediaClient mediaClient;

    @Autowired 
    private NotificationClient notificationClient;

    // Crear perfil
    public UserProfile crearPerfil(UserProfile perfil) {
        if (perfil.getUserId() == null) {
            throw new RuntimeException("El ID del usuario (userId) es obligatorio");
        }

        if (!authClient.existeUsuario(perfil.getUserId())) {
            throw new RuntimeException("El usuario con ID " + perfil.getUserId() + " no existe en el AuthService");
        }
        
        if (userProfileRepository.findByUserId(perfil.getUserId()).isPresent()) {
            throw new RuntimeException("El usuario ya tiene un perfil asociado");
        }

        if (perfil.getTelefono() == null || perfil.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        if (userProfileRepository.existsByTelefono(perfil.getTelefono())) {
            throw new RuntimeException("El teléfono ya se encuentra registrado");
        }

        if (perfil.getFotoPerfil() != null && !mediaClient.avatarExiste(perfil.getFotoPerfil())) {
            throw new RuntimeException("La URL de la foto de perfil no es válida en MediaService");
        }

        return userProfileRepository.save(perfil);
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

    // Obtener perfil por userId (desde AuthService)
    public UserProfile obtenerUserProfilePorUserId(Long userId){
        return userProfileRepository.findByUserId(userId)
        .orElseThrow(()-> new RuntimeException("Perfil no encontrado para el usuario Id: "+ userId));
    }

    // Actualizar perfil
    public UserProfile actualizarUserProfile(Long id, UserProfile nuevosDatos) {
        UserProfile userProfile = userProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Perfil de usuario no encontrado"));

        if (nuevosDatos.getNombre() != null) {
            userProfile.setNombre(nuevosDatos.getNombre());
        }

        if (nuevosDatos.getApellido() != null) {
            userProfile.setApellido(nuevosDatos.getApellido());
        }

        if (nuevosDatos.getTelefono() != null && !nuevosDatos.getTelefono().equals(userProfile.getTelefono())) {
            if (userProfileRepository.existsByTelefono(nuevosDatos.getTelefono())) {
                throw new RuntimeException("El teléfono ya está en uso por otro perfil");
            }
            userProfile.setTelefono(nuevosDatos.getTelefono());
        }

        if (nuevosDatos.getFotoPerfil() != null) {
            if (!mediaClient.avatarExiste(nuevosDatos.getFotoPerfil())) {
                throw new RuntimeException("La nueva URL del avatar no es válida en MediaService");
            }
            userProfile.setFotoPerfil(nuevosDatos.getFotoPerfil());
        }

        if (nuevosDatos.getBiografia() != null) {
            userProfile.setBiografia(nuevosDatos.getBiografia());
        }


        if (nuevosDatos.getFechaNacimiento() != null) {
            userProfile.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        }

        if (nuevosDatos.getDireccion() != null) {
            userProfile.setDireccion(nuevosDatos.getDireccion());
        }

        if (nuevosDatos.getNotificaciones() != null) {
            userProfile.setNotificaciones(nuevosDatos.getNotificaciones());
        }

        UserProfile actualizado = userProfileRepository.save(userProfile);

        notificationClient.enviarNotificacionPerfil(
            actualizado.getUserId(),
            "Perfil actualizado",
            "Tu perfil ha sido actualizado exitosamente."
        );

        return actualizado;
    }

    // Eliminar perfil
    public String eliminarUserProfile(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Perfil de usuario no encontrado. ID: " + id));

        Long userId = userProfile.getUserId();
        userProfileRepository.delete(userProfile);

        try {
            authClient.eliminarUsuario(userId); // Llama a AuthService para eliminar también al usuario
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario en AuthService: " + e.getMessage());
        }

        return "Perfil y usuario eliminados correctamente";
    }

    // Buscar perfiles por nombre (ignora mayúsculas y minúsculas)
    public List<UserProfile> buscarPorNombre(String nombre) {
        return userProfileRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar perfiles por apellido
    public List<UserProfile> buscarPorApellido(String apellido) {
        return userProfileRepository.findByApellidoContainingIgnoreCase(apellido);
    }

}