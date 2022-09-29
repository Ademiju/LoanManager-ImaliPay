package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Loan;
import com.imalipay.LoanManager.datas.models.Payment;
import com.imalipay.LoanManager.datas.models.Status;
import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.datas.repositories.LoanRepository;
import com.imalipay.LoanManager.datas.repositories.PaymentRepository;
import com.imalipay.LoanManager.datas.repositories.UserRepository;
import com.imalipay.LoanManager.dtos.responses.PaymentDetail;
import com.imalipay.LoanManager.exceptions.InEligibilityException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Override
    public PaymentDetail makePayment(String email, double paymentAmount) {
        User user =  userRepository.findByEmail(email.toLowerCase()).orElseThrow(()->new UserNotFoundException("User does not exist"));
        if (user.getLoan() == null) throw new InEligibilityException("No Loan Found");
        Loan loan= loanRepository.findById(user.getLoan().getId()).orElseThrow(()-> new InEligibilityException("No pending Loan"));
        PaymentDetail paymentDetail = new PaymentDetail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Payment payment = new Payment();
        if(paymentAmount > 0 && paymentAmount <= loan.getRepayment().doubleValue()) {
            payment.setDate(LocalDate.now());
            payment.setAmountToPay(BigDecimal.valueOf(paymentAmount));
            payment.setStatus(Status.SUCCESSFUL);
            payment.setMessage(paymentAmount+" Successfully paid");
            loan.setRepayment(loan.getRepayment().subtract(payment.getAmountToPay()));
            paymentRepository.save(payment);
            loan.getPayments().add(payment);
            Loan savedLoan = loanRepository.save(loan);
            user.setLoan(savedLoan);
            userRepository.save(user);

            paymentDetail.setPaymentStatus(Status.SUCCESSFUL);
            paymentDetail.setPaymentTime(LocalDateTime.now().format(formatter));
            paymentDetail.setAmountPaid(BigDecimal.valueOf(paymentAmount));
            paymentDetail.setDebtBalance(savedLoan.getRepayment());
            paymentDetail.setDueDate(savedLoan.getDueDate());
            paymentDetail.setLoanStatus(Status.OWING);
            paymentDetail.setMessage(payment.getMessage());

            if(savedLoan.getRepayment().intValue() == 0 ) {
                paymentDetail.setLoanStatus(Status.LOAN_COMPLETELY_PAID);
                user.setLoan(null);
                userRepository.save(user);
            }
            return paymentDetail;
        }
        paymentDetail.setPaymentTime(LocalDateTime.now().format(formatter));
        paymentDetail.setDebtBalance(loan.getRepayment());
        paymentDetail.setDueDate(loan.getDueDate());
        paymentDetail.setLoanStatus(Status.OWING);
        paymentDetail.setPaymentStatus(Status.DECLINED);
        paymentDetail.setMessage(paymentAmount+" is negative or above required repayment amount");
        payment.setStatus(Status.DECLINED);
        payment.setDate(LocalDate.now());
        payment.setMessage(paymentDetail.getMessage());
        paymentRepository.save(payment);
        loan.getPayments().add(payment);
        Loan savedLoan = loanRepository.save(loan);
        user.setLoan(savedLoan);
        userRepository.save(user);

       return paymentDetail;
    }

    @Override
    public List<Payment> searchPayment(String email) {
        User user= userRepository.findByEmail(email.toLowerCase()).orElseThrow(()-> new UserNotFoundException("User does not exist"));
        if (user.getLoan() == null) throw new InEligibilityException("No Loan Found");
        Loan loan =  loanRepository.findById(user.getLoan().getId()).get();
        return loan.getPayments();
    }
}
