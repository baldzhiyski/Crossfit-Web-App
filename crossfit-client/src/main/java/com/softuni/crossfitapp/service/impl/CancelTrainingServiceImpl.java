package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.domain.events.CancelledTrainingEvent;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.service.CancelTrainingService;
import com.softuni.crossfitapp.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CancelTrainingServiceImpl implements CancelTrainingService {
    private EmailService emailService;

    private WeeklyTrainingRepository weeklyTrainingRepository;

    public CancelTrainingServiceImpl(EmailService emailService, WeeklyTrainingRepository weeklyTrainingRepository) {
        this.emailService = emailService;
        this.weeklyTrainingRepository = weeklyTrainingRepository;
    }


    @Override
    @EventListener(CancelledTrainingEvent.class)
    public void cancelTraining(CancelledTrainingEvent cancelledTrainingEvent) {
        WeeklyTraining weeklyTraining = this.weeklyTrainingRepository.findById(cancelledTrainingEvent.getWeeklyTrainingId()).orElseThrow(() -> new ObjectNotFoundException("No such weekly training !"));
        emailService.sendCancelledTrainingEmail(cancelledTrainingEvent.getCoachFullName(),weeklyTraining,cancelledTrainingEvent.getUserEmail(),cancelledTrainingEvent.getUserFullName() );
    }
}
