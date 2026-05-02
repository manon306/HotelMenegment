package com.example.PaymentService.Repository;

import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Enum.PaymentStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByTransactionId(String transactionId);

    List<Payment> findByBookingId(Long bookingId);

    int countByPaymentStatus(PaymentStatus status);
}