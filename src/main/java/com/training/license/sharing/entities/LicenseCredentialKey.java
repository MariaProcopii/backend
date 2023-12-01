package com.training.license.sharing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LicenseCredentialKey implements Serializable {
    @Column(name = "id_credential")
    private Integer credentialId;

    @Column(name = "id_license")
    private Integer licenseId;

}
