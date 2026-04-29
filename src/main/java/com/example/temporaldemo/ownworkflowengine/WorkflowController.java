package com.example.temporaldemo.ownworkflowengine;

import com.beeline.temporalmini.WorkflowEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/workflows")
@Slf4j
public class WorkflowController {

    private final WorkflowEngine workflowEngine;
    private final ObjectMapper objectMapper;

    public WorkflowController(WorkflowEngine workflowEngine, ObjectMapper objectMapper) {
        this.workflowEngine = workflowEngine;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/order")
    public ResponseEntity<Long> startOrder(@RequestBody ReserveRequest request) {
        try {
            Long workflowId = workflowEngine.start("ORDER", objectMapper.writeValueAsString(request));
            return ResponseEntity.ok(workflowId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start workflow", e);
        }
    }
}
