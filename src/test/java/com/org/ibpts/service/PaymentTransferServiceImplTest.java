package com.org.ibpts.service;

import com.org.ibpts.constants.TransactionType;
import com.org.ibpts.model.Account;
import com.org.ibpts.model.Transactions;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.repository.TransactionsRepository;
import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.ConfirmationResponse;
import com.org.ibpts.response.PaymentTransferResponse;
import com.org.ibpts.service.impl.PaymentTransferServiceImpl;
import com.org.ibpts.utils.AccountNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PaymentTransferServiceImplTest {

    @InjectMocks
    PaymentTransferService paymentTransferService = new PaymentTransferServiceImpl();

    @Mock
    AccountInformationRepository accountInformationRepository;

    @Mock
    TransactionsRepository transactionsRepository;

    @Mock
    AccountNumberValidator accountNumberValidator;

    @BeforeEach
    void beforeAll() {
        when(accountNumberValidator.validate(any(BigInteger.class))).thenReturn(true);
    }

    @DisplayName("shouldCreatePaymentTransfer")
    @Test
    void shouldCreatePaymentTransfer() {
        Account debtorAccount = new Account();
        debtorAccount.setId("12345");
        debtorAccount.setName("DebitorAccount");
        debtorAccount.setBalance(new BigDecimal("23000.5"));
        debtorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("90876723492827"))).thenReturn(Optional.of(debtorAccount));

        Account creditorAccount = new Account();
        creditorAccount.setId("93839");
        creditorAccount.setName("CreditorAccount");
        creditorAccount.setBalance(new BigDecimal("23000.5"));
        creditorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("8787656557"))).thenReturn(Optional.of(creditorAccount));


        PaymentTransferResponse expectedOutput = new PaymentTransferResponse();
        expectedOutput.setStatus("AWAITING_CONFIRMATION");
        expectedOutput.setSigningUrl("http://localhost:8080/v1/payment/confirm/?debtorId=12345&creditorId=93839&reference=fpZnKVTOaB&transactionDate=avc&amount=100&type=DEBIT");
        PaymentTransferRequest input = new PaymentTransferRequest();
        input.setAmount(new BigDecimal(100));
        input.setDebtorAccountNumber(new BigInteger("90876723492827"));
        input.setCreditorAccountNumber(new BigInteger("8787656557"));
        input.setCreditorName("Johny Depp");
        input.setTransferDate(new Date());
        input.setInternationalTransfer(false);
        input.setType(TransactionType.DEBIT);
        PaymentTransferResponse actualOutput = paymentTransferService.createPaymentTransfer(input);
        assertNotNull(actualOutput);
        assertEquals(expectedOutput.getStatus(), actualOutput.getStatus());
        Map<String, String> actualParams = queryParamsToMap(actualOutput.getSigningUrl());
        Map<String, String> expectedParams = queryParamsToMap(expectedOutput.getSigningUrl());
        assertEquals(expectedParams.get("creditorId"), actualParams.get("creditorId"));
        assertEquals(expectedParams.get("debtorId"), actualParams.get("debtorId"));
        assertEquals(expectedParams.get("amount"), actualParams.get("amount"));
    }

    @DisplayName("shouldCreatePaymentTransferIfCreditorNotPresent")
    @Test
    void shouldCreatePaymentTransferCreditorAccountNotPresent() {
        Account debtorAccount = new Account();
        debtorAccount.setId("12345");
        debtorAccount.setName("DebitorAccount");
        debtorAccount.setBalance(new BigDecimal("23000.5"));
        debtorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("90876723492827"))).thenReturn(Optional.of(debtorAccount));


        when(accountInformationRepository.findByAccountNumber(new BigInteger("8787656557"))).thenReturn(Optional.empty());


        PaymentTransferResponse expectedOutput = new PaymentTransferResponse();
        expectedOutput.setStatus("AWAITING_CONFIRMATION");
        expectedOutput.setSigningUrl("http://localhost:8080/v1/payment/confirm/?debtorId=12345&creditorId=93839&reference=fpZnKVTOaB&transactionDate=avc&amount=100&type=DEBIT");
        PaymentTransferRequest input = new PaymentTransferRequest();
        input.setAmount(new BigDecimal(100));
        input.setDebtorAccountNumber(new BigInteger("90876723492827"));
        input.setCreditorAccountNumber(new BigInteger("8787656557"));
        input.setCreditorName("Johny Depp");
        input.setTransferDate(new Date());
        input.setInternationalTransfer(false);
        input.setType(TransactionType.DEBIT);
        PaymentTransferResponse actualOutput = paymentTransferService.createPaymentTransfer(input);
        assertNotNull(actualOutput);
        assertEquals(expectedOutput.getStatus(), actualOutput.getStatus());
        Map<String, String> actualParams = queryParamsToMap(actualOutput.getSigningUrl());
        Map<String, String> expectedParams = queryParamsToMap(expectedOutput.getSigningUrl());
        assertEquals("8787656557", actualParams.get("creditorAccountNumber"));
        assertEquals(expectedParams.get("debtorId"), actualParams.get("debtorId"));
        assertEquals(expectedParams.get("amount"), actualParams.get("amount"));
    }

    @DisplayName("shouldCreateInternationalPaymentTransfer")
    @Test
    void shouldCreateInternationalPaymentTransfer() {
        //internationalFees = 10;
        Account debtorAccount = new Account();
        debtorAccount.setId("12345");
        debtorAccount.setName("DebitorAccount");
        debtorAccount.setBalance(new BigDecimal("23000.5"));
        debtorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("90876723492827"))).thenReturn(Optional.of(debtorAccount));

        Account creditorAccount = new Account();
        creditorAccount.setId("93839");
        creditorAccount.setName("CreditorAccount");
        creditorAccount.setBalance(new BigDecimal("23000.5"));
        creditorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("8787656557"))).thenReturn(Optional.of(creditorAccount));


        PaymentTransferResponse expectedOutput = new PaymentTransferResponse();
        expectedOutput.setStatus("AWAITING_CONFIRMATION");
        expectedOutput.setSigningUrl("http://localhost:8080/v1/payment/confirm/?debtorId=12345&creditorId=93839&reference=fpZnKVTOaB&transactionDate=avc&amount=110&type=DEBIT");
        PaymentTransferRequest input = new PaymentTransferRequest();
        input.setAmount(new BigDecimal(100));
        input.setDebtorAccountNumber(new BigInteger("90876723492827"));
        input.setCreditorAccountNumber(new BigInteger("8787656557"));
        input.setCreditorName("Johny Depp");
        input.setTransferDate(new Date());
        input.setInternationalTransfer(true);
        input.setType(TransactionType.DEBIT);
        PaymentTransferResponse actualOutput = paymentTransferService.createPaymentTransfer(input);
        assertNotNull(actualOutput);
        assertEquals(expectedOutput.getStatus(), actualOutput.getStatus());
        Map<String, String> actualParams = queryParamsToMap(actualOutput.getSigningUrl());
        Map<String, String> expectedParams = queryParamsToMap(expectedOutput.getSigningUrl());
        assertEquals(expectedParams.get("creditorId"), actualParams.get("creditorId"));
        assertEquals(expectedParams.get("debtorId"), actualParams.get("debtorId"));
        assertEquals(expectedParams.get("amount"), actualParams.get("amount"));
    }

    public static Map<String, String> queryParamsToMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    @Test
    void testInSufficientFund() {

        Account debtorAccount = new Account();
        debtorAccount.setId("12345");
        debtorAccount.setName("DebitorAccount");
        debtorAccount.setBalance(new BigDecimal("33"));
        debtorAccount.setIsActive(true);
        when(accountInformationRepository.findByAccountNumber(new BigInteger("90876723492827"))).thenReturn(Optional.of(debtorAccount));

        PaymentTransferRequest paymentTransferRequest = new PaymentTransferRequest();
        paymentTransferRequest.setAmount(new BigDecimal("1000"));
        paymentTransferRequest.setDebtorAccountNumber(new BigInteger("90876723492827"));
        PaymentTransferResponse response = paymentTransferService.createPaymentTransfer(paymentTransferRequest);

        assertEquals("ERROR", response.getStatus());
        assertTrue(response.getMessage().startsWith("Insufficient funds"));

    }

    @Test
    void debtorNotPresent() {
        PaymentTransferRequest request = new PaymentTransferRequest();
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenReturn(Optional.empty());
        PaymentTransferResponse response = paymentTransferService.createPaymentTransfer(request);
        assertEquals("ERROR", response.getStatus());
        assertTrue(response.getMessage().startsWith("Debtor is not registered"));
    }

    @Test
    void testConfirmTransfer() {

        String debitorId = "13";
        String creditorId = "12";
        Account creditorAccount = new Account();
        creditorAccount.setIsActive(true);
        creditorAccount.setAccountNumber(new BigInteger("5678987822"));
        BigDecimal debtorBalance = new BigDecimal("1000");
        BigDecimal creditorBalance = new BigDecimal("1000");
        when(accountInformationRepository.getAccountBalance(debitorId)).thenReturn(debtorBalance);
        doNothing().when(accountInformationRepository).updateBalanceForAccount(debtorBalance, debitorId);
        when(accountInformationRepository.findById(creditorId)).thenReturn(Optional.of(creditorAccount));
        when(accountInformationRepository.getAccountBalance(creditorId)).thenReturn(creditorBalance);
        doNothing().when(accountInformationRepository).updateBalanceForAccount(debtorBalance, creditorId);
        Transactions transactions = new Transactions();
        when(transactionsRepository.findByReference(anyString())).thenReturn(transactions);
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transactions);

        ConfirmationResponse response = paymentTransferService.confirmTransfer(debitorId, creditorId, creditorAccount.getAccountNumber(), "a1233", new BigDecimal("122"), TransactionType.DEBIT.toString());
        assertNotNull(response);
        assertEquals("a1233", response.getReference());
    }
}
