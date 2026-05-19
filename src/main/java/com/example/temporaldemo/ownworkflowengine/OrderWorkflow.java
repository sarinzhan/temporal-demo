package com.example.temporaldemo.ownworkflowengine;

import com.beeline.workflow.core.annotation.WorkflowComponent;
import com.beeline.workflow.core.api.Workflow;
import com.beeline.workflow.core.config.ActivityOptions;
import com.beeline.workflow.core.config.RetryPolicy;

import java.time.Duration;

@WorkflowComponent("ORDER")
public class OrderWorkflow {

    private final ExternalClient externalClient;

    private final ExternalClient externalClientStub = Workflow.newActivityStub(
            ExternalClient.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(1))
                    .setRetryPolicy(RetryPolicy.fixed(10, 10_000))
                    .build()
    );

    public OrderWorkflow(ExternalClient externalClient) {
        this.externalClient = externalClient;
    }

    public void run(ReserveRequest request) {
        // Способ 1: функциональный вызов с input + Function
        ReserverResponse reserved = Workflow.activity(
                "RESERVE", request,
                RetryPolicy.fixed(10, 10_000),
                req -> externalClient.reserve(req)
        );

        // Способ 2: функциональный вызов как Runnable (side-effect)
        Workflow.activity(
                "CHARGE",
                RetryPolicy.fixed(10, 10_000),
                () -> externalClient.charge(reserved)
        );

        // Способ 3: через typed-interface stub (JDK Proxy)
        externalClientStub.deliver();
    }
}
