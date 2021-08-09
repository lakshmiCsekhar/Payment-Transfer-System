package com.org.ibpts.controller;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.Transaction;
import com.org.ibpts.response.TransactionsResponse;
import com.org.ibpts.service.TransactionService;
import com.org.ibpts.utils.AccountNumberValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/v1/transactions/")
@Tag(name = "transactions", description = "Transactions API")
public class TransactionsAPI {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountNumberValidator accountNumberValidator;


    @GetMapping(value = "/{accountNumber}/mini", produces = "application/json")
    @Operation(summary = "Returns upto 20 transactions for account number.")
    public ResponseEntity getTransactions(@PathVariable("accountNumber") BigInteger accountNumber) {
        if (accountNumberValidator.validate(accountNumber)) {
            try {
                TransactionsResponse response = transactionService.getTransactions(accountNumber);
                return ResponseEntity.ok(response);
            } catch (ApiException e) {
                return ResponseEntity.internalServerError().body("Something went wrong :  " + e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid account number provided : " + accountNumber);
        }

    }

    @GetMapping(value = "/single/{reference}")
    public ResponseEntity getTransaction(@PathVariable(required = true) String reference) {
        try {
            Transaction transaction =  transactionService.getTransaction(reference);
            return ResponseEntity.ok(transaction);
        } catch (ApiException e) {
            return ResponseEntity.internalServerError().body("Something went wrong :  " + e.getMessage());
        }

    }
}
