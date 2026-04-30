package com.example.PaymentService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BookingClient {

    @Autowired
    private RestTemplate restTemplate;

    private String getCurrentToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return request.getHeader("Authorization");
        }
        return null;
    }

    private HttpEntity<Void> buildAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getCurrentToken());
        return new HttpEntity<>(headers);
    }

    public void updateStatus(Long bookingId, String status, String paymentIntentId) {
        try {
            // ضفنا paymentIntentId كـ Query Parameter في الـ URL
            String url = "http://BOOKING-SERVICE/bookings/" + bookingId + "/status?status=" + status + "&paymentIntentId=" + paymentIntentId;
            restTemplate.exchange(url, HttpMethod.PUT, buildAuthEntity(), Void.class);
        } catch (Exception e) {
            System.out.println("Booking update failed: " + e.getMessage());
        }
    }
}