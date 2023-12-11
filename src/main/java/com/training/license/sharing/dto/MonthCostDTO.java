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
public class MonthCostDTO {

    @Schema(description = "month", example = "Dec 23")
    private String month;

    @Schema(description = "value", example = "999")
    private Integer value;
}
