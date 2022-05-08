package com.app.dsmovieticket.auth.exceptions;

public class FieldValidationFailed extends RuntimeException{

    public FieldValidationFailed(String message) {
        super(message);
    }

}