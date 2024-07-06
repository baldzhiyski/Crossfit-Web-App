package com.softuni.crossfitapp.domain.dto.coaches;

import com.softuni.crossfitapp.domain.entity.Certificate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record CoachDisplayDto (
     String firstName,

     String lastName,

     List<String> certificatesNames,

     String imageUrl,

     String email){
}
