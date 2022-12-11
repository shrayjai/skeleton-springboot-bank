package com.shrayjai.bank.exception;

public class InsufficientBalanceException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -612148690250178938L;

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
