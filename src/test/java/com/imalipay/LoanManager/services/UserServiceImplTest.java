package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Gender;
import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.UserResponse;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    UserRequest userRequest;
    UserRequest second_userRequest;

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
        second_userRequest = UserRequest.builder()
                .firstName("Test1")
                .lastName("User")
                .phoneNumber("124546")
                .email("test1@mail.com")
                .dateOfBirth("1994-10-3")
                .gender("FEMALE").contactAddress("2,testaddress,state,country")
                .income(1000000).build();

    }

    @Test
    void userCanBeCreatedTest(){
        UserResponse userResponse = userService.createUser(userRequest);
        assertEquals(userResponse.getAge(),27);
        assertEquals(userResponse.getMessage(),"Account Successfully Created");
    }
    @Test
    void createNewUser_WithExistingEmail_ThrowsExceptionTest() {
        userService.createUser(userRequest);
        assertThatThrownBy(() -> userService.createUser(userRequest)).isInstanceOf(LoanManagerException.class).hasMessage("Email Already Exist");

    }

    @Test
    void createNewUser_WithExistingPhoneNumber_ThrowsExceptionTest() {
        userService.createUser(userRequest);
        assertThatThrownBy(() -> userService.createUser(second_userRequest)).isInstanceOf(LoanManagerException.class).hasMessage("Phone number Already Exist");
    }

    @Test
    void findUserByEmailTest(){
        UserResponse response = userService.createUser(userRequest);
        User userFromDB = userService.searchUserByEmail("test@mail.com");
        assertThat(userFromDB.getPhoneNumber(), is(response.getPhoneNumber()));
    }
    @Test
    public void searchUserWithNonExistingEmailThrowsExceptionTest(){
        userService.createUser(userRequest);
        assertThatThrownBy(()->userService.searchUserByEmail("tester@email.com")).isInstanceOf(UserNotFoundException.class).hasMessage("User does not exist");

    }
    @Test
    void findUserByIdTest(){
        UserResponse response = userService.createUser(userRequest);
        User userFromDB = userService.searchUserById(21L);
        assertThat(userFromDB.getPhoneNumber(), is(response.getPhoneNumber()));
    }
    @Test
    public void searchUserWithNonExistingIdThrowsExceptionTest(){
        userService.createUser(userRequest);
        assertThatThrownBy(()->userService.searchUserById(6L)).isInstanceOf(UserNotFoundException.class).hasMessage("User does not exist");

    }
    @AfterEach
    void tearDown() {
        userService.deleteAll();

    }

}