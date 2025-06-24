package com.edutech.community_service.service;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.repository.PublicacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
public class PublicacionService {

    @Autowired
    private PublicacionRepository repository;

    public Publicacion crear(Publicacion publicacion) {
        return repository.save(publicacion);
    }

    public List<Publicacion> listarTodas(){
        return repository.findAll();
    }

    public Optional <Publicacion> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
