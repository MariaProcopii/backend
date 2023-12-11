package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.license.sharing.entities.enums.Discipline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RequestDTO {

    @Schema(description = "username", example = "John Doe")
    @NotNull(message = "Username should not be null")
    @NotBlank(message = "Username should not be blank")
    private String username;

    @Schema(description = "discipline", example = "DEVELOPMENT")
    @NotNull(message = "discipline should not be null")
    private Discipline discipline;

    @Schema(description = "start of use", example = "06-Jun-2023")
    @NotNull(message = "Start of use date should not be null")
    @JsonFormat(pattern = "dd-MMM-yyyy")
    private LocalDate startOfUse;

    @Schema(description = "app", example = "JetBrains")
    @NotBlank(message = "App should not be blank")
    @NotNull(message = "App should not be null")
    private String app;
}
