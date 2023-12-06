package com.training.license.sharing.controllers;

import com.training.license.sharing.util.CustomExceptions.CredentialsNonExistentException;
import com.training.license.sharing.util.CustomExceptions.ImageHasIllegalTypeException;
import com.training.license.sharing.util.CustomExceptions.ImageSizeOutOfBoundsException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByIdException;
import com.training.license.sharing.util.CustomExceptions.LicenseExistentByNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOGO_KEY = "logo";
    private static final String CREDENTIALS_KEY = "credentials";
    public static final String LICENSE_NAME_KEY = "licenseName";
    public static final String LICENSE_ID_KEY = "licenseId";

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

    @ExceptionHandler(CredentialsNonExistentException.class)
    public ResponseEntity<Map<String, String>> credentialsNonExistent(CredentialsNonExistentException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(Map.of(CREDENTIALS_KEY, ex.getMessage()));
    }

    @ExceptionHandler(LicenseExistentByNameException.class)
    public ResponseEntity<Map<String, String>> licenseExistentByNameProblem(LicenseExistentByNameException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LICENSE_NAME_KEY, ex.getMessage()));
    }
    @ExceptionHandler(LicenseExistentByIdException.class)
    public ResponseEntity<Map<String, String>> licenseExistentByIdProblem(LicenseExistentByIdException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(LICENSE_ID_KEY, ex.getMessage()));
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
        messages.put(keyValueArray[0], keyValueArray[1].trim().replaceAll("'", ""));
    }

}
