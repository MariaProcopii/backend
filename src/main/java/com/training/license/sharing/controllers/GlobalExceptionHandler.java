package com.training.license.sharing.controllers;

import com.training.license.sharing.util.CustomExceptions.CredentialsNonExistentException;
import com.training.license.sharing.util.CustomExceptions.FieldNullPointerException;
import com.training.license.sharing.util.CustomExceptions.IdNotValidException;
import com.training.license.sharing.util.CustomExceptions.ImageHasIllegalTypeException;
import com.training.license.sharing.util.CustomExceptions.ImageSizeOutOfBoundsException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByIdException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByNameException;
import com.training.license.sharing.util.CustomExceptions.ModelNotValidException;
import com.training.license.sharing.util.CustomExceptions.ParamNotValidException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.training.license.sharing.util.ErrorKeyUtil.ARGUMENT_TYPE_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.BAD_INPUT_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.CREDENTIALS_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.LICENSE_ID_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.LICENSE_NAME_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.LOGO_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.PARAMETER_KEY;
import static com.training.license.sharing.validator.ErrorMessagesUtil.ARGUMENT_TYPE_MISMATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageSizeOutOfBoundsException.class)
    public ResponseEntity<Map<String, String>> imageSizeOutOfBounds(ImageSizeOutOfBoundsException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LOGO_KEY, ex.getMessage()));
    }

    @ExceptionHandler(ImageHasIllegalTypeException.class)
    public ResponseEntity<Map<String, String>> imageNonexistentType(ImageHasIllegalTypeException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LOGO_KEY, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(toMessagesMap(ex));
    }

    @ExceptionHandler(LicenseExistentByIdException.class)
    public ResponseEntity<Map<String, String>> licenseExistentByIdProblem(LicenseExistentByIdException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LICENSE_ID_KEY, ex.getMessage()));
    }

    @ExceptionHandler(LicenseExistentByNameException.class)
    public ResponseEntity<Map<String, String>> licenseExistentByNameProblem(LicenseExistentByNameException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LICENSE_NAME_KEY, ex.getMessage()));
    }

    private Map<String, String> toMessagesMap(MethodArgumentNotValidException ex) {
        Map<String, String> messages = new HashMap<>();
        Arrays.stream(Objects.requireNonNull(ex.getDetailMessageArguments()))
                .flatMap(this::convertToStream)
                .forEach(object -> putMessageIntoMap(object, messages));
        return messages;
    }

    private Stream<?> convertToStream(Object object) {
        return new ArrayList<>((Collection<?>) object).stream();
    }

    private static void putMessageIntoMap(Object object, Map<String, String> messages) {
        String[] keyValueArray = object.toString().split(":");
        messages.put(keyValueArray[0], keyValueArray[1].trim().replace("'", ""));
    }

    @ExceptionHandler(CredentialsNonExistentException.class)
    public ResponseEntity<Map<String, String>> credentialsNonExistent(CredentialsNonExistentException ex){
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(CREDENTIALS_KEY, ex.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleValidationException(Exception e) {
        String errorMessage = extractErrorMessage(e);
        log.error(errorMessage);
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(BAD_INPUT_KEY, errorMessage));
    }

    private String extractErrorMessage(Exception e) {
        String originalMessage = e.getMessage();
        int startIndex = originalMessage.indexOf("String \"") + 8;
        int endIndex = originalMessage.indexOf("\": not one");
        if (endIndex != -1) {
            return "Invalid value for enum: " + originalMessage.substring(startIndex, endIndex);
        } else {
            return extractMessageForMissingRequestBody(originalMessage);
        }
    }

    private String extractMessageForMissingRequestBody(String originalMessage){
        int colonIndex = originalMessage.indexOf(":");
        if (colonIndex != -1) {
            return originalMessage.substring(0, colonIndex).trim();
        } else {
            return originalMessage;
        }
    }

    @ExceptionHandler(IdNotValidException.class)
    public ResponseEntity<Map<String, String>> handleIdNotValidException(IdNotValidException e) {
        return ResponseEntity.status(NOT_FOUND).body(e.getError());
    }

    @ExceptionHandler(ParamNotValidException.class)
    public ResponseEntity<Map<String, String>> handleRequestParamNotValidException(ParamNotValidException e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.getError());
    }

    @ExceptionHandler(ModelNotValidException.class)
    public ResponseEntity<Map<String, String>> handleModelNotValidEx(ModelNotValidException e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.getError());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(ARGUMENT_TYPE_MISMATCH);
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(ARGUMENT_TYPE_KEY, ARGUMENT_TYPE_MISMATCH));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(BAD_INPUT_KEY, ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(PARAMETER_KEY, ex.getMessage()));
    }

    @ExceptionHandler(FieldNullPointerException.class)
    public ResponseEntity<Map<String, String>> fieldNonExistentException(FieldNullPointerException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(ex.getFieldName(), ex.getMessage()));
    }
}
