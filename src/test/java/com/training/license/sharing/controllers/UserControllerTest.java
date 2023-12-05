package com.training.license.sharing.controllers;

import com.training.license.sharing.entities.enums.Discipline;
import com.training.license.sharing.services.UserService;
import com.training.license.sharing.validator.ParameterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private ParameterValidator userValidation;

    @InjectMocks
    private UserController userController;
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void shouldGetTotalUsers() {

        when(userService.getTotalUsers()).thenReturn(10);

        ResponseEntity<Integer> responseEntity = userController.getTotalUsers();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(10);
        verify(userService).getTotalUsers();
    }

    @Test
    void shouldGetNewUsersCount() {

        when(userService.getNewUsersCountThisMonth()).thenReturn(5);
        ResponseEntity<Integer> responseEntity = userController.getNewUsersCount();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(5);
        verify(userService).getNewUsersCountThisMonth();
    }


    @Test
    void shouldGetTotalDisciplines() {

        when(userService.getTotalDisciplines()).thenReturn(3);
        ResponseEntity<Integer> responseEntity = userController.getTotalDisciplines();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(3);

        verify(userService).getTotalDisciplines();
    }

    @Test
    void shouldGetTotalUsersPerDiscipline() {
        final int page = 0;
        final int size = 8;

        final Map<Discipline, Long> disciplineMap = new HashMap<>();
        disciplineMap.put(Discipline.TESTING, 1L);
        disciplineMap.put(Discipline.DEVELOPMENT, 2L);

        when(userService.getUsersPerDiscipline(page, size)).thenReturn(disciplineMap);
        ResponseEntity<Map<Discipline, Long>> responseEntity = userController.getTotalUsersPerDiscipline(page, size);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(disciplineMap);
        verify(userService).getUsersPerDiscipline(page, size);
    }
}
