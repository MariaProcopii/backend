package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    boolean existsByUsernameAndPassword(String username, String password);
    Credential findByUsernameAndPassword(String username, String password);

    Optional<Credential> findByUsername(String string);
}
