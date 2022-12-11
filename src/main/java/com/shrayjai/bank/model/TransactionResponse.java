package com.shrayjai.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class TransactionResponse {

    private String accountNumber;

    private BigDecimal previousBalance;

    private BigDecimal updatedBalance;
}
