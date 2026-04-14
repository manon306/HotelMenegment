package com.example.PaymentService.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentResponseDTO {
    private long id;
    private String number;
    private String cardNumber;
    private LocalDateTime paymentDate;
    private String cvc;
}
