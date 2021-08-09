package com.org.ibpts.controller;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.AccountBalanceResponse;
import com.org.ibpts.service.AccountInformationService;
import com.org.ibpts.utils.AccountNumberValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;


@RestController
@RequestMapping("/v1/account/")
@Tag(name = "accounts", description = "Account Information API")
public class AccountInformationServiceAPI {

    @Autowired
    AccountInformationService accountInformationService;

    @Autowired
    AccountNumberValidator accountNumberValidator;


    @GetMapping(value = "/{accountNumber}/balance", produces = "application/json")
    @Operation(summary = "Find account balance & information by account number")
    public ResponseEntity getAccountBalance(@Parameter(description = "Account number length should be in the range of 6-20", required = true) @PathVariable(value = "accountNumber", required = true) BigInteger accountNumber) {
        if (accountNumberValidator.validate(accountNumber)) {
            try {
                AccountBalanceResponse response = accountInformationService.getAccountBalanceResponse(accountNumber);
                return ResponseEntity.ok(response);
            } catch (ApiException e) {
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid account number provided : " + accountNumber);
        }

    }


}
