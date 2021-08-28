package com.org.ibpts.request.constraints.validator;

import com.org.ibpts.utils.AccountNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class AccountNumberConstraintValidatorTest {

    AccountNumberValidator accountNumberValidator = new AccountNumberValidator();
    AccountNumberConstraintValidator validator = new AccountNumberConstraintValidator(accountNumberValidator);


    @Test
    void testIsValid() {
        boolean result = validator.isValid(new BigInteger("5678909876"), null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsInValid() {
        boolean result = validator.isValid(new BigInteger("22"), null);
        Assertions.assertEquals(false, result);
    }

    @Test
    void testIsInValidIfNull() {
        boolean result = validator.isValid(null, null);
        Assertions.assertEquals(false, result);
    }

    @Test
    void testIsInValidIfZero() {
        boolean result = validator.isValid(new BigInteger("0"), null);
        Assertions.assertEquals(false, result);
    }

}
