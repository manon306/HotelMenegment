package com.BookingService.BookingService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PaymentClient {
    @Autowired
    private RestTemplate restTemplate;
    

    private String getTokenFromRequest() {
        ServletRequestAttributes attrs = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest().getHeader("Authorization");
    }

    private HttpEntity<String> buildAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getTokenFromRequest());
        return new HttpEntity<>(headers);
    }
    public void RefundPayment(String paymentIntentId) {
        String url = "http://PAYMENT-SERVICE/payments/refund?paymentIntentId=" + paymentIntentId;
        try {
            restTemplate.postForEntity(url, buildAuthEntity(), String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to refund payment: " + e.getMessage());
        }
    }
}
