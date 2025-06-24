package com.edutech.evaluation_service.repository;

import com.edutech.evaluation_service.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}