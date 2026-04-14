package com.example.PaymentService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingClient {

    @Autowired
    private RestTemplate restTemplate;

    public void updateStatus(Long bookingId, String status) {
        try {
            String url = "http://BOOKING-SERVICE/bookings/" + bookingId + "/status?status=" + status;
            restTemplate.put(url, null);
        } catch (Exception e) {
            System.out.println("Booking update failed: " + e.getMessage());
        }
    }
}