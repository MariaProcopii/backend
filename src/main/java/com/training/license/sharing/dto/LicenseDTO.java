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
public class LicenseDTO {
    private String name;
    private Integer cost;
    private Integer availability;
    private Integer unusedPeriod;
}