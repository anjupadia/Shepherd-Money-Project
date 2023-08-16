package com.shepherdmoney.interviewproject.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory implements Comparable<BalanceHistory> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Instant date;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCard creditCard;

    private double balance;

    @Override
    public int compareTo(BalanceHistory balanceHistory) {
        return this.date.compareTo(balanceHistory.date);
    }
}
