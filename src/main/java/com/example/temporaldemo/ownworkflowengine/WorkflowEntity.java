package com.example.temporaldemo.ownworkflowengine;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkflowEntity {
    private Long id;
    private String nextStep;
    private LocalDateTime startedAt;
    private LocalDateTime createdAt;
    private LocalDateTime nextRetryAt;
    private String nextPayload;
    private WorkflowState state;
    private int attempt;
    private String errorMessage;

    private int maxAttempts;
    private boolean exponentialBackoff;
    private int retryIntervalMinutes;
    private String workflowType;
}
