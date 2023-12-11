package com.training.license.sharing.util.CustomExceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ParamNotValidException extends RuntimeException{
    Map<String, String> error;
    public ParamNotValidException(Map<String, String> error) {
        this.error = error;
    }
}
