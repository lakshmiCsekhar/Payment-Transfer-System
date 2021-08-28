package com.org.ibpts.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountNumberValidator {

    private static final String regex = "[0-9]{6,20}+";
    private static final Logger log = Logger.getLogger(AccountNumberValidator.class);

    public boolean validate(BigInteger accountNumber) {

        if (accountNumber == null || accountNumber.compareTo(BigInteger.ZERO) == 0) {
            log.info("Account Number is invalid / Zero");
            return false;
        } else {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(accountNumber));
            return matcher.matches();
            //logic for IBAN validation/ country specific account number

        }
    }

}
