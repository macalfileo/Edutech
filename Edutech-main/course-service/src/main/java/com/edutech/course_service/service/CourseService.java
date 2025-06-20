package com.edutech.course_service.service;


import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.course_service.model.Course;
import com.edutech.course_service.repository.CourseRepository;

@Service
@Transactional

public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> obtenerCourses() {
        return courseRepository.findAll();
    }

    public Course obtenerCoursePorId(Long id) {
        return courseRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ id));
    }

    public Course crearCourse(String titulo, String descripcion, Long instructorId, int duracionMinutos, String categoria) {
        if (titulo == null || descripcion == null || instructorId == null || duracionMinutos < 45 || categoria == null) {
            throw new RuntimeException("Todos los campos son obligatorios y la duración debe ser al menos 45 minutos");
        }
        
        Course curso = new Course();
        curso.setTitulo(titulo);
        curso.setDescripcion(descripcion);
        curso.setInstructorId(instructorId);
        curso.setDuracionMinutos(duracionMinutos);
        curso.setCategoria(categoria);

        return courseRepository.save(curso);
    }

    public Course actualizarCourse(Long id, String titulo, String descripcion, Long instructorId, Integer duracionMinutos, String categoria) {
        Course curso = courseRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ id));

        if (titulo != null && !titulo.trim().isEmpty()) {
            curso.setTitulo(titulo);
        }

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            curso.setDescripcion(descripcion);
        }

        if (instructorId != null) {
            curso.setInstructorId(instructorId);
        }

        if (duracionMinutos != null && duracionMinutos >= 45) {
            curso.setDuracionMinutos(duracionMinutos);
        }

        if (categoria != null && !categoria.trim().isEmpty()) {
            curso.setCategoria(categoria);
        }

        return courseRepository.save(curso);
    }

    public String eliminarCourse(Long id) {
        Course curso = courseRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ id));

        courseRepository.delete(curso);
        return "Curso eliminado";
    }

}