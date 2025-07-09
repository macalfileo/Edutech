package com.edutech.media_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutech.media_service.model.MediaFile;

@Repository


public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    List<MediaFile> findByCourseId(Long courseId);

    List<MediaFile> findByEvaluationId(Long evaluationId);

    List<MediaFile> findByUserId(Long userId);

    List<MediaFile> findByOrigen(String origen);

}