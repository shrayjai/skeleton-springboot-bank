package com.shrayjai.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BalanceResponse {

    private String accountNumber;

    private BigDecimal balance;
}
