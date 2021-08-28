package com.org.ibpts.service.impl;


import com.org.ibpts.constants.APIConstants;
import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.model.Account;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.response.AccountBalanceResponse;
import com.org.ibpts.service.AccountInformationService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;


@Service
public class AccountInformationServiceImpl implements AccountInformationService {

    private static final Logger log = Logger.getLogger(AccountInformationServiceImpl.class);

    @Autowired
    AccountInformationRepository accountInformationRepository;

    public AccountBalanceResponse getAccountBalanceResponse(BigInteger accountNumber) throws ApiException {
        try {
            AccountBalanceResponse response = new AccountBalanceResponse();
            Optional<Account> account = accountInformationRepository.findByAccountNumber(accountNumber);
            if (account.isPresent()) {
                BeanUtils.copyProperties(response, account.get());
                response.setMessage(APIConstants.SUCCESS);
            } else {
                throw new ApiException("Account number is not in system. Please contact support.");
            }
            return response;
        } catch (Exception e) {
            log.info("Exception occurred while getting account balance for : " + accountNumber, e);
            throw new ApiException(e.getMessage());
        }
    }


}
