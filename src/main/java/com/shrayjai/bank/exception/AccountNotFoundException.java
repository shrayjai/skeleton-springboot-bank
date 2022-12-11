package com.shrayjai.bank.exception;

public class AccountNotFoundException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6231091904567322345L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
