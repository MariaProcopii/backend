package com.training.license.sharing.util;

import com.training.license.sharing.dto.UserDTO;
import com.training.license.sharing.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverters {
    private ModelMapper modelMapper;
    private CredentialConverter credentialConverter;
    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setCredential(credentialConverter.convertToDTO(user.getCredential()));

        return userDTO;
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
