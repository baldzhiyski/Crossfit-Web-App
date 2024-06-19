package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.Role;
import com.softuni.crossfitapp.vallidation.annotations.ValidEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{
    @Column(nullable = false,unique = true)
    private String username;

    @Column
    @NotBlank
    private String firstName;

    @Column
    @NotBlank
    private String lastName;

    @Column
    @NotNull
    private String address;

    @Column
    @NotBlank
    private String nationality;

    @Column
    @NotBlank
    private String town;

    @Column
    @NotNull
    private Date bornOn;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false,unique = true)
    private String telephoneNumber;

    @ValidEmail
    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "participants_trainings",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private List<WeeklyTraining> trainingsPerWeekList;


    @ManyToOne
    private Membership membership;

    @Column
    private LocalDate membershipStartDate;

    @Column
    private LocalDate membershipEndDate;

    // Add a method to set membership duration based on selected membership type
    public void setMembershipDuration(MembershipType membershipType) {
        LocalDate currentDate = LocalDate.now();
        this.membershipStartDate = currentDate;

        // Calculate membership end date based on the selected membership type
        switch (membershipType) {
            case BASIC:
                this.membershipEndDate = currentDate.plusMonths(1);
                break;
            case PREMIUM:
                this.membershipEndDate = currentDate.plusMonths(3);
                break;
            case ELITE:
                this.membershipEndDate = currentDate.plusMonths(6);
                break;
            default:
                // Handle unknown membership type
                break;
        }
    }

}
