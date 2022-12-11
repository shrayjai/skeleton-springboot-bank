package com.shrayjai.bank.controller;

import com.shrayjai.bank.exception.AccountNotFoundException;
import com.shrayjai.bank.exception.BankError;
import com.shrayjai.bank.exception.InsufficientBalanceException;
import com.shrayjai.bank.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = RestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class.getName());

    private static final String EXCEPTION_MESSAGE = "{} | {}: {}";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String ref = UUID.randomUUID().toString();
        List<String> validationList = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        LOG.error(EXCEPTION_MESSAGE, ref, e.getClass().getName(), validationList);
        return new ResponseEntity<>(validationList, status);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BankError> handleAccountNotFound(final AccountNotFoundException e) {
        String ref = UUID.randomUUID().toString();
        LOG.error(EXCEPTION_MESSAGE, ref, e.getClass().getName(), e.getMessage());
        return error(e, HttpStatus.NOT_FOUND, ref);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<BankError> handleInvalidRequest(final InvalidRequestException e) {
        String ref = UUID.randomUUID().toString();
        LOG.error(EXCEPTION_MESSAGE, ref, e.getClass().getName(), e.getMessage());
        return error(e, HttpStatus.BAD_REQUEST, ref);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<BankError> handleInsufficientBalance(final InsufficientBalanceException e) {
        String ref = UUID.randomUUID().toString();
        LOG.error(EXCEPTION_MESSAGE, ref, e.getClass().getName(), e.getMessage());
        return error(e, HttpStatus.BAD_REQUEST, ref);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BankError> handleException(final Exception e) {
        String ref = UUID.randomUUID().toString();
        LOG.error(EXCEPTION_MESSAGE, ref, e.getClass().getName(), e.getMessage());
        return error(e, HttpStatus.INTERNAL_SERVER_ERROR, ref);
    }

    private ResponseEntity<BankError> error(final Exception exception, final HttpStatus httpStatus, final String ref) {
        final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        return new ResponseEntity<>(new BankError(ref, message), httpStatus);
    }
}
