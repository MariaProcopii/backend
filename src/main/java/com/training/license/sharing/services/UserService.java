package com.training.license.sharing.services;

import com.training.license.sharing.dto.DisciplineUserCountDTO;
import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import static com.training.license.sharing.util.InfoMessageUtil.AMOUNT_OF_DISTINCT_DISCIPLINES;
import static com.training.license.sharing.util.InfoMessageUtil.DEACTIVATING_USERS;
import static com.training.license.sharing.util.InfoMessageUtil.DISCIPLINE_USER_COUNTS;
import static com.training.license.sharing.util.InfoMessageUtil.FETCHING_ALL_USERS;
import static com.training.license.sharing.util.InfoMessageUtil.FETCHING_USER_BY_ID;
import static com.training.license.sharing.util.InfoMessageUtil.INIT_ADMIN_USER;
import static com.training.license.sharing.util.InfoMessageUtil.NEW_USER_COUNT;
import static com.training.license.sharing.util.InfoMessageUtil.ROLE_CHANGED_FOR_USER;
import static com.training.license.sharing.util.InfoMessageUtil.SAVING_USER;
import static com.training.license.sharing.util.InfoMessageUtil.TOTAL_AMOUNT_OF_USERS;
import static com.training.license.sharing.util.InfoMessageUtil.USERS_PER_DISCIPLINE;
import static com.training.license.sharing.util.InfoMessageUtil.USER_BY_NAME_AND_DISCIPLINE;
import static com.training.license.sharing.util.InfoMessageUtil.USER_DEACTIVATED;
import static com.training.license.sharing.util.InfoMessageUtil.USER_PASSWORD_ENCODED;

@Service
@RequiredArgsConstructor
@Log4j2
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

            log.info(INIT_ADMIN_USER);
            User adminUser = new User();
            adminUser.setName("Admin User");
            adminUser.setCredential(savedCredential);
            userRepository.save(adminUser);
        }
    }

    public User saveUser(User user, Boolean isNewUserCreation) {
        log.info(SAVING_USER, user.getName());
        if (isNewUserCreation) {
            encodeUserPasswordWithBcrypt(user);
            credentialsService.saveCredential(user.getCredential());
        }
        credentialsService.saveCredential(user.getCredential());
        return userRepository.save(user);
    }

    public List<User> getAllUsers(Sort sort) {
        log.info(FETCHING_ALL_USERS);
        return userRepository.findAll(sort);
    }

    public Optional<User> getUserById(Long userId) {
        log.info(FETCHING_USER_BY_ID, userId);
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
            log.info(ROLE_CHANGED_FOR_USER, user.getId(), newRole);
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
        log.info(DEACTIVATING_USERS, userIds);
        final List<User> userList = userIds.stream()
                .map(this::getUserById)
                .flatMap(Optional::stream)
                .toList();

        for (User user : userList) {
            user.setStatus(INACTIVE);
            saveUser(user, false);
            log.info(USER_DEACTIVATED, user.getId());
        }

        return userList;
    }

    public Optional<User> findByNameAndDiscipline(String name, Discipline discipline) {
        log.info(USER_BY_NAME_AND_DISCIPLINE, name, discipline);
        return userRepository.findByUsernameAndDiscipline(name, discipline);
    }

    public int getTotalUsers() {
        log.info(TOTAL_AMOUNT_OF_USERS);
        return userRepository.countUsers();
    }

    public int getTotalDisciplines() {
        log.info(AMOUNT_OF_DISTINCT_DISCIPLINES);
        return userRepository.countTotalDisciplines();
    }

    public Map<Discipline, Long> getUsersPerDiscipline(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "discipline"));
        Page<Object[]> paginatedResult = userRepository.getUsersPerDiscipline(pageable);
        log.info(USERS_PER_DISCIPLINE);
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

        log.info(NEW_USER_COUNT);
        return userRepository.countByLastActiveLessThan(currentDayOfMonth);
    }

    private void encodeUserPasswordWithBcrypt(User user) {
        String password = user.getCredential().getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.getCredential().setPassword(encodedPassword);

        log.info(USER_PASSWORD_ENCODED);
    }

    public List<DisciplineUserCountDTO> getDisciplineUserCounts() {
        List<DisciplineUserCountDTO> disciplineUserCounts = userRepository.getUsersPerDiscipline().stream()
                .filter(obj -> obj[0] != null)
                .map(obj -> new DisciplineUserCountDTO(((Discipline) obj[0]).name(), ((Long) obj[1]).intValue()))
                .collect(Collectors.toList());

        log.info(DISCIPLINE_USER_COUNTS);
        return disciplineUserCounts;
    }
}
