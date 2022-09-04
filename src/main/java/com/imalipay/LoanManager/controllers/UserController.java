package com.imalipay.LoanManager.controllers;

import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.datas.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/students")
    public String listAll(Model model) {
        List<User> listStudents = userRepository.findAll();
        model.addAttribute("listUser", listStudents);

        return "user";
    }
}
