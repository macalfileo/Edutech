package com.edutech.evaluation_service.service;

import com.edutech.evaluation_service.model.Evaluation;
import java.util.ArrayList;
import java.util.List;

public class EvaluationService {
    private final List<Evaluation> evaluations = new ArrayList<>();

    public void createEvaluation(Evaluation evaluation) {
        evaluations.add(evaluation);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluations;
    }
}
