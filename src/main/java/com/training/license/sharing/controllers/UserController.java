package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.UserDTO;
import com.training.license.sharing.dto.UsersOverviewDTO;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.services.UserService;
import com.training.license.sharing.util.CredentialConverter;
import com.training.license.sharing.util.UserConverters;
import com.training.license.sharing.validator.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Map;
import java.util.Optional;

import static com.training.license.sharing.entities.enums.Role.valueOf;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidation userValidation;

    private final UserConverters userConverters;

    @Autowired
    public UserController(UserService userService, UserValidation userValidation, UserConverters userConverters, CredentialsService credentialsService, CredentialConverter credentialConverter) {
        this.userService = userService;
        this.userValidation = userValidation;
        this.userConverters = userConverters;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> findAll(@RequestParam(required = false, defaultValue = "asc") String sort) {
        final Sort sorting = Sort.by(sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "credential.role");
        final List<UserDTO> users = userService.getAllUsers(sorting).stream()
                .map(userConverters::convertToUserDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save-user")
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        final User userFromDTO = userConverters.convertToUser(userDTO);
        final User savedUser = userService.saveUser(userFromDTO, true);

        return ResponseEntity.ok(userConverters.convertToUserDTO(savedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        final Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> ResponseEntity.ok(userConverters.convertToUserDTO(user)))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/deactivate-user")
    public ResponseEntity<List<UserDTO>> deactivateUsers(@RequestBody List<Long> usersIds) {
        if (!userValidation.areAllSelectedIdsExistingInDB(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        final List<UserDTO> deactivatedUsers = userService.deactivateUsers(usersIds).stream()
                .map(userConverters::convertToUserDTO)
                .toList();

        return ResponseEntity.ok(deactivatedUsers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/changing-role")
    public ResponseEntity<List<UserDTO>> changeRole(@RequestBody List<Long> usersIds,
                                                    @RequestParam("role") String stringRole) {
        if (!userValidation.areAllSelectedIdsExistingInDB(usersIds)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        final List<UserDTO> changedUsers = userService.changeRoleForUsers(usersIds, getRole(stringRole))
                .stream()
                .map(userConverters::convertToUserDTO)
                .toList();

        return ResponseEntity.ok(changedUsers);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-total-users")
    public ResponseEntity<Integer> getTotalUsers() {
        return ResponseEntity.ok(userService.getTotalUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-new-users")
    public ResponseEntity<Integer> getNewUsersCount() {
        return ResponseEntity.ok(userService.getNewUsersCountThisMonth());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-total-disciplines")
    public ResponseEntity<Integer> getTotalDisciplines() {
        return ResponseEntity.ok(userService.getTotalDisciplines());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-disciplines-with-users")
    public ResponseEntity<Map<Discipline, Long>> getTotalUsersPerDiscipline(
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "8") int size) {
        return ResponseEntity.ok(userService.getUsersPerDiscipline(page, size));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-users-overview")
    public ResponseEntity<UsersOverviewDTO> getUsersOverview() {
        UsersOverviewDTO overview = new UsersOverviewDTO();
        overview.setTotalUsers(userService.getTotalUsers());
        overview.setTotalDisciplines(userService.getTotalDisciplines());
        overview.setDeltaUsers(userService.getNewUsersCountThisMonth());
        overview.setDisciplines(userService.getDisciplineUserCounts());

        return ResponseEntity.ok(overview);
    }


    private Role getRole(String stringRole) {
        return valueOf(stringRole);
    }

}
