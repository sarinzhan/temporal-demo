package com.example.temporaldemo.ownworkflowengine;

import com.beeline.workflow.core.annotation.WorkflowComponent;
import com.beeline.workflow.core.api.Workflow;
import com.beeline.workflow.core.config.ActivityOptions;
import com.beeline.workflow.core.config.RetryPolicy;

import java.time.Duration;

@WorkflowComponent("ORDER")
public class OrderWorkflow {

    private final ExternalClient externalClient;

    private static final ActivityOptions ACTIVITY_OPTIONS = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(1))
            .setRetryPolicy(RetryPolicy.newBuilder()
                    .setMaxAttempts(15)
                    .setInitialInterval(Duration.ofSeconds(1))
                    .setMaxInterval(Duration.ofSeconds(30))
                    .setBackoffCoefficient(2.0)
                    .addNoRetry(IllegalArgumentException.class)
                    .build())
            .build();

    private final ExternalClient externalClientStub = Workflow.newActivityStub(
            ExternalClient.class,
            ACTIVITY_OPTIONS
    );

    public OrderWorkflow(ExternalClient externalClient) {
        this.externalClient = externalClient;
    }

    public void run(ReserveRequest request) {
        // Способ 1: функциональный вызов с input + Function
        ReserverResponse reserved = Workflow.activity(
                "RESERVE",
                ACTIVITY_OPTIONS,
                request,
                req -> externalClient.reserve(req)
        );

        // Способ 2: функциональный вызов как Runnable (side-effect)
        Workflow.activity(
                "CHARGE",
                ACTIVITY_OPTIONS,
                () -> externalClient.charge(reserved)
        );

        // Способ 3: через typed-interface stub (JDK Proxy)
        externalClientStub.deliver();
    }
}
