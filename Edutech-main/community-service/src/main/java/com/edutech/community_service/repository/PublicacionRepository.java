package com.edutech.community_service.repository;

import com.edutech.community_service.model.publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}