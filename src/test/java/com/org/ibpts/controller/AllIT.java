package com.org.ibpts.controller;

import com.org.ibpts.constants.TransactionType;
import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    void testEveryServiceInSequence() {
        PaymentTransferRequest paymentTransferRequest = new PaymentTransferRequest();
        paymentTransferRequest.setTransferDate(new Date());
        paymentTransferRequest.setInternationalTransfer(false);
        paymentTransferRequest.setAmount(new BigDecimal("100.0"));
        paymentTransferRequest.setType(TransactionType.DEBIT);
        paymentTransferRequest.setDebtorAccountNumber(new BigInteger("90876723492827"));
        paymentTransferRequest.setCreditorAccountNumber(new BigInteger("90856456564"));
        paymentTransferRequest.setCreditorName("A nice name");
        HttpEntity<PaymentTransferRequest> entity = new HttpEntity<PaymentTransferRequest>(paymentTransferRequest, headers);
        ResponseEntity<PaymentTransferResponse> response = restTemplate.exchange("http://localhost:" + port + "/v1/payment/transfer", HttpMethod.POST, entity, PaymentTransferResponse.class);
        Assertions.assertNotNull(response);

        PaymentTransferResponse ptResponse = response.getBody();
        Map<String, String> params = queryParamsToMap(ptResponse.getSigningUrl().split("\\?")[1]);

        StringBuilder url = new StringBuilder("http://localhost:" + port + "/v1/payment/confirm?");
        url.append("debtorId=").append(params.get("debtorId"));
        url.append("&creditorId=").append(params.get("creditorId"));
        url.append("&reference=").append(params.get("reference"));
        url.append("&amount=").append(params.get("amount"));
        url.append("&type=DEBIT");

        ResponseEntity<ConfirmationResponse> confirm = restTemplate.getForEntity(url.toString(), ConfirmationResponse.class);
        Assertions.assertNotNull(confirm);

        AccountBalanceResponse accountBalanceResponse = this.restTemplate.getForObject("http://localhost:" + port + "/v1/account/90876723492827/balance", AccountBalanceResponse.class);
        assertNotNull(accountBalanceResponse);
        assertEquals(new BigDecimal("200.45"), accountBalanceResponse.getBalance());

        TransactionsResponse getTransactions = this.restTemplate.getForObject("http://localhost:" + port + "/v1/transactions/90876723492827/mini", TransactionsResponse.class);
        assertNotNull(getTransactions);
        assertEquals(20, getTransactions.getTransactions().size());

        Transaction getTransaction = this.restTemplate.getForObject("http://localhost:" + port + "/v1/transactions/single/sdssdsa", Transaction.class);
        assertNotNull(getTransaction);
        assertEquals("sdssdsa", getTransaction.getReference());

    }

    private Map<String, String> queryParamsToMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
