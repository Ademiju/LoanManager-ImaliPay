package com.imalipay.LoanManager.datas.models;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString

@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name",nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name",nullable = false, length = 50)
    private String lastName;
    @NotEmpty(message = "Email field cannot be empty")
    private String email;
    private String phoneNumber;
    private String contactAddress;
    private BigDecimal monthlyIncome;
    @OneToOne(orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;
    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}