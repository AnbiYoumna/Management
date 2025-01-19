package com.EmployeSystem.Management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_trail_seq")
    @SequenceGenerator(name = "audit_trail_seq", sequenceName = "audit_trail_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true, foreignKey = @ForeignKey(name = "fk_employee"))
    private Employee employee;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;

    @Column(nullable = false)
    private String changeDetails;
}
