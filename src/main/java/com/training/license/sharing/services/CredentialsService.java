package com.training.license.sharing.services;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.util.InfoMessageUtil.FIND_BY_USERNAME;
import static com.training.license.sharing.util.InfoMessageUtil.FIND_BY_USERNAME_AND_PASSWORD;
import static com.training.license.sharing.util.InfoMessageUtil.IF_CREDENTIAL_EXISTS;
import static com.training.license.sharing.util.InfoMessageUtil.SAVE_CREDENTIAL;

@Service
@RequiredArgsConstructor
@Log4j2
public class CredentialsService {
    private final CredentialRepository credentialRepository;

    public Credential saveCredential(Credential credential) {
        log.info(SAVE_CREDENTIAL, credential.getId());
        return credentialRepository.save(credential);
    }

    public Optional<Credential> findByUsername(String string) {
        log.info(FIND_BY_USERNAME, string);
        return credentialRepository.findByUsername(string);
    }

    public boolean existsByUsernameAndPassword(String username, String password) {
        log.info(IF_CREDENTIAL_EXISTS, username, password);
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
        log.info(FIND_BY_USERNAME_AND_PASSWORD, username, password);
        return credentialRepository.findByUsernameAndPassword(username, password);
    }

}
