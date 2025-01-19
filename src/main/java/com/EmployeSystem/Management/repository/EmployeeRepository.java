package com.EmployeSystem.Management.repository;

import com.EmployeSystem.Management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmployeeId(String employeeId);  // Find employee by employeeId
}
