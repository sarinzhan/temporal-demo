package com.example.temporaldemo.ownworkflowengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class ExternalClientImpl implements ExternalClient {

    private static final double FAILURE_RATE = 0.6;
    private final Random random = new Random();

    @Override
    public ReserverResponse reserve(ReserveRequest request) {
        failRandomly("reserve");
        ReserverResponse response = new ReserverResponse();
        response.setReservationId(UUID.randomUUID().toString());
        response.setOrderId(request.getOrderId());
        response.setAmount(request.getAmount());
        log.debug("reserve: ok, reservationId={}", response.getReservationId());
        return response;
    }

    @Override
    public void charge(ReserverResponse reserved) {
        failRandomly("charge");
        log.debug("charge: ok, reservationId={}", reserved.getReservationId());
    }

    @Override
    public void deliver() {
        failRandomly("deliver");
        log.debug("deliver: ok");
    }

    private void failRandomly(String method) {
        if (random.nextDouble() < FAILURE_RATE) {
            throw new ExternalClientUnavailableException(method + " service unavailable");
        }
    }
}
