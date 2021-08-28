package com.org.ibpts.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table
public class Transactions {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column
    private String reference;
    @Column(nullable = false)
    private BigInteger creditorAccountNumber;
    @Column(nullable = false)
    private String debtorId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(columnDefinition = "DATETIME")
    private Date transactionDate;
    @Column
    private String status;
    @Column
    private String type;
    @Column
    private Boolean isInternationalTransaction;

    public Transactions() {
    }

    public Transactions(BigInteger creditorAccountNumber, String debtorId, BigDecimal amount, Date transactionDate,
                        String status, String type, Boolean isInternationalTransaction) {
        this.creditorAccountNumber = creditorAccountNumber;
        this.debtorId = debtorId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.type = type;
        this.isInternationalTransaction = isInternationalTransaction;
    }

    public Transactions(String reference, String status) {
        this.reference = reference;
        this.status = status;
    }

    public BigInteger getCreditorAccountNumber() {
        return creditorAccountNumber;
    }

    public void setCreditorAccountNumber(BigInteger creditorAccountNumber) {
        this.creditorAccountNumber = creditorAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInternationalTransaction() {
        return isInternationalTransaction;
    }

    public void setInternationalTransaction(Boolean internationalTransaction) {
        isInternationalTransaction = internationalTransaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
