package com.training.license.sharing.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class LicenseCredential {
    @EmbeddedId
    private LicenseCredentialKey id;

    @ManyToOne
    @JoinColumn(name = "credential_id", insertable = false, updatable = false)
    private Credential credential;

    @ManyToOne
    @JoinColumn(name = "license_id", insertable = false, updatable = false)
    private License license;
}
