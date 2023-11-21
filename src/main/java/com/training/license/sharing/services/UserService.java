package com.training.license.sharing.services;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.training.license.sharing.entities.enums.Status.INACTIVE;
import static com.training.license.sharing.util.LoggerUtil.logInfo;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        logInfo("Saving user: " + user.getName());
        return userRepository.save(user);
    }

    public List<User> getAllUsers(Sort sort) {
        logInfo("Fetching all users");
        return userRepository.findAll(sort);
    }

    public Optional<User> findByEmail(String email) {
        logInfo("Fetching user by email: " + email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long userId) {
        logInfo("Fetching user by ID: " + userId);
        return userRepository.findById(userId);
    }

    @Transactional
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

    public int getTotalUsers() {
        return userRepository.countUsers();
    }

    public int getTotalDisciplines() {
        return userRepository.countTotalDisciplines();
    }

    public Map<Discipline, Long> getUsersPerDiscipline(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "discipline"));
        Page<Object[]> paginatedResult = userRepository.getUsersPerDiscipline(pageable);

        return paginatedResult.getContent()
                .stream()
                .collect(Collectors.toMap(
                        obj -> (Discipline) obj[0],
                        obj -> (Long) obj[1],
                        (existing, replacement) -> existing,
                        TreeMap::new
                ));
    }

    public int getNewUsersCountThisMonth() {
        int currentDayOfMonth = LocalDate.now().getDayOfMonth();
        return userRepository.countByLastActiveLessThan(currentDayOfMonth);
    }
}
