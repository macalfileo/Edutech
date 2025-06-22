package com.edutech.payment_service;

import com.edutech.payment_service.model.Payment;
import com.edutech.payment_service.service.PaymentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);

        PaymentService service = new PaymentService();

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setUser("Maria López");
        payment.setAmount(14990);

        service.processPayment(payment);
        service.getAllPayments().forEach(p -> {
            System.out.println("Pago de " + p.getUser() + ": $" + p.getAmount());
        });
    }
}