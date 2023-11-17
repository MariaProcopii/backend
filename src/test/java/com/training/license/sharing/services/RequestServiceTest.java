package com.training.license.sharing.services;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.Request;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.training.license.sharing.entities.enums.Discipline.TESTING;
import static com.training.license.sharing.entities.enums.RequestStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private LicenseService licenseService;

    @Captor
    ArgumentCaptor<Request> requestArgumentCaptor;

    private Request testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new Request();
        testRequest.setUser(new User());
        testRequest.setApp("TESTAPP");
        testRequest.setStatus(PENDING);
        testRequest.setStartOfUse(LocalDate.now());
        testRequest.setRequestDate(LocalDateTime.now());
        testRequest.setId(23L);
    }

    @Test
    void findAllShouldReturnDTOListAndValidateFetchedRequestsTest() {

        final License testLicense = new License();
        testLicense.setUnusedPeriod(3);
        testLicense.setAvailability(20);
        when(requestRepository.findAll()).thenReturn(Collections.singletonList(testRequest));
        when(licenseService.findByNameAndStartDate(testRequest.getApp(), testRequest.getStartOfUse()))
                .thenReturn(Optional.of(testLicense));

        final List<UserRequestDTO> obtainedDTO = requestService.findAll(true, "username");

        assertThat(obtainedDTO.get(0).getRequestId()).isSameAs(testRequest.getId());
        verify(requestRepository, times(2)).findAll();
        verify(licenseService).findByNameAndStartDate(testRequest.getApp(), testRequest.getStartOfUse());
        verify(requestRepository).save(any());
    }

    @Test
    void approveRequestShouldApproveRequestsByIdListTest() {
        final List<Long> testIds = List.of(23L);
        final List<Request> testRequests = Collections.singletonList(testRequest);
        when(requestRepository.findAllById(testIds)).thenReturn(testRequests);

        requestService.approveRequest(testIds);

        verify(requestRepository).findAllById(testIds);
        verify(requestRepository).save(requestArgumentCaptor.capture());

        final Request capturedRequest = requestArgumentCaptor.getValue();
        assertThat(capturedRequest.getId()).isSameAs(testIds.get(0));
    }

    @Test
    void rejectRequestShouldRejectRequestsByIdListTest() {
        final List<Long> testIds = List.of(23L);
        final List<Request> testRequests = Collections.singletonList(testRequest);
        when(requestRepository.findAllById(testIds)).thenReturn(testRequests);

        requestService.rejectRequest(testIds);

        verify(requestRepository).findAllById(testIds);
        verify(requestRepository).save(requestArgumentCaptor.capture());

        final Request captiredRequest = requestArgumentCaptor.getValue();
        assertThat(captiredRequest.getId()).isSameAs(testIds.get(0));
    }

    @Test
    void createRequestShouldCreateRequestFromDTOTest() {
        final String testUserName = "testUserName";
        final RequestDTO testDTO = new RequestDTO(testUserName, TESTING, LocalDate.now(), "Udemy");
        final User testUser = new User();
        testUser.setName(testUserName);
        testUser.setDiscipline(TESTING);
        when(userService.findByNameAndDiscipline(any(), any())).thenReturn(Optional.of(testUser));

        requestService.requestAccess(testDTO);

        verify(userService).findByNameAndDiscipline(testDTO.getUsername(), testDTO.getDiscipline());
        verify(requestRepository).save(requestArgumentCaptor.capture());

        final Request capturedRequest = requestArgumentCaptor.getValue();
        assertThat(capturedRequest.getApp()).isSameAs(testDTO.getApp());
        assertThat(capturedRequest.getStartOfUse()).isSameAs(testDTO.getStartOfUse());
        assertThat(capturedRequest.getUser().getName()).isSameAs(testDTO.getUsername());
        assertThat(capturedRequest.getUser().getDiscipline()).isSameAs(testDTO.getDiscipline());

    }
}