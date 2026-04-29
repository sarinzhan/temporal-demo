package com.example.temporaldemo.ownworkflowengine;

public interface ExternalClient {
    ReserverResponse reserve(ReserveRequest request);
    void charge(ReserverResponse reserved);
    void deliver();
}
