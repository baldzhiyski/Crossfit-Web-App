package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column
    @NotNull
    private Date bornOn;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String telephoneNumber;

    @Column
    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Membership membership;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinTable(
            name = "participants_trainings",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private List<WeeklyTraining> trainingsPerWeekList;


    @Column
    private LocalDate membershipStartDate;

    @Column
    private LocalDate membershipEndDate;

    @Column
    private boolean isActive;


    @OneToMany(mappedBy = "creator",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private Set<Event> events;

    @PrePersist
    public void encodePassword() {
        if (this.password != null) {
            this.password = new BCryptPasswordEncoder().encode(this.password);
        }
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    // Add a method to set membership duration based on selected membership type
    public void setMembershipDuration(MembershipType membershipType) {
        LocalDate currentDate = LocalDate.now();
        this.membershipStartDate = currentDate;

        // Calculate membership end date based on the selected membership type
        switch (membershipType) {
            case BASIC, PREMIUM,ELITE:
                this.membershipEndDate = currentDate.plusMonths(1);
                break;
            case UNLIMITED:
                this.membershipEndDate = currentDate.plusMonths(3);
                break;
            default:
                throw new ObjectNotFoundException("Unsupported Membership Type !");
        }
    }

}
