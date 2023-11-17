package com.training.license.sharing.validator;

import com.training.license.sharing.services.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserValidation {

    private final UserService userService;

    public UserValidation(UserService userService) {
        this.userService = userService;
    }

    public boolean areAllSelectedIdsExistingInDB(List<Long> usersIds) {
        return usersIds.stream().noneMatch(id -> userService.getUserById(id).isEmpty());
    }
}
