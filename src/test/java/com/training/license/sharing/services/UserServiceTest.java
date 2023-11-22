package com.training.license.sharing.services;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.entities.enums.Status;
import com.training.license.sharing.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.training.license.sharing.UserFactoryData.createTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    CredentialsService credentialsService;

    @Captor
    private ArgumentCaptor<Integer> dayOfMonthCaptor;

    @InjectMocks
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;


    private static final User USER1 = createTestUser(1L);
    private static final User USER2 = createTestUser(2L);
    private static final List<User> LIST_OF_USERS = List.of(USER1, USER2);
    private static final Long INVALID_USER_ID = 10L;
    private static final List<Long> LIST_OF_IDS = Arrays.asList(USER1.getId(), USER2.getId(), INVALID_USER_ID);

    @BeforeEach
    void setUp() {
        reset(userRepository);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(USER1)).thenReturn(USER1);

        final User savedUser = userService.saveUser(USER1, true);

        assertAll(
                () -> assertThat(savedUser).isNotNull(),
                () -> assertThat(savedUser.getName()).isEqualTo(USER1.getName()),
                () -> verify(userRepository).save(USER1)
        );
    }

    @Test
    void shouldGetAllUsers() {
        final Sort sort = Sort.by(Sort.Direction.ASC,"role");
        when(userRepository.findAll(sort)).thenReturn(LIST_OF_USERS);

        final List<User> allUsers = userService.getAllUsers(sort);

        assertAll(
                () -> assertThat(allUsers).isNotNull(),
                () -> assertThat(allUsers).hasSize(2),
                () -> assertThat(LIST_OF_USERS).contains(USER1),
                () -> assertThat(LIST_OF_USERS).contains(USER2),
                () -> verify(userRepository).findAll(sort)
        );
    }

    @Test
    void shouldGetUserByValidId() {
        final long userValidId = 1L;
        USER1.setId(userValidId);
        when(userRepository.findById(userValidId)).thenReturn(Optional.of(USER1));

        final Optional<User> foundUser = userService.getUserById(userValidId);

        assertAll(
                () -> assertThat(foundUser).isPresent(),
                () -> assertThat(foundUser.get().getId()).isEqualTo(userValidId),
                () -> verify(userRepository).findById(userValidId)
        );
    }

    @Test
    void shouldNotGetUserByInvalidId() {
        final long userInvalidId = 10L;
        when(userRepository.findById(userInvalidId)).thenReturn(Optional.empty());

        final Optional<User> foundUser = userService.getUserById(userInvalidId);

        assertAll(
                () -> assertTrue(foundUser.isEmpty()),
                () -> verify(userRepository).findById(userInvalidId)
        );
    }

    @Test
    void shouldChangeRoleForValidUserIds() {
        final Role newRole = Role.ADMIN;

        when(userRepository.findById(USER1.getId())).thenReturn(Optional.of(USER1));
        when(userRepository.findById(USER2.getId())).thenReturn(Optional.of(USER2));
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        final List<User> updatedUsers = userService.changeRoleForUsers(LIST_OF_IDS, newRole);

        assertAll(
                () -> assertThat(updatedUsers).hasSize(2),
                () -> verify(userRepository, times(3)).findById(anyLong()),
                () -> verify(credentialsService,times(2)).saveCredential(any()),
                () -> assertThat(updatedUsers)
                        .extracting(user -> user.getCredential().getRole())
                        .containsOnly(newRole)
        );
    }

    @Test
    void shouldDeactivateUsersForValidIds() {
        final Long userId1 = USER1.getId();
        final Long userId2 = USER2.getId();

        when(userRepository.findById(userId1)).thenReturn(Optional.of(USER1));
        when(userRepository.findById(userId2)).thenReturn(Optional.of(USER2));
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());
        when(userRepository.save(USER1)).thenReturn(USER1);
        when(userRepository.save(USER2)).thenReturn(USER2);

        final List<User> deactivatedUsers = userService.deactivateUsers(LIST_OF_IDS);

        assertAll(
                () -> assertThat(deactivatedUsers).hasSize(2),
                () -> verify(userRepository, times(3)).findById(anyLong()),
                () -> deactivatedUsers.forEach(deactivatedUser -> {
                    assertThat(deactivatedUser.getStatus()).isEqualTo(Status.INACTIVE);
                    verify(userRepository).save(deactivatedUser);
                })
        );
    }

    @Test
    void shouldGetAmountOfUsers() {
        when(userRepository.countUsers()).thenReturn(LIST_OF_USERS.size());
        final int amountOfUsers = userService.getTotalUsers();
        assertThat(amountOfUsers).isEqualTo(LIST_OF_USERS.size());
        verify(userRepository).countUsers();
    }

    @Test
    void shouldCountTotalDisciplines() {
        final int expectedTotalDisciplines = 1;
        when(userRepository.countTotalDisciplines()).thenReturn(expectedTotalDisciplines);
        final int actualTotalDisciplines = userService.getTotalDisciplines();
        assertThat(actualTotalDisciplines).isEqualTo(expectedTotalDisciplines);
        verify(userRepository).countTotalDisciplines();
    }

    @Test
    void shouldGetUsersPerDiscipline() {
        final int page = 0;
        final int size = 8;

        final Discipline discipline1 = Discipline.DEVELOPMENT;
        final Discipline discipline2 = Discipline.TESTING;

        final List<Object[]> sampleData = List.of(
                new Object[]{discipline1, 1L},
                new Object[]{discipline2, 2L}
        );
        final Page<Object[]> paginatedResult = new PageImpl<>(sampleData, PageRequest.of(page, size), sampleData.size());

        when(userRepository.getUsersPerDiscipline(any(Pageable.class))).thenReturn(paginatedResult);
        final Map<Discipline, Long> usersPerDiscipline = userService.getUsersPerDiscipline(page, size);

        assertAll(
                () -> assertThat(usersPerDiscipline).isNotNull(),
                () -> assertThat(usersPerDiscipline).hasSize(2),
                () -> assertThat(usersPerDiscipline).containsEntry(discipline1, 1L),
                () -> assertThat(usersPerDiscipline).containsEntry(discipline2, 2L)
        );
    }

    @Test
    void shouldGetNewUsersCountThisMonth() {

        when(userRepository.countByLastActiveLessThan(any(Integer.class))).thenReturn(1);
        final int newUsersCount = userService.getNewUsersCountThisMonth();
        verify(userRepository).countByLastActiveLessThan(dayOfMonthCaptor.capture());
        assertThat(newUsersCount).isEqualTo(1);
        assertThat(dayOfMonthCaptor.getValue()).isEqualTo(LocalDate.now().getDayOfMonth());
    }
}

