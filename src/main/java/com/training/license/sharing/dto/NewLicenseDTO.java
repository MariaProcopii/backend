package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private Long licenseId;

    @Schema(description = "license name", example = "Sample License 1")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{3,30}",
            message = "License name must be alphanumerical with special symbols and the size between 3 and 30 letters!")
    private String licenseName;

    @Schema(description = "website", example = "www.sambplelicense.com")
    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{5,30}",
            message = "Website name must be alphanumerical with special symbols and the size between 5 and 30 letters!")
    private String website;

    @Schema(description = "description", example = "This is a sample license description with a call-to-action.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{5,250}",
            message = "Description must be alphanumerical with special symbols and the size between 5 and 250 letters!")
    private String description;

    @Schema(description = "type of license", example = "TRAINING")
    @JsonProperty("licenseType")
    private LicenseType typeOfLicense;

    @Schema(description = "logo", example = "[100, 101, 102, 97, 117, 108, 116, 45, 108, 111, 103, 111, 45, 98, 97, 115, 101, 54, 52, 45, 118, 97, 108, 117, 101]")
    private String logo;

    @Size(min = 1)
    @NotNull
    private List<CredentialDTO> credentials;

    @Schema(description = "cost", example = "6.0")
    @NotNull
    @Max(100000)
    @Min(1)
    private Double cost;

    @Schema(description = "currency", example = "USD")
    @NotNull
    private Currency currency;

    @Schema(description = "availability", example = "365")
    @NotNull
    @Min(25)
    private Integer availability;

    @Schema(description = "is recurring", example = "true")
    @NotNull
    private Boolean isRecurring;

    @Schema(description = "expires on", example = "12-May-2023")
    @NotNull
    @JsonFormat(pattern = "dd-MMM-yyyy")
    @JsonProperty("expiresOn")
    private LocalDate activationDate;

    @Schema(description = "seatsTotal", example = "10")
    @Max(250)
    @Min(20)
    private Integer seatsTotal;

    @Schema(description = "seatsAvailable", example = "2")
    @Max(250)
    @Min(20)
    private Integer seatsAvailable;

}
