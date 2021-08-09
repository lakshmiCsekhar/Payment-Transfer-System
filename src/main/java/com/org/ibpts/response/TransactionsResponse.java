package com.org.ibpts.response;

import java.math.BigInteger;
import java.util.List;

public class TransactionsResponse {

    List<Transaction> transactions;
    private BigInteger debtorAccountNumber;
    private int numberOfTransactions;
    private String message;

    public TransactionsResponse() {
    }

    public TransactionsResponse(List<Transaction> transactions, BigInteger debtorAccountNumber, int numberOfTransactions, String message) {
        this.transactions = transactions;
        this.debtorAccountNumber = debtorAccountNumber;
        this.numberOfTransactions = numberOfTransactions;
        this.message = message;
    }

    public TransactionsResponse(String message) {
        this.message = message;
    }

    public BigInteger getDebtorAccountNumber() {
        return debtorAccountNumber;
    }

    public void setDebtorAccountNumber(BigInteger debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
