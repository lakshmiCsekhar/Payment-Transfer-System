package com.org.ibpts.request;

import com.org.ibpts.constants.TransactionType;
import com.org.ibpts.request.constraints.AccountNumberConstraint;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class PaymentTransferRequest {

    @Schema(description = "amount to be transferred.", example = "1000", required = true)
    private BigDecimal amount;

    @Schema(description = "Debitor account number. Should be within  6-20 character's.", example = "90876723492827", required = true)
    @AccountNumberConstraint
    private BigInteger debtorAccountNumber;

    @Schema(description = "Creditor account number. Should be within  6-20 character's.", example = "90876723492827", required = true)
    @AccountNumberConstraint
    private BigInteger creditorAccountNumber;

    @Schema(description = "Name of creditor.", example = "John Meyers", required = true)
    @NotBlank(message = "Name is mandatory")
    private String creditorName;

    @Schema(description = "If not provided defaults to today's date.", example = "2018-02-21 15:30:14.332", required = false)
    private Date transferDate;

    @Schema(description = "DEBIT/CREDIT", required = true)
    private TransactionType type;

    @Schema(description = "Defaults to false. Indicate whether transaction is international or not.", required = true, defaultValue = "false")
    private boolean isInternationalTransfer;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigInteger getDebtorAccountNumber() {
        return debtorAccountNumber;
    }

    public void setDebtorAccountNumber(BigInteger debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    public BigInteger getCreditorAccountNumber() {
        return creditorAccountNumber;
    }

    public void setCreditorAccountNumber(BigInteger creditorAccountNumber) {
        this.creditorAccountNumber = creditorAccountNumber;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public boolean isInternationalTransfer() {
        return isInternationalTransfer;
    }

    public void setInternationalTransfer(boolean internationalTransfer) {
        isInternationalTransfer = internationalTransfer;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
}
