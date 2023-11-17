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
public class AverageUserCostViewDTO {
    private Integer calculation;
    private String disciplineName;
    private Integer averageCostsUserDiscipline;
}
