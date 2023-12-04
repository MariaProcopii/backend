package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRequestDTO {

    @Schema(description = "request id", example = "2")
    private Long requestId;

    @Schema(description = "status", example = "PENDING")
    private RequestStatus status;

    @Schema(description = "app", example = "JetBrains")
    private String app;

    @Schema(description = "request date", example = "08-Dec-2023 00:00")
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
    private LocalDateTime requestDate;

    @Schema(description = "start of use", example = "06-Jun-2023")
    @JsonFormat(pattern = "dd-MMM-yyyy")
    private LocalDate startOfUse;

    @Schema(description = "username", example = "Jane Smith")
    private String username;

    @Schema(description = "discipline", example = "CREATIVE_SERVICES")
    private Discipline discipline;
}
