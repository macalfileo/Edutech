package com.edutech.course_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.repository.ContenidoRepository;
import com.edutech.course_service.repository.ModuloRepository;
import com.edutech.course_service.webclient.AuthClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContenidoService {
    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private AuthClient authClient;

    // Obtener todos los contenidos
    public List<Contenido> obtenerContenidos() {
        return contenidoRepository.findAll();
    }

    // Obtener contenido por ID
    public Contenido obtenerContenidoPorId(Long id) {
        return contenidoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Contenido no encontrado Id: " + id));
    }

    // Obtener contenidos por módulo
    public List<Contenido> obtenerContenidosPorModulo(Long moduloId) {
        return contenidoRepository.findByModuloId(moduloId);
    }

    // Crear contenido nuevo
    public Contenido crearContenido(String authHeader, String titulo, String tipo, String url, Long moduloId) {
        if (titulo == null || tipo == null || moduloId == null) {
            throw new RuntimeException("El título, tipo y módulo son obligatorios");
        }

       Modulo modulo = moduloRepository.findById(moduloId)
            .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Long instructorId = modulo.getCurso().getInstructorId();

       if (!authClient.usuarioPuedeModificarCurso(authHeader, instructorId)) {
            throw new RuntimeException("No tienes permiso para agregar contenido a este módulo");
        }

        Contenido contenido = new Contenido();
        contenido.setTitulo(titulo);
        contenido.setDescripcion("Contenido de " + titulo); // Puedes personalizar esto si lo deseas
        contenido.setTipo(tipo);
        contenido.setUrl(url);
        contenido.setModulo(modulo);

        return contenidoRepository.save(contenido);
    }


    // Actualizar contenido
    public Contenido actualizarContenido(String authHeader, Long id, String titulo, String tipo, String url) {
        Contenido contenido = contenidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenido no encontrado Id: " + id));

        Long instructorId = contenido.getModulo().getCurso().getInstructorId();

        if (!authClient.usuarioPuedeModificarCurso(authHeader, instructorId)) {
            throw new RuntimeException("No tienes permiso para modificar este contenido");
        }

        if (titulo != null && !titulo.trim().isEmpty()) {
            contenido.setTitulo(titulo);
        }

        if (tipo != null && !tipo.trim().isEmpty()) {
            contenido.setTipo(tipo);
        }

        if (url != null && !url.trim().isEmpty()) {
            contenido.setUrl(url);
        }

        return contenidoRepository.save(contenido);
    }


    // Eliminar contenido
    public String eliminarContenido(String authHeader, Long id) {
        Contenido contenido = contenidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenido no encontrado Id: " + id));

        Long instructorId = contenido.getModulo().getCurso().getInstructorId();

        if (!authClient.usuarioPuedeModificarCurso(authHeader, instructorId)) {
            throw new RuntimeException("No tienes permiso para eliminar este contenido");
        }

        contenidoRepository.delete(contenido);
        return "Contenido eliminado";
    }

}
