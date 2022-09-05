package com.imalipay.LoanManager.services;

import com.imalipay.LoanManager.datas.models.Gender;
import com.imalipay.LoanManager.datas.models.User;
import com.imalipay.LoanManager.datas.repositories.UserRepository;
import com.imalipay.LoanManager.dtos.requests.UserRequest;
import com.imalipay.LoanManager.dtos.responses.UserResponse;
import com.imalipay.LoanManager.exceptions.LoanManagerException;
import com.imalipay.LoanManager.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail().toLowerCase());
        if(optionalUser.isPresent()) throw new LoanManagerException("Email Already Exist");
        Optional<User> optionalUser1 = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if(optionalUser1.isPresent()) throw new LoanManagerException("Phone number Already Exist");
        User user = new User();
        UserResponse userResponse = new UserResponse();
        LocalDate dateOfBirth = LocalDate.parse(request.getDateOfBirth());
        Gender gender = Gender.valueOf(request.getGender());
        int age = calculateAge(dateOfBirth,LocalDate.now());
        String fullName = request.getFirstName()+" "+request.getLastName();
        BigDecimal income = BigDecimal.valueOf(request.getIncome());
        modelMapper.map(request,user);
        user.setAge(age);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setMonthlyIncome(income);
        userRepository.save(user);
        modelMapper.map(user, userResponse);
        userResponse.setFullName(fullName);
        userResponse.setMessage("Account Successfully Created");


        return userResponse;
    }

    @Override
    public User searchUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new UserNotFoundException("User does not exist"));

    }

    @Override
    public User searchUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist"));

    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

}
