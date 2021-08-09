package com.org.ibpts.controller;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.AccountBalanceResponse;
import com.org.ibpts.service.AccountInformationService;
import com.org.ibpts.utils.AccountNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;

@ExtendWith(SpringExtension.class)
public class AccountInformationServiceAPITest {

    @InjectMocks
    AccountInformationServiceAPI accountInformationServiceAPI;


    @Mock
    AccountInformationService accountInformationService;

    @Mock
    AccountNumberValidator accountNumberValidator;

    @BeforeEach
    void mockMethods() throws ApiException {

    }

    @Test
    void testGetAccoutnBalance() throws ApiException {

        BigInteger accountNumber = new BigInteger("90876723492827");
        AccountBalanceResponse mockResponse = new AccountBalanceResponse();
        mockResponse.setBalance(new BigDecimal("1000.00"));
        mockResponse.setAccountNumber(accountNumber);
        Mockito.when(accountNumberValidator.validate(new BigInteger("90876723492827"))).thenReturn(true);
        Mockito.when(accountInformationService.getAccountBalanceResponse(new BigInteger("90876723492827"))).thenReturn(mockResponse);


        ResponseEntity apiResponse = accountInformationServiceAPI.getAccountBalance(accountNumber);
        Assertions.assertNotNull(apiResponse);
        AccountBalanceResponse response = (AccountBalanceResponse) apiResponse.getBody();
        Assertions.assertEquals(new BigDecimal("1000.00"), response.getBalance());
    }

    @Test
    void testInvalidAccountNumber() {
        Mockito.when(accountNumberValidator.validate(new BigInteger("90876723492827"))).thenReturn(false);
        BigInteger accountNumber = new BigInteger("55");
        ResponseEntity apiResponse = accountInformationServiceAPI.getAccountBalance(accountNumber);
        Assertions.assertNotNull(apiResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatusCode());
        Assertions.assertEquals("Invalid account number provided : " + accountNumber, apiResponse.getBody());
    }

    @Test
    void testApiException() throws ApiException {
        BigInteger accountNumber = new BigInteger("90876723492827");
        Mockito.when(accountNumberValidator.validate(new BigInteger("90876723492827"))).thenReturn(true);
        Mockito.when(accountInformationService.getAccountBalanceResponse(new BigInteger("90876723492827"))).thenThrow(new ApiException("a random service error"));
        ResponseEntity apiResponse = accountInformationServiceAPI.getAccountBalance(accountNumber);
        Assertions.assertNotNull(apiResponse);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiResponse.getStatusCode());
        Assertions.assertEquals("a random service error", apiResponse.getBody());
    }


}
