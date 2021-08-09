package com.org.ibpts.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Transaction {
    private BigInteger creditorAccountNumber;
    private BigDecimal amount;
    private Date transactionDate;
    private String reference;
    private String status;
    private String message;
    private Boolean isInternationalTransaction;

    public Transaction() {
    }

    public Transaction(String message) {
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
