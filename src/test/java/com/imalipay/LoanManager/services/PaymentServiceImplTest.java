package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Gender;
import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.LoanResponse;
import com.imalipay.LoanManager.dtos.responses.PaymentDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserService userService;
    @Autowired
    LoanService loanService;

    UserRequest userRequest;
    LoanRequest loanRequest;


    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .firstName("Test")
                .lastName("User")
                .phoneNumber("124546")
                .email("test@mail.com")
                .dateOfBirth("1994-10-3")
                .gender("FEMALE").contactAddress("2,testaddress,state,country")
                .income(100000).build();

        loanRequest = LoanRequest.builder().email("test@mail.com").amount(49000).durationInMonth(5).build();
       }
    @Test
    void userCanMakePayment(){
        userService.createUser(userRequest);
        LoanResponse loanResponse = loanService.createLoan(loanRequest);
        PaymentDetail paymentDetail = paymentService.makePayment("test@mail.com",20000);
        assertEquals(paymentDetail.getDebtBalance(),loanResponse.getAmount());

    }

    @AfterEach
    void tearDown() {
        userService.deleteAll();

    }

}