package com.edutech.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Modulo;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByModulo(Modulo modulo);
    List<Contenido> findByModuloId(Long moduloId);
    List<Contenido> findByModuloIdOrderByIdAsc(Long moduloId);
    List<Contenido> findByTipo(String tipo);
}
