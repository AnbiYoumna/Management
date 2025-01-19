package com.EmployeSystem.Management.repository;


import com.EmployeSystem.Management.model.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    List<AuditTrail> findByEmployeeId(Long employeeId);
}

