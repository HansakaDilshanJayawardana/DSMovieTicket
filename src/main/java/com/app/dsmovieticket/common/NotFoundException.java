package com.app.dsmovieticket.common;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }

}