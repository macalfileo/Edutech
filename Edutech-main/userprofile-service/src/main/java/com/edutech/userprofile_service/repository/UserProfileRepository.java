package com.edutech.userprofile_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.userprofile_service.model.UserProfile;



@Repository //Se comunica con la base de datos
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    UserProfile findByTelefono(String telefono);
    UserProfile findByEmail(String email);
    boolean existsByEmail(String email); // Para verificar si ya existe un perfil, devuelve true o false
    boolean existsByTelefono(String telefono);
    
}
