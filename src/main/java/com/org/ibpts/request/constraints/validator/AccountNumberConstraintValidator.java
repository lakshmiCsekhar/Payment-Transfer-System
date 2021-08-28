package com.org.ibpts.request.constraints.validator;

import com.org.ibpts.request.constraints.AccountNumberConstraint;
import com.org.ibpts.utils.AccountNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class AccountNumberConstraintValidator implements ConstraintValidator<AccountNumberConstraint, BigInteger> {

    AccountNumberValidator validator;

    @Autowired
    public AccountNumberConstraintValidator(AccountNumberValidator validator) {
        this.validator = validator;
    }


    @Override
    public void initialize(AccountNumberConstraint accountNumber) {
    }

    @Override
    public boolean isValid(BigInteger accountNumber,
                           ConstraintValidatorContext cxt) {
        return validator.validate(accountNumber);
    }
}
