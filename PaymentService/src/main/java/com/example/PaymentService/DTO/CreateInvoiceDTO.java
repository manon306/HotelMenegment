package com.example.PaymentService.DTO;

import com.example.PaymentService.Entity.Invoice;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateInvoiceDTO {

    private Long bookingId;
    private Long paymentId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String serviceDescription;
    private Double amount;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;

    public Invoice toEntity() {
        Invoice invoice = new Invoice();

        invoice.setBookingId(this.bookingId);
        invoice.setPaymentId(this.paymentId);
        invoice.setCustomerName(this.customerName);
        invoice.setCustomerEmail(this.customerEmail);
        invoice.setCustomerPhone(this.customerPhone);
        invoice.setServiceDescription(this.serviceDescription);
        invoice.setAmount(this.amount);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceDate(invoiceDate);

        invoice.setInvoiceNumber("INV-" + UUID.randomUUID());
        invoice.setInvoiceDate(LocalDateTime.now());

        return invoice;
    }
}