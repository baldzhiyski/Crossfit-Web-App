package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Set;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role  extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;

    public Role(RoleType roleType){
        this.roleType = roleType;
    }

}
