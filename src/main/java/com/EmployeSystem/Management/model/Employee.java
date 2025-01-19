package com.EmployeSystem.Management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String department;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Column(nullable = false)
    private String employmentStatus;
    
    //For email format and i added the @valid annotation in requestmapping in the controller
    @Email
    @NotBlank
    @Column(nullable = false)
    private String contactInfo;

    private String address;
    
    
}

