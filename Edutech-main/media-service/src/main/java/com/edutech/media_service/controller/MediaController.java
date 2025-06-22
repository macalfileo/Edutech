package com.edutech.media_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.service.MediaService;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/archivos")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService){
        this.mediaService = mediaService;

    }

    @PostMapping("/subir")
    public ResponseEntity<MediaFile> subir(@RequestParam("archivo")MultipartFile archivo) throws IOException {
        MediaFile guardado = mediaService.guardarArchivo(archivo);
        return ResponseEntity.ok(guardado);
    
    }

    @GetMapping
    public List<MediaFile> listar() {
        return mediaService.listarArchivos();
    }
     // PUT: Actualizar archivo por ID
     @PutMapping("/{id}")
    public ResponseEntity<MediaFile> actualizar(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) throws IOException {

        MediaFile actualizado = mediaService.actualizarArchivo(id, archivo);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: Eliminar archivo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = mediaService.eliminarArchivo(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
