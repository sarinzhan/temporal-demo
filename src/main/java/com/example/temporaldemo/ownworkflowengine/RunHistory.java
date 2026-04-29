package com.example.temporaldemo.ownworkflowengine;

import java.time.LocalDateTime;

public class RunHistory {
    public LocalDateTime startedAt;
    public Duration duration;
    private WorkflowEntity workflow;
    private Boolean isSuccess;
}
