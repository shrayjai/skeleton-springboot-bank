package com.shrayjai.bank.controller;

import com.shrayjai.bank.exception.AccountNotFoundException;
import com.shrayjai.bank.exception.InsufficientBalanceException;
import com.shrayjai.bank.exception.InvalidRequestException;
import com.shrayjai.bank.model.*;
import com.shrayjai.bank.service.impl.BankServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class.getName());

    private static final String RESPONSE_MESSAGE = "Response sent successfully";

    @Autowired
    private BankServiceImpl bankService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> balance(@RequestHeader String accountNumber) throws AccountNotFoundException {

        LOG.info("Requested /balance >>> {}", accountNumber);

        BalanceResponse balance = bankService.viewAccountBalance(accountNumber);

        LOG.info(RESPONSE_MESSAGE);

        return ResponseEntity.ok().body(balance);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest body) throws AccountNotFoundException {

        LOG.info("Requested /deposit >>> {}", body);

        TransactionResponse txnDeposit = bankService.depositCash(body);

        LOG.info(RESPONSE_MESSAGE);

        return ResponseEntity.ok().body(txnDeposit);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest body) throws AccountNotFoundException, InsufficientBalanceException {

        LOG.info("Requested /withdraw >>> {}", body);

        TransactionResponse txnWithdraw = bankService.withdrawCash(body);

        LOG.info(RESPONSE_MESSAGE);

        return ResponseEntity.ok().body(txnWithdraw);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest body) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        LOG.info("Requested /transfer >>> {}", body);

        TransferResponse txnTransfer = bankService.transferMoney(body);

        LOG.info(RESPONSE_MESSAGE);

        return ResponseEntity.ok().body(txnTransfer);
    }
}
