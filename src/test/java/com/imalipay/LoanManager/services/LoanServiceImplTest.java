package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Loan;
import com.imalipay.LoanManager.dtos.requests.LoanRequest;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.LoanResponse;
import com.imalipay.LoanManager.exceptions.InEligibilityException;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoanServiceImplTest {
    @Autowired
    LoanService loanService;
    @Autowired
    UserService userService;
    UserRequest userRequest;
    UserRequest userRequest_18;
    LoanRequest loanRequest;
    LoanRequest loanRequestAboveIncome;
    LoanRequest loanRequestOAboveDuration;

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .firstName("Test")
                .lastName("User")
                .phoneNumber("124546")
                .email("test@mail.com")
                .dateOfBirth("1994-10-3")
                .gender("FEMALE").contactAddress("2,testaddress,state,country")
                .income(1000000).build();

        userRequest_18 = UserRequest.builder()
                .firstName("Test")
                .lastName("User")
                .phoneNumber("124546")
                .email("test@mail.com")
                .dateOfBirth("2005-10-3")
                .gender("FEMALE").contactAddress("2,testaddress,state,country")
                .income(1000000).build();

         loanRequest = LoanRequest.builder().email("test@mail.com").amount(49000).durationInMonth(5).build();
         loanRequestAboveIncome = LoanRequest.builder().email("test@mail.com").amount(4900000).durationInMonth(5).build();
         loanRequestOAboveDuration = LoanRequest.builder().email("test@mail.com").amount(490000).durationInMonth(25).build();
    }
    @Test
    void userCanCreateLoan(){
        userService.createUser(userRequest);

        LoanResponse loanResponse = loanService.createLoan(loanRequest);
        assertEquals("Loan Approved",loanResponse.getMessage());

    }
    @Test
    void userBelowTheAgeOf18CreateLoan_ThrowsAnExceptionTest(){
        userService.createUser(userRequest_18);
        assertThatThrownBy(()->loanService.createLoan(loanRequest)).isInstanceOf(InEligibilityException.class).hasMessage("You are Ineligible for a loan because you are below 18");
    }
@Test
    void userWithExistingLoanCreateLoan_ThrowsAnExceptionTest(){
        userService.createUser(userRequest);
        loanService.createLoan(loanRequest);
        assertThatThrownBy(()->loanService.createLoan(loanRequest)).isInstanceOf(InEligibilityException.class).hasMessage("You have a pending loan");
    }

    @Test
    void userWithLoanRequestAboveIncomeLimit_ThrowsAnExceptionTest(){
        userService.createUser(userRequest);
        assertThatThrownBy(()->loanService.createLoan(loanRequestAboveIncome)).isInstanceOf(InEligibilityException.class).hasMessage("Loan Denied!!!...Loan request amount or duration is above eligible Loan Offer value...Maximum loan Offer is 30% of your yearly income and maximum loan duration is 24months");
    }
    @Test
    void userWithLoanRequestAboveDurationLimit_ThrowsAnExceptionTest(){
        userService.createUser(userRequest);
        assertThatThrownBy(()->loanService.createLoan(loanRequestOAboveDuration)).isInstanceOf(InEligibilityException.class).hasMessage("Loan Denied!!!...Loan request amount or duration is above eligible Loan Offer value...Maximum loan Offer is 30% of your yearly income and maximum loan duration is 24months");
    }

    @Test
    void loanDetailsAreCorrectTest(){
        userService.createUser(userRequest);
        LoanResponse loanResponse = loanService.createLoan(loanRequest);
        assertEquals(LocalDate.now(),loanResponse.getDate());
        assertEquals(LocalDate.now().plusMonths(loanRequest.getDurationInMonth()),loanResponse.getDueDate());
        assertEquals(0.08,loanResponse.getINTEREST_RATE());
        assertEquals(BigDecimal.valueOf(68600.0),loanResponse.getRepayment() );

    }

    @Test
    void searchLoanByEmailTest(){
        userService.createUser(userRequest);
        LoanResponse loanResponse = loanService.createLoan(loanRequest);
        Loan loanFromDB = loanService.searchLoanByUserEmail("test@mail.com");
        assertThat(loanFromDB.getDueDate(), is(loanResponse.getDueDate()));
        assertThat(loanFromDB.getDurationInMonth(), is(loanResponse.getDurationInMonth()));
        assertThat(loanFromDB.getLoanAmount().doubleValue(), is(loanResponse.getAmount().doubleValue()));
        assertThat(loanFromDB.getRepayment().doubleValue(), is(loanResponse.getRepayment().doubleValue()));
    }
    @Test
    void searchLoanWithNonExistingEmail_ThrowsExceptionTest(){
        userService.createUser(userRequest);
        loanService.createLoan(loanRequest);
        assertThatThrownBy(()->loanService.searchLoanByUserEmail("tester@email.com")).isInstanceOf(LoanManagerException.class).hasMessage("User does not exist");

    }


    @AfterEach
    void tearDown() {
        userService.deleteAll();

    }

}