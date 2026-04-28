package com.example.temporaldemo.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OrderActivity {

    @ActivityMethod
    String validateOrder(String orderId);

    @ActivityMethod
    String chargePayment(String orderId);

    @ActivityMethod
    String sendEmail(String orderId);
}
