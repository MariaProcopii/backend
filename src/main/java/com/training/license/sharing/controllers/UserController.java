package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.UserDTO;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.services.UserService;
import com.training.license.sharing.validator.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.entities.enums.Role.valueOf;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;

    @Autowired
    public UserController(UserService userService, UserValidation userValidation) {
        this.userService = userService;
        this.userValidation = userValidation;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> findAll() {
        final List<UserDTO> users = userService.getAllUsers().stream()
                .map(userService::convertToDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/save-user")
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        final User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(userService.convertToDTO(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") long id) {
        final Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> ResponseEntity.ok(userService.convertToDTO(user)))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/deactivate-user")
    public ResponseEntity<List<UserDTO>> deactivateUsers(@RequestBody List<Long> usersIds) {
        if (!userValidation.areAllSelectedIdsExistingInDB(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        final List<UserDTO> deactivatedUsers = userService.deactivateUsers(usersIds).stream()
                .map(userService::convertToDTO)
                .toList();

        return ResponseEntity.ok(deactivatedUsers);
    }

    @PutMapping("/changing-role")
    public ResponseEntity<List<UserDTO>> changeRole(@RequestBody List<Long> usersIds,
                                                    @RequestParam("role") String stringRole) {
        if (!userValidation.areAllSelectedIdsExistingInDB(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        final List<UserDTO> changedUsers = userService.changeRoleForUsers(usersIds, getRole(stringRole))
                .stream()
                .map(userService::convertToDTO)
                .toList();

        return ResponseEntity.ok(changedUsers);
    }

    private Role getRole(String stringRole) {
        return valueOf(stringRole);
    }
}
