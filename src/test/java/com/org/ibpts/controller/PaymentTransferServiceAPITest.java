package com.org.ibpts.controller;

import com.org.ibpts.constants.TransactionType;
import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.ConfirmationResponse;
import com.org.ibpts.response.PaymentTransferResponse;
import com.org.ibpts.service.PaymentTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PaymentTransferServiceAPITest {

    @InjectMocks
    PaymentTransferServiceAPI paymentTransferServiceAPI;

    @Mock
    PaymentTransferService paymentTransferService;

    @Test
    void testCreatePaymentTransfer() {
        PaymentTransferResponse serviceResponse = new PaymentTransferResponse();
        serviceResponse.setSigningUrl("http://localhost:8080/v1/payment/confirm/?debtorId=1&creditorId=5&reference=fpZnKVTOaB&transactionDate=avc&amount=100&type=DEBIT");
        serviceResponse.setMessage("Payment Awaiting");
        serviceResponse.setReference("a1234");
        serviceResponse.setStatus("AWAITING_CONFIRMATION");
        Mockito.when(paymentTransferService.createPaymentTransfer(any(PaymentTransferRequest.class))).thenReturn(serviceResponse);


        ResponseEntity response = paymentTransferServiceAPI.createPaymentTransfer(new PaymentTransferRequest());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testCreatePaymentTransferError() {
        Mockito.when(paymentTransferService.createPaymentTransfer(any(PaymentTransferRequest.class))).thenThrow(new RuntimeException("some random service error with user friendly message"));
        ResponseEntity response = paymentTransferServiceAPI.createPaymentTransfer(new PaymentTransferRequest());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConfirmTransfer() {
        ConfirmationResponse response = new ConfirmationResponse();
        response.setMessage("Success");
        response.setReference("a12345");
        String debtorId = "5";
        String creditorId = "1";
        BigInteger creditorAccountNumber = new BigInteger("93849934343");
        String reference = "a12333";
        BigDecimal amount = new BigDecimal("1000");
        String type = TransactionType.DEBIT.toString();
        Mockito.when(paymentTransferService.confirmTransfer(debtorId, creditorId, creditorAccountNumber, reference, amount, type)).thenReturn(response);

        ResponseEntity apiResponse = paymentTransferServiceAPI.confirmTransfer(debtorId, creditorId, creditorAccountNumber, reference, amount, type);
        Assertions.assertNotNull(apiResponse);
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
    }


}
