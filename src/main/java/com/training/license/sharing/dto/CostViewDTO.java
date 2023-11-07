package com.training.license.sharing.dto;

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
public class CostViewDTO {
    private Integer totalCosts2022;
    private Integer deltaTotalCosts2022;
    private Integer software;
    private Integer deltaSoftware;
    private Integer trainings;
    private Integer deltaTrainings;
}
