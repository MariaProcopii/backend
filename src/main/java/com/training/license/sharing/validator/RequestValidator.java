package com.training.license.sharing.validator;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.entities.License;
import com.training.license.sharing.entities.Request;
import com.training.license.sharing.entities.enums.RequestStatus;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.services.RequestService;
import com.training.license.sharing.services.UserService;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.training.license.sharing.util.ErrorKeyUtil.ID_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.INVALID_DATA;
import static com.training.license.sharing.util.ErrorKeyUtil.LICENSE_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.REQUEST_KEY;
import static com.training.license.sharing.validator.ErrorMessagesUtil.ID_NOT_EXIST_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_DO_NOT_HAVE_SEATS_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_EXPIRATION_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.LICENSE_NOT_EXIST_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.REJECTED_REQUEST_CAN_NOT_BE_CHANGED_TEMPLATE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.USER_NOT_EXIST_MESSAGE;

@Component
@NonNullApi
@Log4j2
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

    public void validateRequestAccess(Object target, BindingResult errors) {
        final RequestDTO requestDTO = (RequestDTO) target;
        if (userService.findByNameAndDiscipline(requestDTO.getUsername(), requestDTO.getDiscipline()).isEmpty()) {
            errors.addError(new FieldError("", INVALID_DATA, USER_NOT_EXIST_MESSAGE));
        }
        validateRequestedLicense(errors, requestDTO.getApp(), requestDTO.getStartOfUse());
    }

    public void validateRequestAccessApproval(List<Long> ids, BindingResult errors) {
        if (!requestService.isAllRequestIdsExistInDB(ids)) {
            errors.addError(new FieldError("", ID_KEY, ID_NOT_EXIST_MESSAGE));
        }

        List<Request> requests = requestService.findAllById(ids);
        List<Request> rejectedRequests = getRejectedRequests(requests);

        if (!rejectedRequests.isEmpty()) {
            String errorMessage = buildErrorMessage(rejectedRequests);
            errors.addError(new FieldError("", REQUEST_KEY, errorMessage));
            log.error(errorMessage);
        }
        requests.forEach(request -> checkIfLicenseIsInvalidForRequest(errors, request.getApp(), request.getStartOfUse()));
    }

    public void validateRequestAccessRejection(List<Long> ids, BindingResult errors) {
        if (!requestService.isAllRequestIdsExistInDB(ids)) {
            errors.addError(new FieldError("", ID_KEY, ID_NOT_EXIST_MESSAGE));
        }
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

    private void checkIfLicenseIsInvalidForRequest(BindingResult errors, String app, LocalDate startOfUse) {
        Optional<License> licenseOptional = licenseService.findByNameAndStartDate(app, startOfUse);
        licenseOptional.ifPresentOrElse(license -> {
                    validateLicenseIsExpiredForRequest(errors, license);
                    validateExceededNumberOfUsers(errors, license);
                },
                () -> errors.addError(new FieldError("", LICENSE_KEY, LICENSE_NOT_EXIST_MESSAGE)));
    }

    private void validateExceededNumberOfUsers(BindingResult errors, License license) {
        final Long amountOfUsers = licenseService.findNumberOfUsersByLicense(license);
        if (Long.parseLong(userLimitPerLicense) < amountOfUsers) {
            errors.addError(new FieldError("", LICENSE_KEY, LICENSE_DO_NOT_HAVE_SEATS_MESSAGE));
            log.error(LICENSE_DO_NOT_HAVE_SEATS_MESSAGE);
        }
    }

    private void validateLicenseIsExpiredForRequest(BindingResult errors, License license) {
        if (!isAvailable(license)) {
            errors.addError(new FieldError("", LICENSE_KEY, LICENSE_EXPIRATION_MESSAGE));
            log.error(LICENSE_EXPIRATION_MESSAGE);
        }
    }

    public static boolean isAvailable(License license) {
        return license.getAvailability() > 0 && license.getUnusedPeriod() == 0;
    }


    private void validateRequestedLicense(BindingResult errors, String app, LocalDate startOfUse) {
        final Optional<License> requestedLicenses = licenseService.findByNameAndStartDate(app, startOfUse);
        requestedLicenses.ifPresentOrElse(license -> {
                    validateLicenseIsExpiredForRequest(errors, license);
                    validateExceededNumberOfUsers(errors, license);
                },
                () -> {
                    errors.addError(new FieldError("", LICENSE_KEY, LICENSE_NOT_EXIST_MESSAGE));
                    log.error(LICENSE_NOT_EXIST_MESSAGE);
                });
    }
}