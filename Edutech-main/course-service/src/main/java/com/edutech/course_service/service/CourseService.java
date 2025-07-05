package com.edutech.course_service.service;


import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.course_service.model.Course;
import com.edutech.course_service.repository.CourseRepository;
import com.edutech.course_service.webclient.AuthClient;

@Service
@Transactional

public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthClient authClient;

    public List<Course> obtenerCourses() {
        return courseRepository.findAll();
    }

    public Course obtenerCoursePorId(Long id) {
        return courseRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ id));
    }

    public Course crearCourse(String authHeader, String titulo, String descripcion, Long instructorId, int duracionMinutos, String categoria) {
        if (titulo == null || descripcion == null || instructorId == null || duracionMinutos < 45 || categoria == null) {
            throw new RuntimeException("Todos los campos son obligatorios y la duración debe ser al menos 45 minutos");
        }

        if (!authClient.usuarioPuedeModificarCurso(authHeader, instructorId)) {
            throw new RuntimeException("El instructor con ID " + instructorId + " no tiene permisos para este curso");
        }

        Course curso = new Course();
        curso.setTitulo(titulo);
        curso.setDescripcion(descripcion);
        curso.setInstructorId(instructorId);
        curso.setDuracionMinutos(duracionMinutos);
        curso.setCategoria(categoria);

        return courseRepository.save(curso);
    }


    public Course actualizarCourse(String authHeader, Long id, String titulo, String descripcion, Long instructorId, Integer duracionMinutos, String categoria) {
        Course curso = courseRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ id));

        // Validación: ¿Es administrador o el mismo instructor?
        if (!authClient.usuarioPuedeModificarCurso(authHeader, curso.getInstructorId())) {
            throw new RuntimeException("No tienes permiso para modificar este curso");
        }

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

    public String eliminarCourse(String authHeader, Long id) {
        Course curso = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado Id: " + id));

        if (!authClient.usuarioPuedeModificarCurso(authHeader, curso.getInstructorId())) {
            throw new RuntimeException("No tienes permiso para eliminar este curso");
        }

        courseRepository.delete(curso);
        return "Curso eliminado";
    }


}