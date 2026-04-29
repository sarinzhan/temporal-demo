package com.example.temporaldemo.ownworkflowengine;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkflowRepository {
    WorkflowEntity findById(Long id);
    void save(WorkflowEntity workflowEntity);
    List<WorkflowEntity> findPendingWorkflows(LocalDateTime now);
    void saveAttempt(WorkflowAttempt attempt);
}
