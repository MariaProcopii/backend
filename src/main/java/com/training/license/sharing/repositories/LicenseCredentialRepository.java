package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.LicenseCredential;
import com.training.license.sharing.entities.LicenseCredentialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LicenseCredentialRepository extends JpaRepository<LicenseCredential, LicenseCredentialKey> {

    List<LicenseCredential> findAllByLicense(License license);

    @Modifying
    @Transactional
    @Query("DELETE FROM LicenseCredential lc " +
            "WHERE lc.license.id = :licenseId")
    void deleteAllByLicenseId(Long licenseId);

}
