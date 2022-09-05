package com.imalipay.LoanManager.controllers;

import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.exceptions.InEligibilityException;
import com.imalipay.LoanManager.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/loan-manager")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @PostMapping("/loan/create")

    public ResponseEntity<?> createLoan(@RequestBody LoanRequest loanRequest) {
        try {
            return new ResponseEntity<>(loanService.createLoan(loanRequest), HttpStatus.CREATED);
        } catch (InEligibilityException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/loan/search/{emailAddress}")

    public ResponseEntity<?> searchLoan(@PathVariable String emailAddress){
        return new ResponseEntity<>(loanService.searchLoanByUserEmail(emailAddress),HttpStatus.OK);
    }

}
