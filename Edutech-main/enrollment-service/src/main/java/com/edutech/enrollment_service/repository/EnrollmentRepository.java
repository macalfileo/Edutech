package com.edutech.enrollment_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.enrollment_service.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
        
        List<Enrollment> findByUserId(Long userId); // Buscar todas las inscripciones de un usuario
        List<Enrollment> findByCourseId(Long courseId); // Buscar todas las inscripciones a un curso específico
        boolean existsByUserIdAndCourseId(Long userId, Long courseId); // Buscar una inscripción específica
    
}
