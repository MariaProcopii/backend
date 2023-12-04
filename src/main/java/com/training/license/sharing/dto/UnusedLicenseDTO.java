package com.training.license.sharing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UnusedLicenseDTO {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "name", example = "Postman")
    private String name;

    @Schema(description = "cost", example = "666.0")
    private Double cost;

    @Schema(description = "unused period", example = "13")
    private Integer unusedPeriod;
}
