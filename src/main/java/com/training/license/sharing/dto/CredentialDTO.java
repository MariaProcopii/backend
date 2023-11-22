package com.training.license.sharing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.license.sharing.entities.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CredentialDTO {
    @Email
    @Pattern(regexp = "[a-zA-Z0-9]+\\.[a-zA-Z0-9]+@endava.com")
    @NotEmpty
    @JsonProperty("email")
    private String username;

    @NotEmpty
    @Column(name = "password")
    @Length(min = 5, max = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

}
