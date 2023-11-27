package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.LicenseCredential;
import com.training.license.sharing.entities.LicenseCredentialKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseCredentialRepository extends JpaRepository<LicenseCredential, LicenseCredentialKey> {
}
