package com.example.PaymentService.Services;

import com.example.PaymentService.Entity.Invoice;
import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Repository.InvoiceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.example.PaymentService.Exceptions.ResourceNotFoundException;

@Service
public class InvoiceServices {
    @Autowired
    private InvoiceRepo invoiceRepo;

    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        if (id == null || !invoiceRepo.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        return invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    public void deleteInvoice(Long id) {
        if (id == null || !invoiceRepo.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        invoiceRepo.deleteById(id);
    }

    public void generate(Payment payment, String customerName, String customerEmail, String customerPhone) {

        Invoice invoice = new Invoice();
        invoice.setBookingId(payment.getBookingId());
        invoice.setPaymentId(payment.getId());
        invoice.setAmount(payment.getAmount());
        invoice.setCustomerName(customerName != null ? customerName : "UNKNOWN");
        invoice.setCustomerEmail(customerEmail != null ? customerEmail : "UNKNOWN");
        invoice.setCustomerPhone(customerPhone);

        invoice.setInvoiceNumber("INV-" + UUID.randomUUID());
        invoice.setInvoiceDate(LocalDateTime.now());

        invoiceRepo.save(invoice);
    }
}