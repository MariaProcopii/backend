package com.training.license.sharing.dto;

import com.training.license.sharing.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class CredentialDTO {

    @Schema(description = "username", example = "john.doe@endava.com")
    @NotEmpty(message = "Email should not be empty")
    @Pattern(regexp = "[a-zA-Z0-9]+\\.[a-zA-Z0-9]+@endava.com", message = "It must be @endava.com email")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9!@#$%^&*()-_+=<>?]).{5,20}$",
            message = "Password might have alphanumerical and special symbols with the size between 5 and 20 symbols")
    private String password;

    @Schema(description = "role", example = "USER")
    @NotNull(message = "Role should not be null")
    @Enumerated(EnumType.STRING)
    private Role role;

}
