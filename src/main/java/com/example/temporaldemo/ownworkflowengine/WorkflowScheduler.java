package com.example.temporaldemo.ownworkflowengine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@ConditionalOnProperty(name = "workflow.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class WorkflowScheduler {

    private final WorkflowEngine engine;
    private final WorkflowRepository workflowRepository;

    public WorkflowScheduler(WorkflowEngine engine, WorkflowRepository workflowRepository) {
        this.engine = engine;
        this.workflowRepository = workflowRepository;
    }

    @Scheduled(fixedDelayString = "${workflow.scheduler.interval-ms:5000}")
    public void poll() {
        List<WorkflowEntity> pending = workflowRepository.findPendingWorkflows(LocalDateTime.now());
        pending.forEach(w -> engine.run(w.getId()));
    }
}
