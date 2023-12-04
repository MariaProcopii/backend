package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.entities.enums.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Base64;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LicenseSummaryDTO {

    @Schema(description = "logo", example = "[100, 101, 102, 97, 117, 108, 116, 45, 108, 111, 103, 111, 45, 98, 97, 115, 101, 54, 52, 45, 118, 97, 108, 117, 101]")
    private String logo;

    @Schema(description = "license name", example = "Visual Studio")
    private String licenseName;

    @Schema(description = "description", example = "Visual Studio API")
    private String description;


    @Schema(description = "cost", example = "800.0")
    private Double cost;

    @Schema(description = "currency", example = "USD")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Schema(description = "license duration", example = "10")
    private Integer licenseDuration;

    @Schema(description = "duration unit", example = "MONTH")
    @Enumerated(EnumType.STRING)
    private DurationUnit durationUnit;

    @Schema(description = "seats available", example = "10")
    private Integer seatsAvailable;

    @Schema(description = "seats total", example = "250")
    private Integer seatsTotal;

    @Schema(description = "is active", example = "false")
    private Boolean isActive;

    @Schema(description = "expiration date", example = "2022-01-10")
    private LocalDate expirationDate;

    @Schema(description = "license type", example = "SOFTWARE")
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    @Schema(description = "is recurring", example = "false")
    private Boolean isRecurring;

    public void setLogo(byte[] logoBytes) {
        if (logoBytes != null && logoBytes.length > 0) {
            this.logo = Base64.getEncoder().encodeToString(logoBytes);
        } else {
            this.logo = null;
        }
    }

}
