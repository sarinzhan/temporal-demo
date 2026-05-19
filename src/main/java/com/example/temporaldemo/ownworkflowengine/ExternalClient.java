package com.example.temporaldemo.ownworkflowengine;

import com.beeline.workflow.core.annotation.Activity;

@Activity
public interface ExternalClient {
    ReserverResponse reserve(ReserveRequest request);
    void charge(ReserverResponse reserved);
    void deliver();
}
