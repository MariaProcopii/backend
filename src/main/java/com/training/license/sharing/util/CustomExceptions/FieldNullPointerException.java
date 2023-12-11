package com.training.license.sharing.util.CustomExceptions;

import lombok.Getter;

@Getter
public class FieldNullPointerException extends RuntimeException{
    private final String fieldName;
    public FieldNullPointerException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
}
