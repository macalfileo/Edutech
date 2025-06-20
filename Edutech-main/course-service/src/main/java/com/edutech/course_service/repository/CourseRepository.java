package com.edutech.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.course_service.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorId(Long instructorId); // Lista completa de cursos creados por un instructor
    List<Course> findByCategoria(String categoria); // Buscar por categoría
    List<Course> findByTituloContainingIgnoreCase(String titulo); // Buscar por título que contenga algo
}
