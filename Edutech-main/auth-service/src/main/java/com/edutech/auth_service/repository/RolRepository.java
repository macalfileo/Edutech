package com.edutech.auth_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.auth_service.model.Rol;

@Repository //Se comunica con la base de datos
public interface RolRepository extends JpaRepository<Rol, Long> {
    List<Rol> findAllByOrderByIdAsc(); // Ordena todos los roles por el Id de forma ascendete
    Rol findByNombre(String nombre); // Permite buscar un rol por su nombre
    boolean existsByNombre(String nombre); // Validacion por si existe un rol con ese nombre
}