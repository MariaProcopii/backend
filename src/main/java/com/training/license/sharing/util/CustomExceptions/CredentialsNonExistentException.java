package com.training.license.sharing.util.CustomExceptions;

public class CredentialsNonExistentException extends RuntimeException{

    public CredentialsNonExistentException(String message) {
        super(message);
    }

}
