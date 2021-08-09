package com.org.ibpts.service;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.model.Account;
import com.org.ibpts.model.Transactions;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.repository.TransactionsRepository;
import com.org.ibpts.response.Transaction;
import com.org.ibpts.response.TransactionsResponse;
import com.org.ibpts.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();

    @Mock
    AccountInformationRepository accountInformationRepository;

    @Mock
    TransactionsRepository transactionsRepository;

    @BeforeEach
    void init() {
        Account account = new Account();
        account.setId("12345");
        account.setName("TestAccount");
        account.setBalance(new BigDecimal("23000.5"));
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenReturn(Optional.of(account));

        List<Transactions> transactionsList = new ArrayList<>();
        Transactions transactionsOne = new Transactions();
        transactionsOne.setDebtorId(account.getId());
        transactionsOne.setAmount(new BigDecimal(1000.00));
        transactionsOne.setReference("testReference");

        Transactions transactionsTwo = new Transactions();
        transactionsOne.setDebtorId(account.getId());
        transactionsOne.setAmount(new BigDecimal(3000.00));

        transactionsList.add(transactionsOne);
        transactionsList.add(transactionsTwo);

        when(transactionsRepository.findByDebtorId(account.getId(), PageRequest.of(0,20))).thenReturn(transactionsList);
        when(transactionsRepository.findByReference("testReference")).thenReturn(transactionsOne);
    }

    @Test
    @DisplayName("Should get transactions list")
    void shouldGetTransactions() throws ApiException {
        TransactionsResponse response = transactionService.getTransactions(new BigInteger("90876723492827"));
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getTransactions());
        Assertions.assertEquals(2, response.getTransactions().size());
    }

    @Test
    @DisplayName("Should get single transaction")
    void shouldGetSingleTransaction() throws ApiException {
        Transaction transaction = transactionService.getTransaction("testReference");
        Assertions.assertNotNull(transaction);
        Assertions.assertNotNull(transaction.getReference());
        Assertions.assertEquals("testReference", transaction.getReference());
    }

    @Test
    void shouldThrowApiErrorForListTransactions() {
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenThrow(new RuntimeException("a system error"));
        Assertions.assertThrows(ApiException.class, () -> {
            transactionService.getTransactions(any(BigInteger.class));
        });
    }

    @Test
    void testDebtorNotPresent() {
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(ApiException.class, () -> {
            transactionService.getTransactions(any(BigInteger.class));
        });
    }
}
