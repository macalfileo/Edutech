package com.edutech.chat_service.service;

import com.edutech.chat_service.dto.MensajeDTO;
import com.edutech.chat_service.model.Mensaje;
import com.edutech.chat_service.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private MensajeRepository repo;

    public Mensaje enviarMensaje(MensajeDTO dto) {
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitenteId(dto.getRemitenteId());
        mensaje.setDestinatarioId(dto.getDestinatarioId());
        mensaje.setContenido(dto.getContenido());
        mensaje.setCreadoEn(null);;
        return repo.save(mensaje);
    }

    public List<Mensaje> obtenerConversacion(Long remitenteId, Long destinatarioId) {
        return repo.findByRemitenteIdAndDestinatarioIdOrderByCreadoEnAsc(remitenteId, destinatarioId);
    }

    public List<Mensaje> obtenerTodos() {
        return repo.findAll();
    }

    public MensajeRepository getRepo() {
        return repo;
    }

    public void setRepo(MensajeRepository repo) {
        this.repo = repo;
    }

    public ChatService() {
    }

    public void sendMessage(Mensaje message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }

    public Object getAllMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMessages'");
    }
}