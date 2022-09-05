package com.imalipay.LoanManager.controllers;

import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.exceptions.InEligibilityException;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import com.imalipay.LoanManager.services.LoanService;
import com.imalipay.LoanManager.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/loan-manager")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/payment/pay/{email}")

    public ResponseEntity<?> makePayment(@PathVariable String email, @RequestParam  double paymentAmount) {
        try {
            return new ResponseEntity<>(paymentService.makePayment(email,paymentAmount), HttpStatus.CREATED);
        } catch (UserNotFoundException | InEligibilityException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/payment/{email}")

    public ResponseEntity<?> searchForPayment(@PathVariable String email) {
        try {
            return new ResponseEntity<>(paymentService.searchPayment(email), HttpStatus.FOUND);
        } catch (UserNotFoundException | InEligibilityException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
