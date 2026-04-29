package com.example.temporaldemo.ownworkflowengine;

import lombok.Data;

@Data
public class ReserverResponse {
    private String reservationId;
    private String orderId;
    private double amount;
}
