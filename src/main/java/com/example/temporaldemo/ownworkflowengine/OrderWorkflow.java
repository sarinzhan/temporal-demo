package com.example.temporaldemo.ownworkflowengine;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class OrderWorkflow implements Workflow {

    private final WorkflowRepository workflowRepository;
    private final ExternalClient externalClient;
    private final ObjectMapper objectMapper;

    public OrderWorkflow(WorkflowRepository workflowRepository, ExternalClient externalClient, ObjectMapper objectMapper) {
        this.workflowRepository = workflowRepository;
        this.externalClient = externalClient;
        this.objectMapper = objectMapper;
    }

    public enum States {
        RESERVE,
        CHARGE,
        DELIVER
    }

    @Override
    public String type() {
        return "ORDER";
    }

    @Override
    public String initialStep() {
        return States.RESERVE.name();
    }

    @Override
    public void run(Long workflowId) {
        WorkflowEntity workFlow = workflowRepository.findById(workflowId);
        States state = States.valueOf(workFlow.getNextStep());
        switch (state) {
            case RESERVE -> {
                ReserveRequest input = new ReserveRequest();
                try {
                    ReserverResponse response = externalClient.reserve(input);
                    if (response == null) {
                        saveAttempt(workFlow, States.RESERVE, null, null, true, null);
                        workFlow.setState(WorkflowState.FINISHED);
                        workflowRepository.save(workFlow);
                        return;
                    }
                    String outputPayload = objectMapper.writeValueAsString(response);
                    saveAttempt(workFlow, States.RESERVE, null, outputPayload, true, null);
                    workFlow.setNextPayload(outputPayload);
                    workFlow.setNextStep(States.CHARGE.name());
                    workflowRepository.save(workFlow);
                } catch (Exception ex) {
                    saveAttempt(workFlow, States.RESERVE, null, null, false, ex.getMessage());
                    handleRetry(workFlow, ex);
                }
            }
            case CHARGE -> {
                String inputPayloadRaw = workFlow.getNextPayload();
                try {
                    ReserverResponse reserverResponse = objectMapper.readValue(inputPayloadRaw, ReserverResponse.class);
                    // TODO: логика charge с использованием prevResponse
                    saveAttempt(workFlow, States.CHARGE, inputPayloadRaw, null, true, null);
                    workFlow.setNextStep(States.DELIVER.name());
                    workflowRepository.save(workFlow);
                } catch (Exception ex) {
                    saveAttempt(workFlow, States.CHARGE, inputPayloadRaw, null, false, ex.getMessage());
                    handleRetry(workFlow, ex);
                }
            }
            case DELIVER -> {
                try {
                    // TODO: логика deliver
                    saveAttempt(workFlow, States.DELIVER, null, null, true, null);
                    workFlow.setState(WorkflowState.FINISHED);
                    workflowRepository.save(workFlow);
                } catch (Exception ex) {
                    saveAttempt(workFlow, States.DELIVER, null, null, false, ex.getMessage());
                    handleRetry(workFlow, ex);
                }
            }
        }
    }

    private void saveAttempt(WorkflowEntity workFlow, States step, String inputPayload,
                             String outputPayload, boolean success, String errorMessage) {
        WorkflowAttempt attempt = new WorkflowAttempt();
        attempt.setWorkflowId(workFlow.getId());
        attempt.setStep(step.name());
        attempt.setAttemptNumber(workFlow.getAttempt());
        attempt.setExecutedAt(LocalDateTime.now());
        attempt.setSuccess(success);
        attempt.setInputPayload(inputPayload);
        attempt.setOutputPayload(outputPayload);
        attempt.setErrorMessage(errorMessage);
        workflowRepository.saveAttempt(attempt);
    }

    private void handleRetry(WorkflowEntity workFlow, Exception ex) {
        int attempt = workFlow.getAttempt() + 1;
        workFlow.setAttempt(attempt);
        workFlow.setErrorMessage(ex.getMessage());
        if (attempt >= workFlow.getMaxAttempts()) {
            workFlow.setState(WorkflowState.FAILED);
        } else if (workFlow.isExponentialBackoff()) {
            workFlow.setNextRetryAt(LocalDateTime.now().plusMinutes((long) Math.pow(2, attempt) * workFlow.getRetryIntervalMinutes()));
        } else {
            workFlow.setNextRetryAt(LocalDateTime.now().plusMinutes(workFlow.getRetryIntervalMinutes()));
        }
        workflowRepository.save(workFlow);
    }
}
