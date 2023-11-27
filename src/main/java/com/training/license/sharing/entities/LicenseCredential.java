package com.training.license.sharing.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCredential {

    @EmbeddedId
    private LicenseCredentialKey id;

    @ManyToOne
    @JoinColumn(name = "id_credential", insertable = false, updatable = false)
    private Credential credential;

    @ManyToOne
    @JoinColumn(name = "id_license", insertable = false, updatable = false)
    private License license;

}
