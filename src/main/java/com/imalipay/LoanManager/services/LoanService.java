package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Loan;
import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.dtos.responses.LoanResponse;

public interface LoanService {
    LoanResponse createLoan(LoanRequest loanRequest);
    Loan searchLoanByUserEmail(String email);
}
