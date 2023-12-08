package com.training.license.sharing.util.CustomExceptions;

public class LicenseExistentByNameException extends RuntimeException{
    public LicenseExistentByNameException(String message) {
        super(message);
    }
}
