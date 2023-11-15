package com.training.license.sharing.entities;

import com.training.license.sharing.entities.enums.DeliveryUnit;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Position;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.entities.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline")
    private Discipline discipline;

    @Enumerated(EnumType.STRING)
    @Column(name = "du")
    private DeliveryUnit du;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "last_active")
    private Integer lastActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Email
    @NotEmpty
    @Column(unique = true)
    private String email;
}
