package com.example.temporaldemo.ownworkflowengine;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkflowAttempt {
    private Long id;
    private Long workflowId;
    private String step;
    private int attemptNumber;
    private LocalDateTime executedAt;
    private boolean success;
    private String errorMessage;
    private String inputPayload;
    private String outputPayload;
}
