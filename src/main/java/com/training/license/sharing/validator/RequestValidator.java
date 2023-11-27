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

import static com.training.license.sharing.validator.ErrorMessagesUtil.ID_NOT_EXIST_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_DO_NOT_HAVE_SEATS_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_EXPIRATION_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_NONEXISTENT_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_NOT_EXIST_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.REJECTED_REQUEST_CAN_NOT_BE_CHANGED_TEMPLATE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.USER_NOT_EXIST_MESSAGE;

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
            errors.reject(USER_NOT_EXIST_MESSAGE);
        }
        validateRequestedLicense(errors, requestDTO.getApp(), requestDTO.getStartOfUse());
    }

    public void validateRequestAccessApproval(List<Long> ids, Errors errors) {
        if (!requestService.isAllRequestIdsExistInDB(ids))
            errors.reject(ID_NOT_EXIST_MESSAGE);

        List<Request> requests = requestService.findAllById(ids);
        List<Request> rejectedRequests = getRejectedRequests(requests);

        if (!rejectedRequests.isEmpty()) {
            String errorMessage = buildErrorMessage(rejectedRequests);
            errors.reject(errorMessage);
        }
        requests.forEach(request -> checkIfLicenseIsInvalidForRequest(errors, request.getApp(), request.getStartOfUse()));
    }

    public void validateRequestAccessRejection(List<Long> ids, Errors errors) {
        if (!requestService.isAllRequestIdsExistInDB(ids))
            errors.reject(ID_NOT_EXIST_MESSAGE);
    }

    private String buildErrorMessage(List<Request> rejectedRequests) {
        StringBuilder defaultMessage = new StringBuilder(REJECTED_REQUEST_CAN_NOT_BE_CHANGED_TEMPLATE);
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
                () -> errors.reject(LICENSE_NOT_EXIST_MESSAGE));
    }

    private void validateExceededNumberOfUsers(Errors errors, License license) {
        final Long amountOfUsers = licenseService.findNumberOfUsersByLicense(license);
        if (Long.parseLong(userLimitPerLicense) < amountOfUsers) {
            errors.reject(LICENSE_DO_NOT_HAVE_SEATS_MESSAGE);
        }
    }

    private void validateLicenseIsExpiredForRequest(Errors errors, License license) {
        if (!isAvailable(license)) {
            errors.reject(LICENSE_EXPIRATION_MESSAGE);
        }
    }

    public static boolean isAvailable(License license) {
        return license.getAvailability() > 0 && license.getUnusedPeriod() == 0;
    }


    private void validateRequestedLicense(Errors errors, String app, LocalDate startOfUse) {
        final Optional<License> requestedLicenses = licenseService.findByNameAndStartDate(app, startOfUse);
        requestedLicenses.ifPresentOrElse(license -> {
                    validateLicenseIsExpiredForRequest(errors, license);
                    validateExceededNumberOfUsers(errors, license);
                },
                () -> errors.reject(LICENSE_NONEXISTENT_MESSAGE));
    }

}
