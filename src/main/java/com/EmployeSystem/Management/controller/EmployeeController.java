package com.EmployeSystem.Management.controller;

import com.EmployeSystem.Management.model.Employee;
import com.EmployeSystem.Management.service.AuditTrailService;
import com.EmployeSystem.Management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private AuditTrailService auditTrailService; // Inject AuditTrailService for logging changes

    // Access allowed for HR, MANAGER, and ADMIN roles
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Access allowed for HR, MANAGER, and ADMIN roles
    @PreAuthorize("hasAnyAuthority('ROLE_HR', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return ResponseEntity.ok(employee);
    }

    // Only HR and admin role can create employees
    @PreAuthorize("hasAuthority('ROLE_HR', 'ROLE_ADMIN')")
    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = repository.save(employee);

        // Log the creation of the employee
        auditTrailService.logAuditTrail(savedEmployee, "Created a new employee record");

        return savedEmployee;
    }

    // Only HR and admin role can update employees
    @PreAuthorize("hasAuthority('ROLE_HR', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update employee details
        employee.setFullName(employeeDetails.getFullName());
        employee.setJobTitle(employeeDetails.getJobTitle());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setEmploymentStatus(employeeDetails.getEmploymentStatus());
        employee.setContactInfo(employeeDetails.getContactInfo());
        employee.setAddress(employeeDetails.getAddress());

        Employee updatedEmployee = repository.save(employee);

        // Log the update
        String changeDetails = "Updated employee details for: " + updatedEmployee.getFullName();
        auditTrailService.logAuditTrail(updatedEmployee, changeDetails);

        return ResponseEntity.ok(updatedEmployee);
    }

    // Only HR and Admin role can delete employees
    //@PreAuthorize("hasAuthority('ROLE_HR', 'ROLE_ADMIN')")
    //@DeleteMapping("/{id}")
    //public ResponseEntity<Void> deleteEmployee1(@PathVariable Long id) {
        //Employee employee = repository.findById(id)
                //.orElseThrow(() -> new RuntimeException("Employee not found"));

        // Log the deletion
        //String changeDetails = "Deleted employee record: " + employee.getFullName();
        //auditTrailService.logAuditTrail(employee, changeDetails);

        //repository.delete(employee);
        //return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
    //}
    
    @PreAuthorize("hasAuthority('ROLE_HR', 'ROLE_ADMIN')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        // Fetch the employee by their employeeId
        Employee employee = repository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }

        // Log the deletion
        String changeDetails = "Deleted employee record: " + employee.getFullName();
        auditTrailService.logAuditTrail(employee, changeDetails);

        // Perform the deletion
        repository.delete(employee);

        return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
    }
}
