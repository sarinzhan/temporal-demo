package com.example.temporaldemo.ownworkflowengine;

import lombok.Data;

@Data
public class RetryConfig {
    private int maxAttempts;
    private boolean exponentialBackoff;
    private int retryIntervalMinutes;

    public static RetryConfig defaults() {
        RetryConfig config = new RetryConfig();
        config.setMaxAttempts(5);
        config.setExponentialBackoff(true);
        config.setRetryIntervalMinutes(1);
        return config;
    }
}
