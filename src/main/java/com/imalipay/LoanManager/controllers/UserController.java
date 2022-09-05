package com.imalipay.LoanManager.controllers;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import com.imalipay.LoanManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/loan-manager")
public class UserController {

        @Autowired
        private UserService userService;
        @PostMapping("/users/register")

        public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
            try {
                return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
            } catch (LoanManagerException error) {
                return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

    @GetMapping("/admin/search/{emailAddress}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String emailAddress){
        try{
            return new ResponseEntity<>(userService.searchUserByEmail(emailAddress),HttpStatus.FOUND);
        }catch (UserNotFoundException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/admin/search/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(userService.searchUserById(id),HttpStatus.FOUND);
        }catch (UserNotFoundException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    }

