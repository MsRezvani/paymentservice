package com.digipay.paymentservice.paymentservice.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_transaction_type")
public class TransactionType {

    @Id
    @GeneratedValue
    private int id;
    private boolean active;
    private String code;
    private String title;
    private String constantCode;
}
