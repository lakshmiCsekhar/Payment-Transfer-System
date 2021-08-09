package com.org.ibpts.service;

import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.ConfirmationResponse;
import com.org.ibpts.response.PaymentTransferResponse;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface PaymentTransferService {
    PaymentTransferResponse createPaymentTransfer(PaymentTransferRequest request);

    ConfirmationResponse confirmTransfer(String debtorId, String creditorId, BigInteger creditorAccountNumber,
                                         String reference, BigDecimal amount, String type);

}
