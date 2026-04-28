package com.example.temporaldemo.controller;

import com.example.temporaldemo.workflow.OrderWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowOptions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final WorkflowClient client;

    public OrderController(WorkflowClient client) {
        this.client = client;
    }

    @PostMapping("/{orderId}")
    public String startOrder(@PathVariable String orderId) {
        OrderWorkflow workflow = client.newWorkflowStub(
                OrderWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("ORDER_TASK_QUEUE")
                        .build()
        );

        WorkflowExecution execution = WorkflowClient.start(workflow::processOrder, orderId);
        return "Started workflow: " + execution.getWorkflowId() + " / run: " + execution.getRunId();
    }
}
