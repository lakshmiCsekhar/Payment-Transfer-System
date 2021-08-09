package com.org.ibpts.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

public class AccounNumberValidatorTest {

    AccountNumberValidator accountNumberValidator = new AccountNumberValidator();

    @Test
    void testValidAccountNumber() {
        BigInteger validAccountNumber = new BigInteger("8299292922");
        boolean result = accountNumberValidator.validate(validAccountNumber);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testValidAccountNumberZero() {
        BigInteger validAccountNumber = new BigInteger("0");
        boolean result = accountNumberValidator.validate(validAccountNumber);
        Assertions.assertEquals(false, result);
    }

    @Test
    void testInvalidAccountNumber() {
        BigInteger inValidAccountNumber = new BigInteger("33");
        boolean result = accountNumberValidator.validate(inValidAccountNumber);
        Assertions.assertEquals(false, result);
    }
}
