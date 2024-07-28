package com.softuni.crossfitcommunityevents.service.impl;

import com.softuni.crossfitcommunityevents.service.MonitoringService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    private final Counter eventsSearchCounter;

    public MonitoringServiceImpl(MeterRegistry meterRegistry) {

        eventsSearchCounter = Counter
                .builder("events.searches")
                .description("How many times events were searched.")
                .register(meterRegistry);
    }
    @Override
    public void increaseEventsSearch() {
        eventsSearchCounter.increment();
    }
}
