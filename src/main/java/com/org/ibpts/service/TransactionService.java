package com.org.ibpts.service;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.response.Transaction;
import com.org.ibpts.response.TransactionsResponse;

import java.math.BigInteger;

public interface TransactionService {

    TransactionsResponse getTransactions(BigInteger accountNumber) throws ApiException;

    Transaction getTransaction(String reference) throws ApiException;

}
