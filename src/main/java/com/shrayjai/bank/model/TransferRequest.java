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
public class TransferRequest {

    @NotBlank(message = "benefactor must not be blank")
    private String benefactor;

    @NotBlank(message = "beneficiary must not be blank")
    private String beneficiary;

    @Positive(message = "amount must be greater than 0")
    private BigDecimal amount;
}
