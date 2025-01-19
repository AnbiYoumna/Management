package com.EmployeSystem.Management.service;

import com.EmployeSystem.Management.model.AuditTrail;
import com.EmployeSystem.Management.model.Employee;
import com.EmployeSystem.Management.model.User;
import com.EmployeSystem.Management.repository.AuditTrailRepository;
import com.EmployeSystem.Management.repository.EmployeeRepository; // Add EmployeeRepository
import com.EmployeSystem.Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditTrailService {

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;  // Inject EmployeeRepository

    public void logAuditTrail(Employee employee, String changeDetails) {
        // Check if the employee exists in the database
        Employee existingEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        if (existingEmployee == null) {
            // Handle the case where the employee doesn't exist
            throw new IllegalArgumentException("Employee not found with ID: " + employee.getId());
        }

        // Capture the current authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Fetch the full User object from the database using the username
        User currentUser = userRepository.findByUsername(username);

        // Create and save the audit log
        AuditTrail log = new AuditTrail();
        log.setEmployee(existingEmployee);  // Use the existing Employee object from the database
        log.setUser(currentUser);
        log.setChangeDate(new Date());
        log.setChangeDetails(changeDetails);
        auditTrailRepository.save(log);
    }
}
