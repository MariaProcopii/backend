package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.license.sharing.entities.enums.Discipline;
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

    @NotNull(message = "Username should not be null")
    @NotBlank(message = "Username should not be blank")
    private String username;

    @NotNull(message = "discipline should not be null")
    private Discipline discipline;

    @NotNull(message = "Start of use date should not be null")
    @JsonFormat(pattern = "dd-MMM-yyyy")
    private LocalDate startOfUse;

    @NotBlank(message = "App should not be blank")
    @NotNull(message = "App should not be null")
    private String app;
}
