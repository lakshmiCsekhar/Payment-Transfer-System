package com.org.ibpts.service;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.model.Account;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.response.AccountBalanceResponse;
import com.org.ibpts.service.impl.AccountInformationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AccountInformationServiceImplTest {

    @InjectMocks
    AccountInformationService accountInformationService = new AccountInformationServiceImpl();

    @Mock
    AccountInformationRepository accountInformationRepository;

    @DisplayName("Should get correct account balance")
    @Test
    void shoudGetAccountBalanceResponse() throws ApiException {
        Account account = new Account();
        account.setId("12345");
        account.setName("TestAccount");
        account.setBalance(new BigDecimal("23000.5"));
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenReturn(Optional.of(account));

        AccountBalanceResponse expected = new AccountBalanceResponse();
        expected.setBalance(BigDecimal.valueOf(23000.50));
        BigInteger input = new BigInteger("90876723492827");
        AccountBalanceResponse actualOutput = accountInformationService.getAccountBalanceResponse(input);
        assertEquals(expected.getBalance(), actualOutput.getBalance());
    }

    @Test
    @DisplayName("Test Api Exception with non existing account number")
    void testAccountNotPresentError() {
        Optional<Account> notExistingAccount = Optional.empty();
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenReturn(notExistingAccount);
        assertThrows(ApiException.class,() -> {
            accountInformationService.getAccountBalanceResponse(any(BigInteger.class));
        });
    }

    @Test
    @DisplayName("Test Api Exception")
    void testApiException() {
        when(accountInformationRepository.findByAccountNumber(any(BigInteger.class))).thenThrow(new RuntimeException("a random error"));
        assertThrows(ApiException.class,() -> {
            accountInformationService.getAccountBalanceResponse(any(BigInteger.class));
        });
    }
}
