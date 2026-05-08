package com.example.PaymentService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

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
    
        String token = getCurrentToken();
    
        if (token != null && !token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
    
        headers.set("Authorization", token);
    
        return new HttpEntity<>(headers);
    }

    public void updateStatus(Long bookingId, String status, String paymentIntentId, String paymentStatus) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl("http://BOOKING-SERVICE/bookings/" + bookingId + "/status")
                    .queryParam("status", status)
                    .queryParam("paymentIntentId", paymentIntentId)
                    .queryParam("paymentStatus", paymentStatus);

            restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.PUT,
                    buildAuthEntity(),
                    Void.class);

        } catch (Exception e) {
            System.out.println("Booking update failed: " + e.getMessage());
        }
    }
}