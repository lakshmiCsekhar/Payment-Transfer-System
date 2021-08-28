package com.org.ibpts.controller;

import com.org.ibpts.request.PaymentTransferRequest;
import com.org.ibpts.response.ConfirmationResponse;
import com.org.ibpts.response.PaymentTransferResponse;
import com.org.ibpts.service.PaymentTransferService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("/v1/payment")
public class PaymentTransferServiceAPI {

    @Autowired
    PaymentTransferService paymentTransferService;

    @PostMapping(value = "/transfer", produces = "application/json")
    @Operation(summary = "Starts the payment process and returns a url for confirmation.")
    public ResponseEntity createPaymentTransfer(@Valid @RequestBody PaymentTransferRequest request) {
        try {
            PaymentTransferResponse response = paymentTransferService.createPaymentTransfer(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Hidden
    @GetMapping(value = "/confirm")
    public ResponseEntity confirmTransfer(@RequestParam String debtorId, @RequestParam(required = false) String creditorId, @RequestParam(required = false) BigInteger creditorAccountNumber,
                                          @RequestParam String reference, @RequestParam BigDecimal amount, @RequestParam String type) {
        ConfirmationResponse response = paymentTransferService.confirmTransfer(debtorId, creditorId, creditorAccountNumber,
                reference, amount, type);
        return ResponseEntity.ok(response);
    }


}
