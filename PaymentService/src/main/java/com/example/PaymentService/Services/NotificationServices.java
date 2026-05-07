package com.example.PaymentService.Services;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.http.MediaType;

@Service
public class NotificationServices {
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

    private HttpEntity<Map<String, Object>> buildAuthEntityWithBody(Long userId, String email, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getCurrentToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("email", email);
        body.put("message", message);

        return new HttpEntity<>(body, headers);
    }

    // public void SendSecretNumberInEmail(Long userId, String email, String
    // message) {
    // try {
    // UriComponentsBuilder builder = UriComponentsBuilder
    // .fromHttpUrl("http://NOTIFICATION-SERVICE/notifications/send")
    // .queryParam("userId", userId)
    // .queryParam("email", email)
    // .queryParam("message", message)
    // // .RequestBody(userId,email,); // Since we're using query parameters, the
    // body can be null

    // restTemplate.exchange(
    // builder.toUriString(),
    // HttpMethod.POST,
    // buildAuthEntity(),
    // Void.class);
    // } catch (Exception e) {
    // System.out.println("Notification sending failed: " + e.getMessage());
    // }
    // }
    public void SendSecretNumberInEmail(Long userId, String email, String message) {
        try {
            restTemplate.exchange(
                    "http://NOTIFICATION-SERVICE/notifications/send",
                    HttpMethod.POST,
                    buildAuthEntityWithBody(userId, email, message),
                    Void.class);
        } catch (Exception e) {
            System.out.println("Notification sending failed: " + e.getMessage());
        }

    }
}
