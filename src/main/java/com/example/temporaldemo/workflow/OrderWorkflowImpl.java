package com.example.temporaldemo.workflow;

import com.example.temporaldemo.activities.OrderActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class OrderWorkflowImpl implements OrderWorkflow {
    private final OrderActivity activity =
            Workflow.newActivityStub(OrderActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .build());

    @Override
    public String processOrder(String orderId) {

        String validation = activity.validateOrder(orderId);
        System.out.println("Order validated");

        if (!"VALID".equals(validation)) {
            return "ORDER REJECTED";
        }

        String payment = activity.chargePayment(orderId);
        System.out.println("Payment charged");

        String email = activity.sendEmail(orderId);
        System.out.println("Email send");

        return "SUCCESS: " + payment + " & " + email;
    }
}
