package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.UserDetail;
import com.imalipay.LoanManager.dtos.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public UserResponse createUser(UserRequest request) {
        return null;
    }

    @Override
    public UserDetail searchUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDetail searchUserById(Integer id) {
        return null;
    }


}
