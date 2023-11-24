package com.training.license.sharing.entities;

import com.training.license.sharing.entities.enums.Currency;
import com.training.license.sharing.entities.enums.LicenseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "license_name")
    private String licenseName;

    @NotNull
    @Column(name = "cost")
    private Double cost;

    @NotNull
    @Column(name = "availability")
    private Integer availability;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type")
    private LicenseType licenseType;

    @NotNull
    @Column(name = "unused_period")
    private Integer unusedPeriod;

    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    @Column(name = "activation_date")
    private LocalDate activationDate;

    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Lob
    @Column(name = "logo", columnDefinition = "bytea")
    private byte[] logo;

    @Column(name = "website")
    private String website;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "seats_available")
    private Integer seatsAvailable;

    @Column(name = "seats_total")
    private Integer seatsTotal;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    @Column(name = "creating_date")
    private LocalDate creatingDate;

}