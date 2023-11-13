package com.training.license.sharing.services;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.training.license.sharing.entities.enums.Status.INACTIVE;
import static com.training.license.sharing.util.LoggerUtil.logInfo;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
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
    public List<User> changeRoleForUsers(List<Long> userIds, Role newRole) {
        return userIds.stream()
                .map(this::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setRole(newRole);
                    return user;
                })
                .map(this::saveUser)
                .peek(user -> logInfo("Role changed for user ID " + user.getId() + ": " + newRole))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<User> deactivateUsers(List<Long> userIds) {
        logInfo("Deactivating users: " + userIds);

        return userIds.stream()
                .map(this::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setStatus(INACTIVE);
                    return user;
                })
                .map(this::saveUser)
                .peek(user -> logInfo("User ID " + user.getId() + " deactivated"))
                .collect(Collectors.toList());
    }
}
