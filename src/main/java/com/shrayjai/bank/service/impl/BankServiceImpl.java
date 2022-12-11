package com.shrayjai.bank.service.impl;

import com.shrayjai.bank.data.Accounts;
import com.shrayjai.bank.data.AccountsRepository;
import com.shrayjai.bank.exception.AccountNotFoundException;
import com.shrayjai.bank.exception.InsufficientBalanceException;
import com.shrayjai.bank.exception.InvalidRequestException;
import com.shrayjai.bank.model.*;
import com.shrayjai.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private AccountsRepository accountsRepository;

    private static final String ACCOUNT_NOT_FOUND = "account does not exist";

    private static final String SAME_ACCOUNT = "benefactor and beneficiary must be different";

    private static final String LOW_BALANCE = "low balance";

    private static final String SUCCESS = "success";

    @Override
    public BalanceResponse viewAccountBalance(String accountNumber) throws AccountNotFoundException {

        Accounts account = accountsRepository.findById(accountNumber).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        return BalanceResponse.builder().accountNumber(account.getAccountNumber()).balance(account.getBalance()).build();
    }

    @Override
    public TransactionResponse depositCash(TransactionRequest request) throws AccountNotFoundException {

        Accounts account = accountsRepository.findById(request.getAccountNumber()).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        BigDecimal previousBalance = account.getBalance();
        BigDecimal updatedBalance = previousBalance.add(request.getAmount());

        account.setBalance(updatedBalance);

        accountsRepository.saveAndFlush(account);

        return TransactionResponse.builder().accountNumber(account.getAccountNumber()).previousBalance(previousBalance).updatedBalance(updatedBalance).build();
    }

    @Override
    public TransactionResponse withdrawCash(TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException {

        Accounts account = accountsRepository.findById(request.getAccountNumber()).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        BigDecimal previousBalance = account.getBalance();
        BigDecimal updatedBalance = previousBalance.subtract(request.getAmount());

        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) throw new InsufficientBalanceException(LOW_BALANCE);

        account.setBalance(updatedBalance);

        accountsRepository.saveAndFlush(account);

        return TransactionResponse.builder().accountNumber(account.getAccountNumber()).previousBalance(previousBalance).updatedBalance(updatedBalance).build();
    }

    @Override
    public TransferResponse transferMoney(TransferRequest request) throws AccountNotFoundException, InvalidRequestException, InsufficientBalanceException {

        String benefactor = request.getBenefactor();
        String beneficiary = request.getBeneficiary();

        Accounts benefactorAccount = accountsRepository.findById(benefactor).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        Accounts beneficiaryAccount = accountsRepository.findById(beneficiary).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        if (benefactorAccount.getCustomerId().equals(beneficiaryAccount.getCustomerId()))
            throw new InvalidRequestException(SAME_ACCOUNT);

        BigDecimal benefactorNewBalance = benefactorAccount.getBalance().subtract(request.getAmount());
        benefactorAccount.setBalance(benefactorNewBalance);

        if (benefactorNewBalance.compareTo(BigDecimal.ZERO) < 0) throw new InsufficientBalanceException(LOW_BALANCE);

        BigDecimal beneficiaryNewBalance = beneficiaryAccount.getBalance().add(request.getAmount());
        beneficiaryAccount.setBalance(beneficiaryNewBalance);

        accountsRepository.saveAndFlush(benefactorAccount);
        accountsRepository.saveAndFlush(beneficiaryAccount);

        return TransferResponse.builder().beneficiary(beneficiary).amount(request.getAmount()).status(SUCCESS).build();
    }
}
