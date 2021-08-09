package com.org.ibpts.service.impl;

import com.org.ibpts.constants.APIConstants;
import com.org.ibpts.model.Account;
import com.org.ibpts.model.Transactions;
import com.org.ibpts.repository.AccountInformationRepository;
import com.org.ibpts.repository.TransactionsRepository;
import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.ConfirmationResponse;
import com.org.ibpts.response.PaymentTransferResponse;
import com.org.ibpts.service.PaymentTransferService;
import com.org.ibpts.utils.AccountNumberValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;


@Service
public class PaymentTransferServiceImpl implements PaymentTransferService {
    public static final String PAYMENT_CONFIRMATION_BASE_URL = "http://localhost:8080/v1/payment/confirm";
    private static final Logger log = Logger.getLogger(PaymentTransferServiceImpl.class);
    @Autowired
    AccountInformationRepository accountInformationRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    AccountNumberValidator accountNumberValidator;


    @Override
    public PaymentTransferResponse createPaymentTransfer(PaymentTransferRequest request) {

        PaymentTransferResponse response = new PaymentTransferResponse();
        log.debug("PaymentTransfer request : " + request.toString());
        boolean generateSigningUrl = true;
        StringBuilder signingUrl = new StringBuilder(PAYMENT_CONFIRMATION_BASE_URL);
        StringBuilder message = new StringBuilder("Please confirm the details below to start payment transfer. Click the signing url to proceed.");
        BigDecimal transferAmount = request.getAmount();

        transferAmount = setUpdatedTransferAmountForInternationalTransaction(request, response, message, transferAmount);
        Optional<Account> debtorAccount = accountInformationRepository.findByAccountNumber(request.getDebtorAccountNumber());
        if (debtorAccount.isPresent()) {
            if (debtorAccount.get().getIsActive()) {
                generateSigningUrl = setUpDebtorAndCreditor(request, response, generateSigningUrl, signingUrl, transferAmount, debtorAccount.get());

            } else {
                generateSigningUrl = false;
                response.setStatus(APIConstants.ERROR);
                response.setMessage("Debtor  Account is not active. Please proceed after registration.");
            }
        } else {
            generateSigningUrl = false;
            response.setStatus(APIConstants.ERROR);
            response.setMessage("Debtor is not registered in  our system . Please proceed after registration.");
        }

        if (generateSigningUrl) {
            createPendingPayment(request, response, signingUrl, debtorAccount.get());
            prepareResponse(request, response, signingUrl, message, transferAmount);
        }
        return response;
    }

    private void prepareResponse(PaymentTransferRequest request, PaymentTransferResponse response, StringBuilder signingUrl, StringBuilder message, BigDecimal transferAmount) {
        signingUrl.append("&amount=").append(transferAmount);
        signingUrl.append("&type=").append(request.getType());
        response.setMessage(message.toString());
        response.setSigningUrl(signingUrl.toString());
        response.setStatus(APIConstants.AWAITING_CONFIRMATION);
    }

    private boolean setUpDebtorAndCreditor(PaymentTransferRequest request, PaymentTransferResponse response, boolean generateSigningUrl, StringBuilder signingUrl, BigDecimal transferAmount, Account debtorAccount) {
        log.info("Debtor is in system.");
        signingUrl.append("/?debtorId=").append(debtorAccount.getId()); //debtor accountid

        BigDecimal debtorAccountBalance = debtorAccount.getBalance();
        if (debtorAccountBalance.compareTo(transferAmount) > 0) {
            generateSigningUrl = appendCreditor(request.getCreditorAccountNumber(), signingUrl, response, generateSigningUrl);
        } else {
            generateSigningUrl = false;
            response.setStatus(APIConstants.ERROR);
            response.setMessage("Insufficient funds. Account balance is : " + debtorAccount.getBalance() + debtorAccount.getCurrency());
        }
        return generateSigningUrl;
    }

    private BigDecimal setUpdatedTransferAmountForInternationalTransaction(PaymentTransferRequest request, PaymentTransferResponse response, StringBuilder message, BigDecimal transferAmount) {
        if (request.isInternationalTransfer()) {
            response.setInternationalTransactionFee("10 NOK");
            message.append("International transfer fee of 100 NOK will be applied to this transaction.");
            transferAmount = transferAmount.add(BigDecimal.valueOf(10));
        }
        return transferAmount;
    }

    private boolean appendCreditor(BigInteger creditorAccountNumber, StringBuilder signingUrl, PaymentTransferResponse response, boolean generateSigningUrl) {
        if (accountNumberValidator.validate(creditorAccountNumber)) {
            Optional<Account> creditorAccount = accountInformationRepository.findByAccountNumber(creditorAccountNumber);
            if (creditorAccount.isPresent()) {
                if (creditorAccount.get().getIsActive()) {
                    signingUrl.append("&creditorId=").append(creditorAccount.get().getId());//creditor account id
                } else {
                    generateSigningUrl = false;
                    response.setStatus(APIConstants.ERROR);
                    response.setMessage("Creditor account number is not active.");
                }
            } else {
                signingUrl.append("&creditorAccountNumber=").append(creditorAccountNumber); // creditor account number
            }
        } else {
            generateSigningUrl = false;
            response.setStatus(APIConstants.ERROR);
            response.setMessage("Creditor account number is not valid.");
        }
        return generateSigningUrl;
    }

    private void createPendingPayment(PaymentTransferRequest request, PaymentTransferResponse response, StringBuilder signingUrl, Account debtor) {
        Transactions transaction = new Transactions(request.getCreditorAccountNumber(), debtor.getId(), request.getAmount(),
                request.getTransferDate(), APIConstants.AWAITING_CONFIRMATION, request.getType().toString(), request.isInternationalTransfer());
        transactionsRepository.save(transaction);

        String reference = transaction.getReference();
        signingUrl.append("&reference=").append(reference);
        response.setReference(reference);
    }


    @Override
    @Transactional
    public ConfirmationResponse confirmTransfer(String debtorId, String creditorId, BigInteger creditorAccountNumber,
                                                String reference, BigDecimal amount, String type) {

        BigDecimal debtorAccountBalance = accountInformationRepository.getAccountBalance(debtorId);
        final BigDecimal toUpdate = debtorAccountBalance.subtract(amount);
        accountInformationRepository.updateBalanceForAccount(toUpdate, debtorId);


        Optional<Account> creditor = accountInformationRepository.findById(creditorId);
        if (creditor.isPresent()) {
            BigDecimal creditorAccountBalance = accountInformationRepository.getAccountBalance(creditorId);
            accountInformationRepository.updateBalanceForAccount(creditorAccountBalance.add(amount), creditorId);
        }
        Transactions tr = transactionsRepository.findByReference(reference);
        tr.setStatus(APIConstants.COMPLETE);
        transactionsRepository.save(tr);

        //psd2 logic goes here

        return new ConfirmationResponse(reference, "Transaction started. " +
                "Please use the schedule to track the progress of your payment.");

    }


}
