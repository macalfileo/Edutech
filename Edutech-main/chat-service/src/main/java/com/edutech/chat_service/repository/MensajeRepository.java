package com.edutech.chat_service.repository;

import com.edutech.chat_service.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByRemitenteIdAndDestinatarioIdOrderByCreadoEnAsc(Long remitenteId, Long destinatarioId);
}