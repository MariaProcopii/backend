package com.training.license.sharing.controllers;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.services.UserService;
import com.training.license.sharing.util.UserValidation;
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

import java.util.ArrayList;
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

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        final List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        final User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") long id) {
        final Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/deactivate-user")
    public ResponseEntity<List<User>> deactivateUsers(@RequestBody List<Long> usersIds) {
        if (!userValidation.validateUserIds(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        final List<User> deactivatedUsers = userService.deactivateUsers(usersIds);
        return ResponseEntity.ok(deactivatedUsers);
    }

    @PutMapping("/changing-role")
    public ResponseEntity<List<User>> changeRole(@RequestBody List<Long> usersIds, @RequestParam("role") String stringRole) {
        if (!userValidation.validateRole(stringRole) || !userValidation.validateUserIds(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        final List<User> changedUsers = userService.changeRoleForUsers(usersIds, getRole(stringRole));
        return ResponseEntity.ok(changedUsers);
    }

    private Role getRole(String stringRole) {
        return valueOf(stringRole);
    }

    @PutMapping("/approve-access")
    public ResponseEntity<List<User>> approveAccess() {
        final List<User> approveAccessedUsers = new ArrayList<>();
        return ResponseEntity.ok(approveAccessedUsers);
    }

    @PostMapping("/request-access")
    public ResponseEntity<User> responseAccess(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }

}
