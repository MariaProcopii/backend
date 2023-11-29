package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    private String name;
    private Double cost;
    private Integer availability;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
}
