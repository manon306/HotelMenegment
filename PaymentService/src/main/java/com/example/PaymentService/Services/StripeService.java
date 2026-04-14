package com.example.PaymentService.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;

import org.springframework.beans.factory.annotation.Value;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public PaymentIntent createIntent(Double amount) throws Exception {

        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", Math.round(amount * 100));
        params.put("currency", "usd");
        params.put("payment_method_types", List.of("card"));

        return PaymentIntent.create(params);
    }

    public PaymentIntent retrieveIntent(String id) throws Exception {
        Stripe.apiKey = secretKey;
        return PaymentIntent.retrieve(id);
    }
}