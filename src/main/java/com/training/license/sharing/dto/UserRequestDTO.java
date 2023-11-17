package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.RequestStatus;
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

    private Long requestId;

    private RequestStatus status;

    private String app;

    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
    private LocalDateTime requestDate;

    @JsonFormat(pattern = "dd-MMM-yyyy")
    private LocalDate startOfUse;

    private String username;

    private Discipline discipline;
}
