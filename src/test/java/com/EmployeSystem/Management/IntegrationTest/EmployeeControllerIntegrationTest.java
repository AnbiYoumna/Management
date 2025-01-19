package com.EmployeSystem.Management.IntegrationTest;

import com.EmployeSystem.Management.model.Employee;
import com.EmployeSystem.Management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll(); // Clear database before each test
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFullName("John Doe");
        employee.setJobTitle("Manager");
        employee.setDepartment("HR");
        employee.setHireDate(new Date());
        employee.setEmploymentStatus("Full-Time");
        employee.setContactInfo("john.doe@example.com");
        employee.setAddress("123 Main St");
        employeeRepository.save(employee);
    }

    @Test
    void testGetAllEmployees() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/employees", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreateEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId("E124");
        newEmployee.setFullName("Jane Smith");
        newEmployee.setJobTitle("Developer");
        newEmployee.setDepartment("IT");
        newEmployee.setHireDate(new Date());
        newEmployee.setEmploymentStatus("Contract");
        newEmployee.setContactInfo("jane.smith@example.com");
        newEmployee.setAddress("456 Elm St");

        ResponseEntity<Employee> response = restTemplate.postForEntity("/api/employees", newEmployee, Employee.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jane Smith", response.getBody().getFullName());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = employeeRepository.findAll().get(0);

        ResponseEntity<Employee> response = restTemplate.getForEntity("/api/employees/" + employee.getId(), Employee.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getFullName());
    }
    
    //test for invalid email or not
    
    @Test
    void testCreateEmployeeInvalidEmail() {
        Employee invalidEmployee = new Employee();
        invalidEmployee.setFullName("Invalid User");
        invalidEmployee.setContactInfo("invalid-email"); // Invalid email format

        ResponseEntity<String> response = restTemplate.postForEntity("/api/employees", invalidEmployee, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}

