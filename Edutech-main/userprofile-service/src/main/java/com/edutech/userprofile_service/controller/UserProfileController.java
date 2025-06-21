package com.edutech.userprofile_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.userprofile_service.model.UserProfile;
import com.edutech.userprofile_service.service.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/usuario_perfil")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Endpoint para consultar todos los perfiles
    @GetMapping("/perfiles")
    public ResponseEntity<List<UserProfile>> getPerfiles() {
        List<UserProfile> perfiles = userProfileService.obtenerUserProfile();
        if (perfiles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(perfiles);
    }
    
    // Endpoint para consultar por id los perfiles
    @GetMapping("/perfiles/{id}")
    public ResponseEntity<?> obtenerUserProfilePorId(@PathVariable Long id) {
        try {
            UserProfile userProfile = userProfileService.obtenerUserProfilePorId(id);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para crear un perfil nuevo
    @PostMapping("/perfiles")
    public ResponseEntity<?> crearUserProfile(@RequestBody UserProfile userProfile) {
        try {
            UserProfile nuevo = userProfileService.crearUserProfile(userProfile.getName(), userProfile.getEmail(), userProfile.getTelefono());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}