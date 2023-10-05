package com.intuit.profile.exception;

public class UserNotSubscribedToProductsException extends RuntimeException{

    public UserNotSubscribedToProductsException(String message) {
        super(message);
    }
}
