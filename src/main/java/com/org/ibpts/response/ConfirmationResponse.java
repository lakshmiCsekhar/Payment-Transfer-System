package com.org.ibpts.response;

public class ConfirmationResponse {
    private String reference;
    private String message;

    public ConfirmationResponse() {
    }

    public ConfirmationResponse(String reference, String message) {
        this.reference = reference;
        this.message = message;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
