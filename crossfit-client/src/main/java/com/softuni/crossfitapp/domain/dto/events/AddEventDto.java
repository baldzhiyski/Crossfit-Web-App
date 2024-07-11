package com.softuni.crossfitapp.domain.dto.events;

import com.softuni.crossfitapp.vallidation.annotations.NotPastDate;
import com.softuni.crossfitapp.vallidation.annotations.ValidYouTubeUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class AddEventDto {
    @NotBlank(message = "{field.required}")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotPastDate
    private Date date;

    @NotBlank(message = "{field.required}")
    private String address;

    @NotBlank(message = "{field.required}")
    private String eventName;

    @ValidYouTubeUrl
    private String videoUrl;

}
