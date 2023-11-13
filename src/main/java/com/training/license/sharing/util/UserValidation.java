package com.training.license.sharing.util;

import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.services.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.training.license.sharing.entities.enums.Role.valueOf;

@Component
public class UserValidation {

    private final UserService userService;

    public UserValidation(UserService userService) {
        this.userService = userService;
    }

    public boolean validateRole(String role){
        try{
            valueOf(role);
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    public boolean validateUserIds(List<Long> usersIds) {
        return usersIds.stream()
                .noneMatch(id-> userService.getUserById(id).isEmpty());
    }
}
