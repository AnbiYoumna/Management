package com.EmployeSystem.Management.ControllerTest;

import com.EmployeSystem.Management.controller.EmployeeController;
import com.EmployeSystem.Management.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeController employeeController;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFullName("John Cena");
        employee.setJobTitle("Developer");
        employee.setDepartment("IT");
        employee.setEmploymentStatus("Active");
        employee.setContactInfo("123-456-7890");
        employee.setAddress("123 Street, City");
    }

    @Test
    @Order(1)
    public void saveEmployeeTest() throws Exception {
        given(employeeController.createEmployee(any(Employee.class))).willReturn(employee);

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName", is(employee.getFullName())))
                .andExpect(jsonPath("$.jobTitle", is(employee.getJobTitle())))
                .andExpect(jsonPath("$.department", is(employee.getDepartment())));
    }

    @Test
    @Order(2)
    public void getAllEmployeesTest() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employees.add(new Employee());

        given(employeeController.getAllEmployees()).willReturn(employees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())));
    }

    @Test
    @Order(3)
    public void getEmployeeByIdTest() throws Exception {
        given(employeeController.getEmployeeById(employee.getId()));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(employee.getFullName())));
    }

    @Test
    @Order(4)
    public void updateEmployeeTest() throws Exception {
        given(employeeController.getEmployeeById(employee.getId()));

        employee.setFullName("Max Smith");
        given(employeeController.updateEmployee(any(Long.class), any(Employee.class)));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(employee.getFullName())));
    }

    @Test
    @Order(5)
    public void deleteEmployeeTest() throws Exception {
        willDoNothing().given(employeeController).deleteEmployee(null);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }
}
