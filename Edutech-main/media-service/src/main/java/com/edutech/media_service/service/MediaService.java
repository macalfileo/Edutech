package com.edutech.media_service.service;

import com.edutech.media_service.model.MediaFile;
import com.edutech.media_service.repository.MediaFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MediaService {
    
    private final MediaFileRepository mediaFileRepository;

    public MediaService(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    public MediaFile guardarArchivo(MultipartFile archivo) throws IOException {
        MediaFile media = new MediaFile();
        media.setNombreArchivo(archivo.getOriginalFilename());
        media.setTipoArchivo(archivo.getContentType());
        media.setContenido(archivo.getBytes());
        return mediaFileRepository.save(media);
    }

    public List<MediaFile> listarArchivos() {
        return mediaFileRepository.findAll();
    }

     public MediaFile actualizarArchivo(Long id, MultipartFile nuevoArchivo) throws IOException {
        MediaFile existente = mediaFileRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }

        existente.setNombreArchivo(nuevoArchivo.getOriginalFilename());
        existente.setTipoArchivo(nuevoArchivo.getContentType());
        existente.setContenido(nuevoArchivo.getBytes());

        return mediaFileRepository.save(existente);
    }

    public boolean eliminarArchivo(Long id) {
        if (mediaFileRepository.existsById(id)) {
            mediaFileRepository.deleteById(id);
            return true;
        }
        return false;
    }

}