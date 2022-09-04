package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.UserDetail;
import com.imalipay.LoanManager.dtos.responses.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserDetail searchUserByEmail(String email);
    UserDetail searchUserById(Integer id);
}
