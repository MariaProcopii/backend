package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    @Query("SELECT l " +
            "FROM License l " +
            "WHERE l.licenseName = :name " +
            "AND l.activationDate = :startOfUse")
    List<License> findByLicenseName(@Param("name") String name, @Param("startOfUse") LocalDate startOfUse);

    @Query("SELECT COUNT(*) " +
            "FROM UserLicense ul " +
            "WHERE ul.license.id = :#{#license.id}")
    Long findNumberOfUsersByLicense(@Param("license") License license);
}
