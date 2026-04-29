package com.example.temporaldemo.ownworkflowengine;

public interface Workflow {
    String type();
    String initialStep();
    void run(Long workflowId);
}
