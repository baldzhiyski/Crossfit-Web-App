package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.MonitoringService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    private static MeterRegistry meterRegistry;
    private static final ConcurrentMap<String, Counter> trainingEnrollmentCounters = new ConcurrentHashMap<>();
    private static Counter blogReadersCounter;

    public MonitoringServiceImpl(MeterRegistry meterRegistry) {
        MonitoringServiceImpl.meterRegistry = meterRegistry;
        initializeCounters();
    }

    private static void initializeCounters() {
        blogReadersCounter = Counter
                .builder("blog.readers")
                .description("How many times users have read the blog.")
                .register(meterRegistry);
    }

    @Override
    public void increaseBlogReaders() {
        blogReadersCounter.increment();
    }

    @Override
    public void enrollUserInTraining(String trainingType) {
        Counter counter = trainingEnrollmentCounters.computeIfAbsent(trainingType, key ->
                Counter.builder("training.enrollments")
                        .tag("type", trainingType)
                        .description("Number of enrollments per training type")
                        .register(meterRegistry)
        );
        counter.increment();
    }
}

