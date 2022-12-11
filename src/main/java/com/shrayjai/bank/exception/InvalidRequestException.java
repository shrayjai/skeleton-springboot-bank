package com.shrayjai.bank.exception;

public class InvalidRequestException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 132257606702494506L;

    public InvalidRequestException(String message) {
        super(message);
    }
}
