package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Loan;
import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.datas.repositories.LoanRepository;
import com.imalipay.LoanManager.datas.repositories.UserRepository;
import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.dtos.responses.LoanResponse;
import com.imalipay.LoanManager.exceptions.InEligibilityException;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService{
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {
        User user = userService.searchUserByEmail(loanRequest.getEmail());
        LoanResponse loanResponse = new LoanResponse();
        if(user.getAge() >=18){
            if(user.getLoan() == null){
//  Maximum loan offer is 30% of a user's income
                BigDecimal monthlyIncome = user.getMonthlyIncome();
                BigDecimal yearlyIncome = monthlyIncome.multiply(BigDecimal.valueOf(12));
                BigDecimal maxLoanOffer = yearlyIncome.multiply(BigDecimal.valueOf(0.3));
                if (maxLoanOffer.compareTo(BigDecimal.valueOf(loanRequest.getAmount()))>= 0 && loanRequest.getDurationInMonth() <= 24 ){
                   Loan loan = new Loan();
                   loan.setUser(user);
                   BigDecimal amountToBorrow = BigDecimal.valueOf(loanRequest.getAmount());
                   loan.setAmount(amountToBorrow);
                   LocalDate dateOfLoanRequest = LocalDate.now();
                   loan.setDate(dateOfLoanRequest);
                   loan.setDueDate(dateOfLoanRequest.plusMonths(loanRequest.getDurationInMonth()));
                   int loanDuration =loanRequest.getDurationInMonth();
                   loan.setDurationInMonth(loanDuration);
// Calculate Amount to be repaid by the borrower
                   double interest = 1 + (loan.getINTEREST_RATE()* loanDuration);
                   BigDecimal repaymentAmount = amountToBorrow.multiply(BigDecimal.valueOf(interest));
                   loan.setRepayment(repaymentAmount);
                   Loan savedLoan = loanRepository.save(loan);
                   user.setLoan(savedLoan);
                   userRepository.save(user);
                   modelMapper.map(savedLoan,loanResponse);
                   loanResponse.setMessage("Loan Approved");
                   return loanResponse;
               }
                   throw new InEligibilityException("Loan Denied!!!...Loan request amount or duration is above eligible Loan Offer value...Maximum loan Offer is 30% of your yearly income and maximum loan duration is 24months");
            }else {
                throw new InEligibilityException("You have a pending loan");
            }

        }else{
            throw new InEligibilityException("You are Ineligible for a loan because you are below 18");
        }
    }

    @Override
    public Loan searchLoanByUserEmail(String email) {
        User user = userService.searchUserByEmail(email);
        return loanRepository.findById(user.getLoan().getId()).orElseThrow(()-> new LoanManagerException("No Loan Found"));

    }
}
