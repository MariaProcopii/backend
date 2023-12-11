package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.DeliveryUnit;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Position;
import com.training.license.sharing.entities.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "id", example = "1")
    private Long Id;

    @Schema(description = "name", example = "John Doe")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Schema(description = "position", example = "DEVELOPER")
    @NotNull(message = "Position should not be null")
    @Enumerated(EnumType.STRING)
    private Position position;

    @Schema(description = "discipline", example = "DEVELOPMENT")
    @NotNull(message = "Discipline should not be null")
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    @Schema(description = "du", example = "MDD")
    @NotNull(message = "Delivery unit should not be null")
    @Enumerated(EnumType.STRING)
    private DeliveryUnit du;

    @Schema(description = "status", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "last active", example = "100")
    private Integer lastActive;

    @Valid
    CredentialDTO credential;
}
