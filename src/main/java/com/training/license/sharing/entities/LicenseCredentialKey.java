package com.training.license.sharing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class LicenseCredentialKey implements Serializable {
    @Column(name = "id_credential")
    private Integer credentialId;

    @Column(name = "id_license")
    private Integer licenseId;

}
