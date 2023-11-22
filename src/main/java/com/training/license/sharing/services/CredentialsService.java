package com.training.license.sharing.services;

import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialsService {
    private final CredentialRepository credentialRepository;

    public Optional<Credential> findById(long id){
        return credentialRepository.findById(id);
    }

    public Credential saveCredential(Credential credential){
        return credentialRepository.save(credential);
    }

}
