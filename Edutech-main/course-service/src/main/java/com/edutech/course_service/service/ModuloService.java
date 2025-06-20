package com.edutech.course_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.repository.CourseRepository;
import com.edutech.course_service.repository.ModuloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ModuloService {
    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    // Obtener todos los módulos
    public List<Modulo> obtenerModulos() {
        return moduloRepository.findAll();
    }

    // Obtener módulo por ID
    public Modulo obtenerModuloPorId(Long id) {
        return moduloRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Nódulo no encontrado Id: "+ id));
    }

    // Obtener módulos por curso, ordenados por orden ascendente
    public List<Modulo> obtenerModulosPorCurso(Long cursoId) {
        return moduloRepository.findByCursoIdOrderByOrdenAsc(cursoId);
    }

    // Crear nuevo módulo
    public Modulo crearModulo(String titulo, String descripcion, Integer orden, Long cursoId) {
        if (titulo == null || descripcion == null || orden == null || cursoId == null) {
            throw new RuntimeException("Todos los campos son obligatorios");
        }
        
        Course curso = courseRepository.findById(cursoId)
        .orElseThrow(()-> new RuntimeException("Curso no encontrado Id: "+ cursoId));
        Modulo modulo = new Modulo();
        modulo.setTitulo(titulo);
        modulo.setDescripcion(descripcion);
        modulo.setOrden(orden);
        modulo.setCurso(curso);

        return moduloRepository.save(modulo);
    }

    // Actualizar módulo existente
    public Modulo actualizarModulo(Long id, String titulo, String descripcion, Integer orden) {
        Modulo modulo = moduloRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Módulo no encontrado Id: " + id));

        if (titulo != null && !titulo.trim().isEmpty()) {
            modulo.setTitulo(titulo);
        }

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            modulo.setDescripcion(descripcion);
        }

        if (orden != null && orden >= 1) {
            modulo.setOrden(orden);
        }

        return moduloRepository.save(modulo);
    }

    // Eliminar módulo
    public String eliminarModulo(Long id) {
        Modulo modulo = moduloRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Módulo no encontrado Id: " + id));

        moduloRepository.delete(modulo);
        return "Módulo eliminado";
    }

}
