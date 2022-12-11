package com.shrayjai.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankError {

    private String ref;

    private String message;
}
