package com.training.license.sharing.validator;

import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.util.CustomExceptions.ParamNotValidException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.training.license.sharing.util.ErrorKeyUtil.INVALID_OBJECT_FIELD_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.PAGE_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.ROLE_KEY;
import static com.training.license.sharing.util.ErrorKeyUtil.SIZE_KEY;
import static com.training.license.sharing.validator.ErrorMessagesUtil.INVALID_OBJECT_FIELD;
import static com.training.license.sharing.validator.ErrorMessagesUtil.PAGE_NOT_VALID;
import static com.training.license.sharing.validator.ErrorMessagesUtil.ROLE_NOT_VALID;
import static com.training.license.sharing.validator.ErrorMessagesUtil.SIZE_NOT_VALID;
@Component
@Log4j2
public class ParameterValidator {

    public static void isPageNumberValid(int page){
        if(page < 0) {
            log.error(PAGE_NOT_VALID);
            throw new ParamNotValidException(Map.of(PAGE_KEY, PAGE_NOT_VALID));
        }
    }

    public static void isSizeValid(int size){
        if(size <= 0) {
            log.error(SIZE_NOT_VALID);
            throw new ParamNotValidException(Map.of(SIZE_KEY, SIZE_NOT_VALID));
        }
    }

    public static void isRoleValid(String role){
        final List<Role> validRoles = List.of(Role.ADMIN, Role.USER, Role.REVIEWER);
        final boolean contains = validRoles.stream().anyMatch(validRole -> validRole.name().equals(role));
        if(!contains){
            log.error(ROLE_NOT_VALID);
            throw new ParamNotValidException(Map.of(ROLE_KEY, ROLE_NOT_VALID));
        }
    }

    public static void isObjectFieldValid(String fieldName, Class<?> classForChecking) {
        List<String> validFieldNames = getFieldNames(classForChecking);
        if (!validFieldNames.contains(fieldName)) {
            log.error(INVALID_OBJECT_FIELD);
            throw new ParamNotValidException(Map.of(INVALID_OBJECT_FIELD_KEY, INVALID_OBJECT_FIELD));
        }
    }

    private static List<String> getFieldNames(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .toList();
    }
}