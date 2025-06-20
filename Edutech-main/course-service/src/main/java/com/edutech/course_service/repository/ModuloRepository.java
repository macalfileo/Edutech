package com.edutech.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    List<Modulo> findByCurso(Course curso);
    List<Modulo> findByCursoId(Long cursoId);
    List<Modulo> findByCursoIdOrderByOrdenAsc(Long cursoId);
}