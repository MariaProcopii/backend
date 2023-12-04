package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.DurationUnit;
import com.training.license.sharing.entities.enums.LicenseType;
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

    private String logo;

    private String licenseName;

    private String description;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Integer licenseDuration;

    @Enumerated(EnumType.STRING)
    private DurationUnit durationUnit;

    private Integer seatsAvailable;

    private Integer seatsTotal;

    private Boolean isActive;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private Boolean isRecurring;

    public void setLogo(byte[] logoBytes) {
        if (logoBytes != null && logoBytes.length > 0) {
            this.logo = Base64.getEncoder().encodeToString(logoBytes);
        } else {
            this.logo = null;
        }
    }

}
