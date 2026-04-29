package com.example.temporaldemo.ownworkflowengine;

import lombok.Data;

@Data
public class ReserveRequest {
    private String orderId;
    private String userId;
    private double amount;
}
