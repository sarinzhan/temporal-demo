package com.example.temporaldemo.ownworkflowengine;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WorkflowEngine {

    private final Map<String, Workflow> registry;
    private final WorkflowRepository workflowRepository;

    public WorkflowEngine(List<Workflow> workflows, WorkflowRepository workflowRepository) {
        this.registry = workflows.stream().collect(Collectors.toMap(Workflow::type, Function.identity()));
        this.workflowRepository = workflowRepository;
    }

    public Long start(String workflowType, String initialPayload, RetryConfig retryConfig) {
        Workflow workflow = registry.get(workflowType);
        if (workflow == null) {
            throw new IllegalArgumentException("Unknown workflow type: " + workflowType);
        }
        WorkflowEntity entity = new WorkflowEntity();
        entity.setWorkflowType(workflowType);
        entity.setCurrentStep(workflow.initialStep());
        entity.setNextPayload(initialPayload);
        entity.setState(WorkflowState.NEW);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setMaxAttempts(retryConfig.getMaxAttempts());
        entity.setExponentialBackoff(retryConfig.isExponentialBackoff());
        entity.setRetryIntervalMinutes(retryConfig.getRetryIntervalMinutes());
        workflowRepository.save(entity);
        return entity.getId();
    }

    public void run(Long workflowId) {
        WorkflowEntity entity = workflowRepository.findById(workflowId);
        entity.setState(WorkflowState.RUNNING);
        entity.setStartedAt(LocalDateTime.now());
        workflowRepository.save(entity);

        Workflow workflow = registry.get(entity.getWorkflowType());
        workflow.run(workflowId);
    }
}
