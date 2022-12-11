package com.shrayjai.bank.data;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_ACCOUNTS")
public class Customer {

    @Id
    @Column(name = "CID")
    private Long customerId;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Accounts> accounts = new ArrayList<>();
}
