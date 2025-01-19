package com.EmployeSystem.Management.RepositoryTests;

import com.EmployeSystem.Management.model.Employee;
import com.EmployeSystem.Management.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryUnitTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Test 1: Save Employee Test")
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest() {
        // Action
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFullName("John Doe");
        employee.setJobTitle("Developer");
        employee.setDepartment("IT");
        employee.setHireDate(new Date());
        employee.setEmploymentStatus("Full-Time");
        employee.setContactInfo("johndoe@example.com");
        employee.setAddress("123 Main St");

        Employee savedEmployee = employeeRepository.save(employee);

        // Verify
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 2: Get Employee By ID Test")
    @Order(2)
    public void getEmployeeByIdTest() {
        // Action
        Employee employee = employeeRepository.findById(1L).orElseThrow(() -> new RuntimeException("Employee not found"));

        // Verify
        Assertions.assertThat(employee.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test 3: Get All Employees Test")
    @Order(3)
    public void getListOfEmployeesTest() {
        // Action
        List<Employee> employees = employeeRepository.findAll();

        // Verify
        Assertions.assertThat(employees.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 4: Update Employee Test")
    @Order(4)
    @Rollback(value = false)
    public void updateEmployeeTest() {
        // Action
        Employee employee = employeeRepository.findById(1L).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setContactInfo("newemail@example.com");
        Employee updatedEmployee = employeeRepository.save(employee);

        // Verify
        Assertions.assertThat(updatedEmployee.getContactInfo()).isEqualTo("newemail@example.com");
    }

    @Test
    @DisplayName("Test 5: Delete Employee Test")
    @Order(5)
    @Rollback(value = false)
    public void deleteEmployeeTest() {
        // Action
        employeeRepository.deleteById(1L);
        Optional<Employee> employeeOptional = employeeRepository.findById(1L);

        // Verify
        Assertions.assertThat(employeeOptional).isEmpty();
    }
}

