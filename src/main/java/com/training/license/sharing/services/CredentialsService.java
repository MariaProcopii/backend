package com.training.license.sharing.services;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialsService {
    private final CredentialRepository credentialRepository;

    public Credential saveCredential(Credential credential) {
        return credentialRepository.save(credential);
    }

    public Optional<Credential> findByUsername(String string) {
        return credentialRepository.findByUsername(string);
    }

    public boolean existsByUsernameAndPassword(String username, String password) {
        return credentialRepository.existsByUsernameAndPassword(username, password);
    }

    public List<Credential> findByDTOs(List<CredentialDTO> credentialDTOS) {
        return credentialDTOS.stream()
                .map(this::findByDTO)
                .toList();
    }

    private Credential findByDTO(CredentialDTO credentialDTO) {
        return findByUsernameAndPassword(credentialDTO.getUsername(), credentialDTO.getPassword());
    }

    public Credential findByUsernameAndPassword(String username, String password) {
        return credentialRepository.findByUsernameAndPassword(username, password);
    }

}
