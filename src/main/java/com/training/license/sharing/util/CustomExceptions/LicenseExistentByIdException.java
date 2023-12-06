package com.training.license.sharing.util.CustomExceptions;

public class LicenseExistentByIdException extends RuntimeException{
    public LicenseExistentByIdException(String message) {
        super(message);
    }
}
