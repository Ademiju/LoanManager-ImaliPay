package com.imalipay.LoanManager.dtos.requests;

import com.imalipay.LoanManager.datas.models.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String contactAddress;
    private int income;
    private String dateOfBirth;
    private String gender;


}
