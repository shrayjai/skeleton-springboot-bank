package com.shrayjai.bank.data;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "TBL_ACCOUNTS")
public class Accounts {

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "CID")
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
}
