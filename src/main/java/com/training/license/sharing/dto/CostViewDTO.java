package com.training.license.sharing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CostViewDTO {

    @Schema(description = "total costs for current year", example = "6427")
    private Integer totalCostsCurrentYear;

    @Schema(description = "delta total costs for  current year", example = "2227")
    private Integer deltaTotalCosts;

    @Schema(description = "software", example = "3")
    private Integer software;

    @Schema(description = "delta software", example = "1")
    private Integer deltaSoftware;

    @Schema(description = "trainings", example = "3")
    private Integer trainings;

    @Schema(description = "delta trainings", example = "1")
    private Integer deltaTrainings;

    @Singular("costPerMonth")
    private List<MonthCostDTO> costsPerMonth;
}
