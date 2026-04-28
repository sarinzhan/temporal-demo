package com.example.temporaldemo.activities;

import java.util.Random;

public class OrderActivityImpl implements OrderActivity {

    private static final Random RANDOM = new Random();

    @Override
    public String validateOrder(String orderId) {
        System.out.println("Validating order: " + orderId);
        if (RANDOM.nextInt(10) < 8) {
            throw new RuntimeException("Random failure in validateOrder for order: " + orderId);
        }
        return "VALID";
    }

    @Override
    public String chargePayment(String orderId) {
        System.out.println("Charging payment for: " + orderId);
        if (RANDOM.nextInt(10) < 8) {
            throw new RuntimeException("Random failure in chargePayment for order: " + orderId);
        }
        return "PAID";
    }

    @Override
    public String sendEmail(String orderId) {
        System.out.println("Sending confirmation email for: " + orderId);
        if (RANDOM.nextInt(10) < 8) {
            throw new RuntimeException("Random failure in sendEmail for order: " + orderId);
        }
        return "EMAIL_SENT";
    }
}
