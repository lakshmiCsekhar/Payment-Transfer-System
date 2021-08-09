package com.org.ibpts.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AccountBalanceResponse{

    @Schema(description = "Account holder name.")
    private String name;
    @Schema(description = "Account number")
    private BigInteger accountNumber;
    @Schema(description = "Account balance")
    private BigDecimal balance;
    @Schema(description = "Account currency")
    private String currency;
    @Schema(description = "External response")
    private String message;
    @Schema(description = "Account Status")
    private Boolean isActive;

    public AccountBalanceResponse() {
    }

    public AccountBalanceResponse(String message) {
        this.message = message;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
