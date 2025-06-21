package com.edutech.userprofile_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.userprofile_service.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    boolean existsByEmail(String email); // Para verificar si ya existe un perfil, devuelve true o false
    boolean exitsByTelefono(String telefono);
    
}
