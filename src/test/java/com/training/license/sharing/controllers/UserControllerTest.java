package com.training.license.sharing.controllers;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void findByIdShouldReturnNotFound() {
        final Long userId = 3L;
        final ResponseEntity<User> userResponseEntity = userController.findById(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.empty());
        assertThat(userResponseEntity.getStatusCode()).isSameAs(NOT_FOUND);
    }

    @Test
    public void changeRoleShouldReturnBadRequest() {
        final List<Long> ids = asList(1L, 2L, 3L, 4L);
        final String nonValidatedRole = "INVALID_ROLE";
        final ResponseEntity<List<User>> responseEntity = userController.changeRole(ids, nonValidatedRole);

        assertThat(responseEntity.getStatusCode()).isSameAs(BAD_REQUEST);
    }
}