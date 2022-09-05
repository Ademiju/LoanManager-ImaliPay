package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    User searchUserByEmail(String email);
    User searchUserById(Long id);

    void deleteAll();
}
