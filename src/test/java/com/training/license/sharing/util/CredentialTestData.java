package com.training.license.sharing.util;

import com.training.license.sharing.dto.CredentialDTO;
import com.training.license.sharing.entities.enums.Role;

public class CredentialTestData {
    public static final String EMAIL_TEST = "john.doe@endava.com";
    public static final String PASSWORD_TEST = "johndoe";

    public static CredentialDTO getGenerateCredential(String email, String password, Role role) {
        return new CredentialDTO().toBuilder()
                .username(email)
                .password(password)
                .role(role)
                .build();
    }
}
