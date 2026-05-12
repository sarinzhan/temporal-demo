package com.example.temporaldemo.ownworkflowengine;

import com.beeline.temporalmini.RetryPolicy;
import com.beeline.temporalmini.Workflow;
import com.beeline.temporalmini.WorkflowContext;
import org.springframework.stereotype.Component;

@Component
public class OrderWorkflow implements Workflow {

    private final ExternalClient externalClient;

    public OrderWorkflow(ExternalClient externalClient) {
        this.externalClient = externalClient;
    }

    @Override
    public String type() {
        return "ORDER";
    }

    @Override
    public void run(WorkflowContext ctx) {
        ReserverResponse reserved = ctx.activity(
                "RESERVE", ReserverResponse.class,
                RetryPolicy.fixed(10, 10_000),
                () -> {
                    return externalClient.reserve(new ReserveRequest());
                }
        );

        ctx.activity(
                "CHARGE",
                RetryPolicy.fixed(10, 10_000),
                () -> externalClient.charge(reserved)
        );

        ctx.activity(
                "DELIVER",
                RetryPolicy.fixed(10, 10_000),
                externalClient::deliver
        );
    }
}
