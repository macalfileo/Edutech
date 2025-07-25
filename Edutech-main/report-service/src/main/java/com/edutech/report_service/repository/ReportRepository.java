package com.edutech.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.report_service.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUsuarioId(Long usuarioId);

    List<Report> findByTipo(String tipo);
}
