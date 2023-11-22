package com.training.license.sharing.util;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.entities.Credential;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CredentialConverter {
    private ModelMapper modelMapper;

    public CredentialDTO convertToDTO(Credential credential){
        return modelMapper.map(credential, CredentialDTO.class);
    }

    public Credential convertToCredential(CredentialDTO credentialDTO){
        return modelMapper.map(credentialDTO, Credential.class);
    }
}
