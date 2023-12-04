package com.training.license.sharing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UsersOverviewDTO {

    @Schema(description = "total users", example = "5")
    private int totalUsers;

    @Schema(description = "total disciplines", example = "1")
    private int totalDisciplines;

    @Schema(description = "delta users", example = "0")
    private int deltaUsers;

    private List<DisciplineUserCountDTO> disciplines;
}
