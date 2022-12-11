package com.shrayjai.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotBlank(message = "accountNumber must not be blank")
    private String accountNumber;

    @Positive(message = "amount must be greater than 0")
    private BigDecimal amount;
}
