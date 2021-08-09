package com.org.ibpts.response;

public class PaymentTransferResponse {
    private String reference;
    private String status;
    private String internationalTransactionFee;
    private String signingUrl;
    private String message;

    public PaymentTransferResponse() {
    }

    public PaymentTransferResponse(String message) {
        this.message = message;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSigningUrl() {
        return signingUrl;
    }

    public void setSigningUrl(String signingUrl) {
        this.signingUrl = signingUrl;
    }

    public String getInternationalTransactionFee() {
        return internationalTransactionFee;
    }

    public void setInternationalTransactionFee(String internationalTransactionFee) {
        this.internationalTransactionFee = internationalTransactionFee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
