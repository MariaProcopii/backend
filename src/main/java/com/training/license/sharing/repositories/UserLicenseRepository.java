package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.UserLicense;
import com.training.license.sharing.entities.UserLicenseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLicenseRepository extends JpaRepository<UserLicense, UserLicenseKey> {
}
