package com.example.temporaldemo.ownworkflowengine;

import com.beeline.workflow.spring.api.WorkflowClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/workflows")
@Slf4j
public class WorkflowController {

    private final WorkflowClient workflowClient;

    public WorkflowController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @PostMapping("/order")
    public ResponseEntity<Map<String, Long>> startOrder(@RequestBody ReserveRequest request) {
        Long workflowId = workflowClient.startWorkflow("ORDER", request);
        log.info("Started ORDER workflow {}", workflowId);
        return ResponseEntity.ok(Map.of("workflowId", workflowId));
    }
}
