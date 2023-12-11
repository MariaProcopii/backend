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
public class DisciplineCostDTO {

    @Schema(description = "discipline name", example = "CREATIVE_SERVICES")
    private String disciplineName;

    @Schema(description = "average costs user discipline", example = "1999")
    private Integer averageCostsUserDiscipline;
}
