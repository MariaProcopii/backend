package com.training.license.sharing.util.CustomExceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ModelNotValidException extends RuntimeException{
    public Map<String, String> error;
    public ModelNotValidException(Map<String, String> error) {
        this.error = error;
    }
}
