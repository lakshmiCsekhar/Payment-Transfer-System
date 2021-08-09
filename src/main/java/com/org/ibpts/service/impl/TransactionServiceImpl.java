package com.org.ibpts.service.impl;

import com.org.ibpts.errorhandling.ApiException;
import com.org.ibpts.model.Account;
import com.org.ibpts.model.Transactions;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.repository.TransactionsRepository;
import com.org.ibpts.response.Transaction;
import com.org.ibpts.response.TransactionsResponse;
import com.org.ibpts.service.TransactionService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    AccountInformationRepository accountInformationRepository;

    @Autowired
    TransactionsRepository transactionsRepository;


    @Override
    public TransactionsResponse getTransactions(BigInteger accountNumber) throws ApiException {

        try {
            Optional<Account> debtor = accountInformationRepository.findByAccountNumber(accountNumber);
            if (debtor.isPresent()) {
                List<Transactions> transactionsFromDb = transactionsRepository.findByDebtorId(debtor.get().getId(), PageRequest.of(0,20));
                List<Transaction> transactionList = new ArrayList<>();
                for (Transactions tr : transactionsFromDb) {
                    Transaction ts = new Transaction();
                    BeanUtils.copyProperties(ts, tr);
                    transactionList.add(ts);
                }
                return new TransactionsResponse(transactionList, accountNumber, transactionsFromDb.size(), "SUCCESS");
            } else {
                throw new ApiException("Debitor is not present is system.");
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public Transaction getTransaction(String reference) throws ApiException {
        Transactions transaction = transactionsRepository.findByReference(reference);
        Transaction transactionDao = new Transaction();
        try {
            BeanUtils.copyProperties(transactionDao, transaction);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
        return transactionDao;
    }
}
