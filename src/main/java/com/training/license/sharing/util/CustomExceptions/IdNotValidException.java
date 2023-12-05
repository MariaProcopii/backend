package com.training.license.sharing.util.CustomExceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class IdNotValidException extends RuntimeException {
    public Map<String, String> error;
    public IdNotValidException(Map<String, String> error) {
        this.error = error;
    }
}
