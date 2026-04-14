package com.example.PaymentService.DTO;
import com.example.PaymentService.Entity.Payment;

import lombok.Data;

@Data
public class PaymentCreateRequest {
    private String number;
    private String cardNumber;
    private String expiryDate;
    private String cvc;

    public Payment toEntity() {
        Payment payment = new Payment();
        payment.setNumber(getNumber());
        return payment;
    }
}
