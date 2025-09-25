package com.example.SpringBootTask.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class CustomMetrics {

    private final MeterRegistry meterRegistry;

    private final AtomicInteger successfulLogins = new AtomicInteger(0);

    private final AtomicInteger failedLogins = new AtomicInteger(0);

    private final AtomicInteger activeSessions = new AtomicInteger(0);

    @PostConstruct
    public void registerMetrics() {
        Counter.builder("custom_successful_logins_total")
                .description("Total number of successful logins")
                .register(meterRegistry);

        Counter.builder("custom_failed_logins_total")
                .description("Total number of failed logins")
                .register(meterRegistry);

        Gauge.builder("custom_active_sessions", activeSessions, AtomicInteger::get)
                .description("Current number of active sessions")
                .register(meterRegistry);
    }

    public void incrementSuccessfulLogins() {
        successfulLogins.incrementAndGet();
        meterRegistry.counter("custom_successful_logins_total").increment();
    }

    public void incrementFailedLogins() {
        failedLogins.incrementAndGet();
        meterRegistry.counter("custom_failed_logins_total").increment();
    }

    public void setActiveSessions(int count) {
        activeSessions.set(count);
    }
}
