package com.training.license.sharing.dto;

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
    private Integer totalCosts2023;
    private Integer deltaTotalCosts2023;
    private Integer software;
    private Integer deltaSoftware;
    private Integer trainings;
    private Integer deltaTrainings;

    @Singular("costPerMonth")
    private List<MonthCostDTO> costsPerMonth;
}
