package com.training.license.sharing.services;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.Request;
import com.training.license.sharing.entities.User;
import com.training.license.sharing.repositories.RequestRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.training.license.sharing.entities.enums.RequestStatus.APPROVED;
import static com.training.license.sharing.entities.enums.RequestStatus.PENDING;
import static com.training.license.sharing.entities.enums.RequestStatus.REJECTED;
import static com.training.license.sharing.util.InfoMessageUtil.APPROVE_REQUEST;
import static com.training.license.sharing.util.InfoMessageUtil.EXPIRED_LICENSE_CHECK;
import static com.training.license.sharing.util.InfoMessageUtil.FETCH_AND_VALIDATE_REQUESTS;
import static com.training.license.sharing.util.InfoMessageUtil.FIND_ALL_REQUESTS;
import static com.training.license.sharing.util.InfoMessageUtil.LICENSE_EXPIRY_CHECK;
import static com.training.license.sharing.util.InfoMessageUtil.REJECTING_REQUEST;
import static com.training.license.sharing.util.InfoMessageUtil.REJECT_REQUEST;
import static com.training.license.sharing.util.InfoMessageUtil.REQUEST_ACCESS;
import static com.training.license.sharing.validator.RequestValidator.isAvailable;

@Service
@Log4j2
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final LicenseService licenseService;

    public RequestService(RequestRepository requestRepository, UserService userService, LicenseService licenseService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.licenseService = licenseService;
    }

    public List<UserRequestDTO> findAll(Boolean asc, String field) {
        log.info(FIND_ALL_REQUESTS, asc, field);
        asc = Objects.requireNonNullElse(asc, false);
        field = Objects.requireNonNullElse(field, "requestDate");

        fetchAndValidateRequests();

        if (isSortableByUsernameOrDiscipline(field)) {
            Comparator<UserRequestDTO> comparator = getComparatorByField(field);
            return sortAndMapRequests(comparator, asc);
        } else {
            Order order = asc ? Order.asc(field) : Order.desc(field);
            return sortAndMapRequests(order);
        }
    }

    @Transactional
    public void approveRequest(List<Long> ids) {
        log.info(APPROVE_REQUEST, ids);
        findAllById(ids).stream()
                .filter(request -> Objects.equals(request.getStatus(), PENDING))
                .forEach(request -> {
                    request.setStatus(APPROVED);
                    requestRepository.save(request);
                });
    }

    @Transactional
    public void rejectRequest(List<Long> ids) {
        log.info(REJECT_REQUEST, ids);
        findAllById(ids).stream()
                .filter(request -> !Objects.equals(request.getStatus(), REJECTED))
                .forEach(request -> {
                    request.setStatus(REJECTED);
                    requestRepository.save(request);
                });
    }

    public void requestAccess(RequestDTO dto) {
        log.info(REQUEST_ACCESS, dto.getUsername(), dto.getDiscipline());
        final User user = userService.findByNameAndDiscipline(dto.getUsername(), dto.getDiscipline()).get();
        requestRepository.save(Request.builder().id(0L)
                .user(user)
                .startOfUse(dto.getStartOfUse())
                .app(dto.getApp())
                .status(PENDING)
                .requestDate(LocalDateTime.now())
                .build()
        );
    }

    public List<Request> findAllById(List<Long> ids) {
        return requestRepository.findAllById(ids);
    }

    private UserRequestDTO requestToDTOMapper(Request request) {
        return new UserRequestDTO(request.getId(),
                request.getStatus(),
                request.getApp(),
                request.getRequestDate(),
                request.getStartOfUse(),
                request.getUser().getName(),
                request.getUser().getDiscipline());
    }

    public boolean isAllRequestIdsExistInDB(List<Long> ids) {
        return !ids.isEmpty() && ids.stream().allMatch(requestRepository::existsById);
    }

    private boolean isSortableByUsernameOrDiscipline(String field) {
        return Objects.equals(field, "username") || Objects.equals(field, "discipline");
    }

    private Comparator<UserRequestDTO> getComparatorByField(String field) {
        return Objects.equals(field, "username") ?
                Comparator.comparing(UserRequestDTO::getUsername) :
                Comparator.comparing(UserRequestDTO::getDiscipline);
    }

    private List<UserRequestDTO> sortAndMapRequests(Comparator<UserRequestDTO> comparator, boolean asc) {
        return requestRepository.findAll()
                .stream()
                .map(this::requestToDTOMapper)
                .sorted(asc ? comparator : comparator.reversed())
                .toList();
    }

    private List<UserRequestDTO> sortAndMapRequests(Order order) {
        return requestRepository.findAll(Sort.by(order))
                .stream()
                .map(this::requestToDTOMapper)
                .toList();
    }

    private void fetchAndValidateRequests() {
        log.info(FETCH_AND_VALIDATE_REQUESTS);
        List<Request> requests = requestRepository.findAll();
        requests.forEach(this::findExpiredLicenseForRequestAndRejectIt);
    }

    private void findExpiredLicenseForRequestAndRejectIt(Request request) {
        log.info(EXPIRED_LICENSE_CHECK, request.getId());
        Optional<License> licenseOptional = licenseService.findByNameAndStartDate(request.getApp(), request.getStartOfUse());
        if (licenseOptional.isPresent()) {
            License license = licenseOptional.get();
            if (isLicenseExpired(license)) {
                rejectRequest(request);
            }
        }
    }

    private boolean isLicenseExpired(License license) {
        log.info(LICENSE_EXPIRY_CHECK, license.getId());
        return !isAvailable(license) || licenseService.findNumberOfUsersByLicense(license) >= 10;
    }

    private void rejectRequest(Request request) {
        log.info(REJECTING_REQUEST, request.getId());
        request.setStatus(REJECTED);
        requestRepository.save(request);
    }

}
