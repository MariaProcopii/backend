package com.training.license.sharing.services;

import com.training.license.sharing.dto.UserDTO;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.entities.enums.Status.INACTIVE;
import static com.training.license.sharing.util.LoggerUtil.logInfo;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public User saveUser(User user) {
        logInfo("Saving user: " + user.getName());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        logInfo("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        logInfo("Fetching user by ID: " + userId);
        return userRepository.findById(userId);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> changeRoleForUsers(List<Long> userIds, Role newRole) {
        final List<User> userList = userIds.stream()
                .map(this::getUserById)
                .flatMap(Optional::stream)
                .toList();

        for (User user : userList) {
            user.setRole(newRole);
            saveUser(user);
            logInfo("Role changed for user ID " + user.getId() + ": " + newRole);
        }

        return userList;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> deactivateUsers(List<Long> userIds) {
        logInfo("Deactivating users: " + userIds);

        final List<User> userList = userIds.stream()
                .map(this::getUserById)
                .flatMap(Optional::stream)
                .toList();

        for (User user : userList) {
            user.setStatus(INACTIVE);
            saveUser(user);
            logInfo("User ID " + user.getId() + " deactivated");
        }

        return userList;
    }

    public Optional<User> findByNameAndDiscipline(String name, Discipline discipline) {
        return userRepository.findByUsernameAndDiscipline(name, discipline);
    }

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
