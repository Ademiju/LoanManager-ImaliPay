package com.imalipay.LoanManager.dtos.responses;

import lombok.Data;

@Data
public class UserResponse {
    private String fullName;
    private String email;
    private int age;
    private String phoneNumber;
    private String Message;
}
