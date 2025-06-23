package com.edutech.community_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edutech.community_service.model.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}