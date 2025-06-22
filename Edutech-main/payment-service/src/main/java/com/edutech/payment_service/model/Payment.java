package com.edutech.payment_service.model;

import lombok.Data;

@Data
public class Payment {
    private Long id;
    private String user;
    private double amount;
}