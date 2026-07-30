package com.library.management.config;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TimeProvider {

    private Clock clock;

    public TimeProvider() {
        this.clock = Clock.systemUTC();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public LocalDate today() {
        return LocalDate.now(clock);
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public Clock getClock() {
        return clock;
    }
}

