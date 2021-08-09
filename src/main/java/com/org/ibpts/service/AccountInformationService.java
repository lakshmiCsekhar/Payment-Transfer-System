package com.org.ibpts.service;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.AccountBalanceResponse;

import java.math.BigInteger;

public interface AccountInformationService {

    AccountBalanceResponse getAccountBalanceResponse(BigInteger accountNumber) throws ApiException;

}
