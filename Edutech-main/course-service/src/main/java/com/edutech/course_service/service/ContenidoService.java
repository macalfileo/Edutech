package com.edutech.course_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.course_service.model.Contenido;
import com.edutech.course_service.model.Modulo;
import com.edutech.course_service.repository.ContenidoRepository;
import com.edutech.course_service.repository.ModuloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContenidoService {
    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private ModuloRepository moduloRepository;

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
    public Contenido crearContenido(String titulo, String tipo, String url, Long moduloId) {
        if (titulo == null || tipo == null || url == null || moduloId == null) {
            throw new RuntimeException("Todos los campos son obligatorios");
        }

        Modulo modulo = moduloRepository.findById(moduloId)
        .orElseThrow(() -> new RuntimeException("Módulo no encontrado Id: " + moduloId));

        Contenido nuevo = new Contenido();
        nuevo.setTitulo(titulo);
        nuevo.setTipo(tipo);
        nuevo.setUrl(url);
        nuevo.setModulo(modulo);

        return contenidoRepository.save(nuevo);
    }

    // Actualizar contenido
    public Contenido actualizarContenido(Long id, String titulo, String tipo, String url) {
        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado Id: " + id));

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
    public String eliminarContenido(Long id) {
        Contenido contenido = contenidoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Contenido no encontrado Id: " + id));

        contenidoRepository.delete(contenido);
        return "Contenido eliminado";
    }
}
