package com.example.PaymentService.Services;

import com.stripe.model.PaymentIntent;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Enum.PaymentStatus;
import com.example.PaymentService.Enum.PaymentType;
import com.example.PaymentService.Repository.PaymentRepository;
import com.example.PaymentService.Exceptions.BadRequestException;
import com.example.PaymentService.Exceptions.ResourceNotFoundException;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private InvoiceServices invoiceService;

    private Payment buildPayment(Long bookingId, Double amount, String transactionId) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentType(PaymentType.ONLINE);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(transactionId);
        return payment;
    }

    public String createPayment(long bookingId, Double amount) throws Exception {
        PaymentIntent intent = stripeService.createIntent(amount);
        Payment payment = buildPayment(bookingId, amount, intent.getId());
        paymentRepository.save(payment);
        return intent.getClientSecret();
    }

    public Payment findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public String confirmPayment(String paymentIntentId, String customerName, String customerEmail,
            String customerPhone) throws Exception {

        PaymentIntent intent = stripeService.retrieveIntent(paymentIntentId);

        Payment payment = paymentRepository.findByTransactionId(paymentIntentId);

        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found");
        }

        switch (intent.getStatus()) {

            case "succeeded":
                payment.setPaymentStatus(PaymentStatus.PAID);
                invoiceService.generate(payment, customerName, customerEmail, customerPhone);
                bookingClient.updateStatus(payment.getBookingId(), "ACCEPTED", paymentIntentId, "PAID");

                break;

            case "processing":
                payment.setPaymentStatus(PaymentStatus.PENDING);
                break;

            default:
                payment.setPaymentStatus(PaymentStatus.FAILED);
                bookingClient.updateStatus(payment.getBookingId(), "CANCELLED", paymentIntentId, "FAILED");
        }

        paymentRepository.save(payment);

        return payment.getPaymentStatus().name();
    }

    public Payment cashPayment(Long bookingId, Double amount) {
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentType(PaymentType.CASH);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("CASH-" + System.currentTimeMillis());

        return paymentRepository.save(payment);
    }

    private void validateRetry(Long bookingId) {

        List<Payment> payments = paymentRepository.findByBookingId(bookingId);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("No previous payment found");
        }

        Payment lastPayment = payments.get(payments.size() - 1);

        if (lastPayment.getPaymentStatus() != PaymentStatus.FAILED) {
            throw new BadRequestException("Retry allowed only after FAILED payment");
        }
    }

    public String retryPayment(Long bookingId, Double amount) throws Exception {

        validateRetry(bookingId);

        PaymentIntent intent = stripeService.createIntent(amount);

        Payment payment = buildPayment(bookingId, amount, intent.getId());

        paymentRepository.save(payment);

        return intent.getClientSecret();
    }

    public String refundPayment(String paymentIntentId) throws Exception {
        Payment payment = paymentRepository.findByTransactionId(paymentIntentId);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment record not found for Intent ID: " + paymentIntentId);
        }

        com.stripe.model.Refund stripeRefund = stripeService.createRefund(paymentIntentId);

        if ("succeeded".equals(stripeRefund.getStatus())) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        return stripeRefund.getStatus();
    }

    public int countByPendingPayment() {
        return paymentRepository.countByPaymentStatus(PaymentStatus.PENDING);
    }
}
