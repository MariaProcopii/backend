package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.LicenseType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewLicenseDTO {

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{3,30}",
            message = "License name must be alphanumerical with special symbols and the size between 3 and 30 letters!")
    private String licenseName;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{5,30}",
            message = "Website name must be alphanumerical with special symbols and the size between 5 and 30 letters!")
    private String website;

    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{5,250}",
            message = "Description must be alphanumerical with special symbols and the size between 5 and 250 letters!")
    private String description;

    @JsonProperty("licenseType")
    private LicenseType typeOfLicense;

    private String logo;

    @Size(min = 1)
    @NotNull
    private List<CredentialDTO> credentials;

    @NotNull
    @Max(100000)
    @Min(1)
    private Double cost;

    @NotNull
    private Currency currency;

    @NotNull
    @Min(25)
    private Integer availability;

    @NotNull
    private Boolean isRecurring;

    @NotNull
    @JsonFormat(pattern = "dd-MMM-yyyy")
    @JsonProperty("expiresOn")
    private LocalDate activationDate;

    @NotNull
    @Max(250)
    @Min(20)
    private Integer seats;

}

