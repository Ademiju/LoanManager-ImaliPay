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
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class PaymentServiceImpl implements PaymentService{

    ModelMapper modelMapper = new ModelMapper();
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
        Loan loan= loanRepository.findById(user.getLoan().getId()).orElseThrow(()-> new InEligibilityException("No pending Loan"));
        PaymentDetail paymentDetail = new PaymentDetail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(paymentAmount > 0 && paymentAmount <= loan.getRepayment().doubleValue()) {
            Payment payment = new Payment();
            payment.setDate(LocalDate.now());
            payment.setAmount(BigDecimal.valueOf(paymentAmount));
            loan.setAmount(loan.getAmount().subtract(payment.getAmount()));
            paymentRepository.save(payment);
            loan.getPayments().add(payment);
            Loan savedLoan = loanRepository.save(loan);
            user.setLoan(savedLoan);
            userRepository.save(user);

            paymentDetail.setPaymentStatus(Status.SUCCESSFUL);
            paymentDetail.setPaymentTime(LocalDateTime.now().format(formatter));
            paymentDetail.setDebtBalance(savedLoan.getAmount());
            paymentDetail.setDueDate(savedLoan.getDueDate());
            paymentDetail.setLoanStatus(Status.OWING);
            if(savedLoan.getRepayment().intValue() == 0 ) {
                paymentDetail.setLoanStatus(Status.LOAN_COMPLETELY_PAID);
                user.setLoan(null);
                userRepository.save(user);
            }
            return paymentDetail;
        }
        paymentDetail.setPaymentTime(LocalDateTime.now().format(formatter));
        paymentDetail.setDebtBalance(loan.getAmount());
        paymentDetail.setDueDate(loan.getDueDate());
        paymentDetail.setLoanStatus(Status.OWING);
        paymentDetail.setPaymentStatus(Status.DECLINED);
       return paymentDetail;
    }

    @Override
    public List<Payment> searchPayment(String email) {
        User user= userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User does not exist"));
        Loan loan =  loanRepository.findById(user.getLoan().getId()).orElseThrow(()-> new InEligibilityException("No pending Loan"));
        return loan.getPayments();
    }
}
