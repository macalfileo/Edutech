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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Course;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.service.ContenidoService;
import com.edutech.course_service.service.CourseService;
import com.edutech.course_service.service.ModuloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @Operation(summary = "Obtener todos los cursos", description = "Devuelve todos los cursos registrados.")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de cursos obtenida correctamente",
        content = @Content(schema = @Schema(implementation = Course.class)))
    @ApiResponse(
        responseCode = "204",
        description = "No hay cursos registrados",
        content = @Content
        )
    @GetMapping("/cursos")
    public ResponseEntity<List<Course>> getCourse() {
        List<Course> cursos = courseService.obtenerCourses();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cursos);
    }

    // Obtener todos los módulos
    @Operation(summary = "Obtener todos los módulos", description = "Devuelve todos los módulos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de módulos obtenida correctamente", content = @Content(schema = @Schema(implementation = Modulo.class)))
    @ApiResponse(responseCode = "204", description = "No hay módulos registrados", content = @Content)
    @GetMapping("/modulos")
    public ResponseEntity<List<Modulo>> getModulo() {
        List<Modulo> modulos = moduloService.obtenerModulos();
        if (modulos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(modulos);
    }

    // Obtener todos los contenidos
    @Operation(summary = "Obtener todos los contenidos", description = "Devuelve todos los contenidos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de contenidos obtenida correctamente", content = @Content(schema = @Schema(implementation = Contenido.class)))
    @GetMapping("/contenidos")
    public ResponseEntity<List<Contenido>> getContenido() {
        List<Contenido> contenidos = contenidoService.obtenerContenidos();
        if (contenidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contenidos);
    }

    // Obtener curso por ID
    @Operation(summary = "Obtener curso por ID", description = "Devuelve un curso si el ID existe.")
    @ApiResponse(responseCode = "200", description = "Curso encontrado", content = @Content(schema = @Schema(implementation = Course.class)))
    @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content)
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
    @Operation(summary = "Obtener módulo por ID", description = "Devuelve un módulo si el ID existe.")
    @ApiResponse(responseCode = "200", description = "Módulo encontrado", content = @Content(schema = @Schema(implementation = Modulo.class)))
    @ApiResponse(responseCode = "404", description = "Módulo no encontrado", content = @Content)
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
    @Operation(summary = "Obtener contenido por ID", description = "Devuelve un contenido si el ID existe.")
    @ApiResponse(responseCode = "200", description = "Contenido encontrado", content = @Content(schema = @Schema(implementation = Contenido.class)))
    @ApiResponse(responseCode = "404", description = "Contenido no encontrado", content = @Content)
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
    @Operation(summary = "Obtener módulos de un curso", description = "Devuelve todos los módulos de un curso específico.")
    @ApiResponse(responseCode = "200", description = "Lista de módulos obtenida correctamente", content = @Content(schema = @Schema(implementation = Modulo.class)))
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
    @Operation(summary = "Obtener contenidos de un módulo", description = "Devuelve todos los contenidos de un módulo específico.")
    @ApiResponse(responseCode = "200", description = "Lista de contenidos obtenida correctamente", content = @Content(schema = @Schema(implementation = Contenido.class)))
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
    @Operation(summary = "Crear un nuevo curso", description = "Registra un nuevo curso con su información.")
    @ApiResponse(responseCode = "201", description = "Curso creado exitosamente", content = @Content(schema = @Schema(implementation = Course.class)))
    @ApiResponse(responseCode = "404", description = "Error en los datos enviados", content = @Content)
    @PostMapping("/cursos")
    public ResponseEntity<?> crearCourse(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Course curso) {
        try {
            Course nuevo = courseService.crearCourse(authHeader, curso.getTitulo(), curso.getDescripcion(), curso.getInstructorId(), curso.getDuracionMinutos(), curso.getCategoria());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    // Crear nuevo módulo
    @Operation(summary = "Crear un nuevo módulo", description = "Registra un nuevo módulo asociado a un curso.")
    @ApiResponse(responseCode = "201", description = "Módulo creado exitosamente", content = @Content(schema = @Schema(implementation = Modulo.class)))
    @ApiResponse(responseCode = "404", description = "Error en los datos enviados", content = @Content)
    @PostMapping("/modulos")
    public ResponseEntity<?> crearModulo(@RequestHeader("Authorization") String authHeader, @RequestBody Modulo modulo){
        try {
            Modulo nuevo = moduloService.crearModulo(authHeader, modulo.getTitulo(), modulo.getDescripcion(), modulo.getOrden(), modulo.getCurso().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Crear contenido nuevo
    @Operation(summary = "Crear un nuevo contenido", description = "Registra un nuevo contenido dentro de un módulo.")
    @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente", content = @Content(schema = @Schema(implementation = Contenido.class)))
    @ApiResponse(responseCode = "404", description = "Error en los datos enviados", content = @Content)
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
    @Operation(summary = "Actualizar curso", description = "Actualiza los datos de un curso dado su ID.")
    @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente", content = @Content(schema = @Schema(implementation = Course.class)))
    @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content)
    @PutMapping("/cursos/{id}")
    public ResponseEntity<?> actualizarCourse(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody Course curso
    ) {
        try {
            Course actualizado = courseService.actualizarCourse(
                authHeader,
                id,
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getInstructorId(),
                curso.getDuracionMinutos(),
                curso.getCategoria()
            );
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Actualizar módulo
    @Operation(summary = "Actualizar módulo", description = "Actualiza los datos de un módulo dado su ID.")
    @ApiResponse(responseCode = "200", description = "Módulo actualizado exitosamente", content = @Content(schema = @Schema(implementation = Modulo.class)))
    @ApiResponse(responseCode = "404", description = "Módulo no encontrado", content = @Content)
    @PutMapping("/modulos/{id}")
    public ResponseEntity<?> actualizarModulo(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody Modulo modulo
    ) {
        try {
            Modulo actualizado = moduloService.actualizarModulo(
                authHeader,
                id,
                modulo.getTitulo(),
                modulo.getDescripcion(),
                modulo.getOrden(),
                modulo.getCurso().getId()
            );
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
}

    // Actualizar contenido
    @Operation(summary = "Actualizar contenido", description = "Actualiza los datos de un contenido dado su ID.")
    @ApiResponse(responseCode = "200", description = "Contenido actualizado exitosamente", content = @Content(schema = @Schema(implementation = Contenido.class)))
    @ApiResponse(responseCode = "404", description = "Contenido no encontrado", content = @Content)
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
    @Operation(summary = "Eliminar un curso", description = "Elimina un curso según el ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content)
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<?> eliminarCourse(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            String mensaje = courseService.eliminarCourse(authHeader, id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar módulo
    @Operation(summary = "Eliminar un módulo", description = "Elimina un módulo según el ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Módulo eliminado correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Módulo no encontrado", content = @Content)
    @DeleteMapping("/modulos/{id}")
    public ResponseEntity<?> eliminarModulo(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            String mensaje = moduloService.eliminarModulo(authHeader, id);
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
           if (e.getMessage().contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar contenido
    @Operation(summary = "Eliminar un contenido", description = "Elimina un contenido según el ID proporcionado.")
    @ApiResponse(responseCode = "200", description = "Contenido eliminado correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Contenido no encontrado", content = @Content)
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

