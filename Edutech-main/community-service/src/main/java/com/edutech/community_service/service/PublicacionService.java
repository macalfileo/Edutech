package com.edutech.community_service.service;

import com.edutech.community_service.model.Publicacion;
import com.edutech.community_service.repository.publicacionRepository; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.service;

import java.util.list;
import java.util.Optional;

@service 
public class PublicacionService {

    @Autowired
    private PublicionRepository repository;

    public Publicacion crear(Publicacion publicacion) {
        return repository.save(publicacion);
    }

    public List<publicacion> listarTodas(){
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
