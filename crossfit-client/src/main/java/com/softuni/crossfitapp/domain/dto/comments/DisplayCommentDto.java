package com.softuni.crossfitapp.domain.dto.comments;

import com.softuni.crossfitapp.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DisplayCommentDto {
    private String authorFirstName;
    private String authorLastName;
    private String authorUsername;
    private String authorImageUrl;
    private String text;
    private LocalDate date;
    private  int likes;

    private int dislikes;

    private UUID uuid;

    private Long id;
}
