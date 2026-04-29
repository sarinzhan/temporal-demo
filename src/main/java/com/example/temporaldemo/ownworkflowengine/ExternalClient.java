package com.example.temporaldemo.ownworkflowengine;

public interface ExternalClient {
    ReserverResponse reserve(ReserveRequest request);
}
