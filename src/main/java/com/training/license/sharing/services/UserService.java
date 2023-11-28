package com.training.license.sharing.services;

import com.training.license.sharing.dto.DisciplineUserCountDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequiredArgsConstructor
public class UserService {
    @Value("${admin.user.username}")
    private String adminUsername;

    @Value("${admin.user.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final CredentialsService credentialsService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {
        if (!credentialsService.findByUsername(adminUsername).isPresent()) {
            Credential adminCredential = new Credential();
            adminCredential.setUsername(adminUsername);
            adminCredential.setPassword(passwordEncoder.encode(adminPassword));
            adminCredential.setRole(Role.ADMIN);

            Credential savedCredential = credentialsService.saveCredential(adminCredential);

            User adminUser = new User();
            adminUser.setName("Admin User");
            adminUser.setCredential(savedCredential);
            userRepository.save(adminUser);
        }
    }

    public User saveUser(User user, Boolean isNewUserCreation) {
        logInfo("Saving user: " + user.getName());
        if (isNewUserCreation) {
            encodeUserPasswordWithBcrypt(user);
            credentialsService.saveCredential(user.getCredential());
        }
        credentialsService.saveCredential(user.getCredential());
        return userRepository.save(user);
    }

    public List<User> getAllUsers(Sort sort) {
        logInfo("Fetching all users");
        return userRepository.findAll(sort);
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
            changeRoleOfUser(newRole, user);
            logInfo("Role changed for user ID " + user.getId() + ": " + newRole);
        }

        return userList;
    }

    private void changeRoleOfUser(Role newRole, User user) {
        Credential credential = user.getCredential();
        credential.setRole(newRole);
        credentialsService.saveCredential(credential);
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
            saveUser(user, false);
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

    private void encodeUserPasswordWithBcrypt(User user) {
        String password = user.getCredential().getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.getCredential().setPassword(encodedPassword);
    }

    public List<DisciplineUserCountDTO> getDisciplineUserCounts() {
        return userRepository.getUsersPerDiscipline().stream()
                .filter(obj -> obj[0] != null) // Filter out records with null discipline
                .map(obj -> new DisciplineUserCountDTO(((Discipline) obj[0]).name(), ((Long) obj[1]).intValue()))
                .collect(Collectors.toList());
    }

}
