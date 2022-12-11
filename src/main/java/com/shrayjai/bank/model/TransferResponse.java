package com.shrayjai.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class TransferResponse {

    private String beneficiary;

    private BigDecimal amount;

    private String status;
}
