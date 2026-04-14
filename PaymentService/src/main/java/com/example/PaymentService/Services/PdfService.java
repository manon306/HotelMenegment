package com.example.PaymentService.Services;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;


import com.example.PaymentService.Entity.Invoice;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


@Service
public class PdfService {

    public byte[] generateInvoicePdf(Invoice invoice) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();

        document.add(new Paragraph("INVOICE"));
        document.add(new Paragraph("-----------------------------"));

        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
        document.add(new Paragraph("Booking ID: " + invoice.getBookingId()));
        document.add(new Paragraph("Payment ID: " + invoice.getPaymentId()));
        document.add(new Paragraph("Customer Name: " + invoice.getCustomerName()));
        document.add(new Paragraph("Email: " + invoice.getCustomerEmail()));
        document.add(new Paragraph("Phone: " + invoice.getCustomerPhone()));
        document.add(new Paragraph("Amount: " + invoice.getAmount()));
        document.add(new Paragraph("Date: " + invoice.getInvoiceDate()));

        document.add(new Paragraph("-----------------------------"));
        document.add(new Paragraph("Thank you for your payment!"));

        document.close();

        return out.toByteArray();
    }
}