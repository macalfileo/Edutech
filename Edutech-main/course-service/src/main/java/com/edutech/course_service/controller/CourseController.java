package com.edutech.course_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.service.ContenidoService;
import com.edutech.course_service.service.CourseService;
import com.edutech.course_service.service.ModuloService;

@RestController
@RequestMapping("/api/v1/courses") // Define la ruta base
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuloService moduloService;

    @Autowired
    private ContenidoService contenidoService;

    // Obtener todos los cursos
    @GetMapping("/cursos")
    public ResponseEntity<List<Course>> getCourse() {
        List<Course> cursos = courseService.obtenerCourses();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cursos);
    }

    // Obtener todos los módulos
    @GetMapping("/modulos")
    public ResponseEntity<List<Modulo>> getModulo() {
        List<Modulo> modulos = moduloService.obtenerModulos();
        if (modulos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(modulos);
    }

    // Obtener todos los contenidos
    @GetMapping("/contenidos")
    public ResponseEntity<List<Contenido>> getContenido() {
        List<Contenido> contenidos = contenidoService.obtenerContenidos();
        if (contenidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contenidos);
    }

    // Obtener curso por ID
    @GetMapping("/cursos/{id}")
    public ResponseEntity<?> obtenerCoursePorId(@PathVariable Long id) {
        try {
            Course curso = courseService.obtenerCoursePorId(id);
            return ResponseEntity.ok(curso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener módulo por ID
    @GetMapping("/modulos/{id}")
    public ResponseEntity<?> obtenerModuloPorId(@PathVariable Long id) {
        try {
            Modulo modulos = moduloService.obtenerModuloPorId(id);
            return ResponseEntity.ok(modulos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener contenido por ID
    @GetMapping("/contenidos/{id}")
    public ResponseEntity<?> obtenerContenidoPorId(@PathVariable Long id) {
        try {
            Contenido contenidos = contenidoService.obtenerContenidoPorId(id);
            return ResponseEntity.ok(contenidos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener módulos por curso
    @GetMapping("/cursos/{cursoId}/modulos")
    public ResponseEntity<?> obtenerModulosPorCurso(@PathVariable Long cursoId) {
        try {
            List<Modulo> modulos = moduloService.obtenerModulosPorCurso(cursoId);
            return ResponseEntity.ok(modulos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener contenidos por módulo
    @GetMapping("/modulos/{moduloId}/contenidos")
    public ResponseEntity<?> obtenerContenidosPorCurso(@PathVariable Long moduloId) {
        try {
            List<Contenido> contenidos = contenidoService.obtenerContenidosPorModulo(moduloId);
            return ResponseEntity.ok(contenidos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Crear nuevo curso
    @PostMapping("/cursos")
    public ResponseEntity<?> crearCourse(@RequestBody Course curso){
        try {
            Course nuevo = courseService.crearCourse(curso.getTitulo(), curso.getDescripcion(), curso.getInstructorId(), curso.getDuracionMinutos(), curso.getCategoria());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Crear nuevo módulo
    @PostMapping("/modulos")
    public ResponseEntity<?> crearModulo(@RequestBody Modulo modulo, @RequestParam Long cursoId){
        try {
            Modulo nuevo = moduloService.crearModulo(modulo.getTitulo(), modulo.getDescripcion(), modulo.getOrden(), cursoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Crear contenido nuevo
    @PostMapping("/contenidos")
    public ResponseEntity<?> crearContenido(@RequestBody Contenido contenidos){
        try {
            Contenido nuevo = contenidoService.crearContenido(contenidos.getTitulo(), contenidos.getTipo(), contenidos.getUrl(), contenidos.getModulo().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Actualizar curso
    @PutMapping("/cursos/{id}")
    public ResponseEntity<?> actualizarCourse(@PathVariable Long id, @RequestBody Course curso) {
        try {
            Course actualizado = courseService.actualizarCourse(id, curso.getTitulo(), curso.getDescripcion(), curso.getInstructorId(), curso.getDuracionMinutos(), curso.getCategoria());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Actualizar módulo
    @PutMapping("/modulos/{id}")
    public ResponseEntity<?> actualizarModulo(@PathVariable Long id, @RequestBody Modulo modulo) {
        try {
            Modulo actualizado = moduloService.actualizarModulo(id, modulo.getTitulo(), modulo.getDescripcion(), modulo.getOrden());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/contenidos/{id}")
    public ResponseEntity<?> actualizarContenido(@PathVariable Long id, @RequestBody Contenido contenidos) {
        try {
            Contenido actualizado = contenidoService.actualizarContenido(id,contenidos.getTitulo(), contenidos.getTipo(), contenidos.getUrl());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar curso
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<?> eliminarCourse(@PathVariable Long id) {
        try {
            String mensaje = courseService.eliminarCourse(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar módulo
    @DeleteMapping("/modulos/{id}")
    public ResponseEntity<?> eliminarModulo(@PathVariable Long id) {
        try {
            String mensaje = moduloService.eliminarModulo(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar contenido
    @DeleteMapping("/contenidos/{id}")
    public ResponseEntity<?> eliminarContenido(@PathVariable Long id) {
        try {
            String mensaje = contenidoService.eliminarContenido(id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

