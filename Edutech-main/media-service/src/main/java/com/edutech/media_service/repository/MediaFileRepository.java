package com.edutech.media_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutech.media_service.model.MediaFile;

@Repository


public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

}