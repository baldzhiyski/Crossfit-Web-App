package com.softuni.crossfittrainings.web;

import com.softuni.crossfittrainings.model.Training;
import com.softuni.crossfittrainings.model.dto.TrainingDTO;
import com.softuni.crossfittrainings.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/crossfit-api")
public class TrainingsRestController {

    private TrainingService trainingService;

    @Autowired
    public TrainingsRestController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }


    @GetMapping("/get-all-trainings")
    public ResponseEntity<Set<TrainingDTO>> getAllTrainings(){
        return ResponseEntity.ok(
                trainingService.getAllTrainings()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<TrainingDTO> findById(@PathVariable("id") UUID id) {
        return ResponseEntity
                .ok(trainingService.getTrainingById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TrainingDTO> deleteById(@PathVariable("id") UUID id) {
        trainingService.deleteOffer(id);
        return ResponseEntity.ok().build();
    }
}
