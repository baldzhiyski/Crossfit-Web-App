package com.softuni.crossfitapp.domain.dto.comments;

import com.softuni.crossfitapp.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DisplayCommentDto {
    private User author;
    private String text;
    private LocalDate date;
}