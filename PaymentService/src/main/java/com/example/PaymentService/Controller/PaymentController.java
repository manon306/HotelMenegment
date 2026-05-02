package com.example.PaymentService.Controller;

import com.example.PaymentService.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam Long bookingId, @RequestParam Double amount) throws Exception {
        String clientSecret = paymentService.createPayment(bookingId, amount);
        return ResponseEntity.ok(clientSecret);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestParam String paymentIntentId,
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String customerPhone) throws Exception {

        String status = paymentService.confirmPayment(paymentIntentId, customerName, customerEmail, customerPhone);

        return ResponseEntity.ok(status);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/retry")
    public ResponseEntity<?> retryPayment(@RequestParam Long bookingId,
            @RequestParam Double amount) throws Exception {

        String clientSecret = paymentService.retryPayment(bookingId, amount);

        return ResponseEntity.ok(clientSecret);
    }

    @GetMapping("/PendingPayment")
    public int getCountPendingPayment() {
        return paymentService.countByPendingPayment();
    }

}