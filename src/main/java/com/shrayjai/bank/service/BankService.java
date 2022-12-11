package com.shrayjai.bank.service;

import com.shrayjai.bank.exception.AccountNotFoundException;
import com.shrayjai.bank.exception.InsufficientBalanceException;
import com.shrayjai.bank.exception.InvalidRequestException;
import com.shrayjai.bank.model.*;

public interface BankService {

    BalanceResponse viewAccountBalance(String accountNumber) throws AccountNotFoundException;

    TransactionResponse depositCash(TransactionRequest request) throws AccountNotFoundException;

    TransactionResponse withdrawCash(TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException;

    TransferResponse transferMoney(TransferRequest request) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException;
}
