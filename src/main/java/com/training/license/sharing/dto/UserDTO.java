package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.DeliveryUnit;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Position;
import com.training.license.sharing.entities.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long Id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotNull(message = "Position should not be null")
    @Enumerated(EnumType.STRING)
    private Position position;

    @NotNull(message = "Discipline should not be null")
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    @NotNull(message = "Delivery unit should not be null")
    @Enumerated(EnumType.STRING)
    private DeliveryUnit du;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer lastActive;

    @Valid
    CredentialDTO credential;
}
