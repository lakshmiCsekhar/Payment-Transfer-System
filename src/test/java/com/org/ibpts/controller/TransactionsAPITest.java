package com.org.ibpts.controller;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.Transaction;
import com.org.ibpts.response.TransactionsResponse;
import com.org.ibpts.service.TransactionService;
import com.org.ibpts.utils.AccountNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class TransactionsAPITest {

    @InjectMocks
    TransactionsAPI transactionsAPI;

    @Mock
    TransactionService transactionService;

    @Mock
    AccountNumberValidator accountNumberValidator;

    @Test
    void testGetTransactions() throws ApiException {

        BigInteger validAccoutNumber = new BigInteger("78393883938");
        Transaction mockTransaction = new Transaction();
        mockTransaction.setTransactionDate(new Date());
        mockTransaction.setInternationalTransaction(false);
        mockTransaction.setAmount(new BigDecimal("1000"));
        mockTransaction.setMessage("A good transaction");
        mockTransaction.setReference("a14252");
        mockTransaction.setCreditorAccountNumber(new BigInteger("345678922"));

        Transaction mockTransactionTwo = new Transaction();
        mockTransactionTwo.setTransactionDate(new Date());
        mockTransactionTwo.setInternationalTransaction(false);
        mockTransactionTwo.setAmount(new BigDecimal("10100"));
        mockTransactionTwo.setMessage("Another good transaction");
        mockTransactionTwo.setReference("a1u4252");
        mockTransactionTwo.setCreditorAccountNumber(new BigInteger("3456788922"));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(mockTransaction);
        transactions.add(mockTransactionTwo);
        TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setTransactions(transactions);

        Mockito.when(accountNumberValidator.validate(validAccoutNumber)).thenReturn(true);
        Mockito.when(transactionService.getTransactions(validAccoutNumber)).thenReturn(transactionsResponse);

        ResponseEntity apiResponse = transactionsAPI.getTransactions(validAccoutNumber);
        Assertions.assertNotNull(apiResponse);
        Assertions.assertNotNull(apiResponse.getBody());
        Assertions.assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
        TransactionsResponse transactionsApiResponse = (TransactionsResponse) apiResponse.getBody();
        Assertions.assertNotNull(transactionsApiResponse);
        Assertions.assertNotNull(transactionsApiResponse.getTransactions());
        Assertions.assertEquals(2, transactionsApiResponse.getTransactions().size());


    }

    @Test
    void testGetTransactionsApiError() throws ApiException {
        Mockito.when(accountNumberValidator.validate(ArgumentMatchers.any(BigInteger.class))).thenReturn(true);
        Mockito.when(transactionService.getTransactions(ArgumentMatchers.any(BigInteger.class))).thenThrow(new ApiException("some random service error"));
        ResponseEntity responseEntity = transactionsAPI.getTransactions(new BigInteger("828299292"));
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assertions.assertEquals("Something went wrong :  some random service error", responseEntity.getBody());

    }

    @Test
    void testInvalidAccountNumber() {
        BigInteger invalidAccountNumber = new BigInteger("122");
        Mockito.when(accountNumberValidator.validate(invalidAccountNumber)).thenReturn(false);
        ResponseEntity responseEntity = transactionsAPI.getTransactions(invalidAccountNumber);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals("Invalid account number provided : " + invalidAccountNumber, responseEntity.getBody());

    }

    @Test
    void testGetSingleTransaction() throws ApiException {

        Transaction mockTransaction = new Transaction();
        mockTransaction.setTransactionDate(new Date());
        mockTransaction.setInternationalTransaction(false);
        mockTransaction.setAmount(new BigDecimal("1000"));
        mockTransaction.setMessage("A good transaction");
        mockTransaction.setReference("a14252");
        mockTransaction.setCreditorAccountNumber(new BigInteger("345678922"));

        Mockito.when(transactionService.getTransaction("a14252")).thenReturn(mockTransaction);

        ResponseEntity apiResponse = transactionsAPI.getTransaction("a14252");
        Assertions.assertNotNull(apiResponse);
        Assertions.assertNotNull(apiResponse.getBody());
        Transaction transaction = (Transaction) apiResponse.getBody();
        Assertions.assertEquals("a14252", transaction.getReference());

    }

    @Test
    void testGetSingleTransactionApiError() throws ApiException {
        Mockito.when(transactionService.getTransaction("o000")).thenThrow(new ApiException("some random service error with a nice message"));
        ResponseEntity apiResponse = transactionsAPI.getTransaction("o000");
        Assertions.assertNotNull(apiResponse);
        Assertions.assertNotNull(apiResponse.getBody());
        Assertions.assertEquals("Something went wrong :  some random service error with a nice message", apiResponse.getBody());
    }

}
