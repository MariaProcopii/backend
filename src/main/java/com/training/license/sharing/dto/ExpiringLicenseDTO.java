package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ExpiringLicenseDTO {

    @Schema(description = "id", example = "5")
    private Long id;

    @Schema(description = "name", example = "JetBrains")
    private String name;

    @Schema(description = "cost", example = "1444.0")
    private Double cost;

    @Schema(description = "availability", example = "120")
    private Integer availability;

    @Schema(description = "expiration date", example = "2023-10-04")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
}
