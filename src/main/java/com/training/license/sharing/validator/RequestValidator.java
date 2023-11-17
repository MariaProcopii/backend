package com.training.license.sharing.validator;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.Request;
import com.training.license.sharing.entities.enums.RequestStatus;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.services.RequestService;
import com.training.license.sharing.services.UserService;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@NonNullApi
public class RequestValidator implements Validator {

    private final UserService userService;
    private final RequestService requestService;
    private final LicenseService licenseService;

    @Value("${user.limit.per.license}")
    private String userLimitPerLicense;

    public RequestValidator(UserService userService, RequestService requestService, LicenseService licenseService) {
        this.userService = userService;
        this.requestService = requestService;
        this.licenseService = licenseService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return List.of(RequestDTO.class, Request.class).contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //
    }

    public void validateRequestAccess(Object target, Errors errors) {
        final RequestDTO requestDTO = (RequestDTO) target;
        if (userService.findByNameAndDiscipline(requestDTO.getUsername(), requestDTO.getDiscipline()).isEmpty()) {
            errors.reject("User with this data does not exist");
        }
        validateRequestedLicense(errors, requestDTO.getApp(), requestDTO.getStartOfUse());
    }

    private void validateRequestedLicense(Errors errors, String app, LocalDate startOfUse) {
        final Optional<License> requestedLicenses = licenseService.findByNameAndStartDate(app, startOfUse);
        requestedLicenses.ifPresentOrElse(license -> {
                    validateLicenseIsExpiredForRequest(errors, license);
                    validateExceededNumberOfUsers(errors, license);
                },
                () -> errors.reject("License does not exist"));
    }

    public void validateRequestAccessApproval(List<Long> ids, Errors errors) {
        List<Request> requests = requestService.findAllById(ids);
        List<Request> rejectedRequests = getRejectedRequests(requests);

        if (!rejectedRequests.isEmpty()) {
            String errorMessage = buildErrorMessage(rejectedRequests);
            errors.reject(errorMessage);
        }
        requests.forEach(request -> checkIfLicenseIsInvalidForRequest(errors, request.getApp(), request.getStartOfUse()));
    }

    private String buildErrorMessage(List<Request> rejectedRequests) {
        StringBuilder defaultMessage = new StringBuilder("Rejected request can't be changed. Requests:");
        rejectedRequests.forEach(request -> defaultMessage.append(request.getApp()).append(" for ")
                .append(request.getUser().getName()).append(";"));
        return defaultMessage.toString();
    }

    private List<Request> getRejectedRequests(List<Request> requests) {
        return requests.stream()
                .filter(request -> Objects.equals(request.getStatus(), RequestStatus.REJECTED))
                .toList();
    }

    private void checkIfLicenseIsInvalidForRequest(Errors errors, String app, LocalDate startOfUse) {
        Optional<License> licenseOptional = licenseService.findByNameAndStartDate(app, startOfUse);
        licenseOptional.ifPresentOrElse(license -> {
                    validateLicenseIsExpiredForRequest(errors, license);
                    validateExceededNumberOfUsers(errors, license);
                },
                () -> errors.reject("License does not exist"));
    }

    private void validateExceededNumberOfUsers(Errors errors, License license) {
        final Long amountOfUsers = licenseService.findNumberOfUsersByLicense(license);
        if (Long.parseLong(userLimitPerLicense) < amountOfUsers) {
            errors.reject("the training license exceeded the number of users");
        }
    }

    private void validateLicenseIsExpiredForRequest(Errors errors, License license) {
        if (!isAvailable(license)) {
            errors.reject("License is expired");
        }
    }

    public static boolean isAvailable(License license) {
        return license.getAvailability() > 0 && license.getUnusedPeriod() == 0;
    }

}
