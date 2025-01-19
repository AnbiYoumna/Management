package com.EmployeSystem.Management.ControllerTest;

import com.EmployeSystem.Management.controller.EmployeeController;
import com.EmployeSystem.Management.controller.UserController;
import com.EmployeSystem.Management.model.Employee;
import com.EmployeSystem.Management.model.User;
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

@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("Youmna");
        user.setPassword("12345678");
        user.setRole("HR");
        user.setDepartment("Humnan Resource");
    }

    @Test
    @Order(1)
    public void saveUserTest() throws Exception {
        given(userController.createUser(any(User.class)));

        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    @Order(2)
    public void getAllUsersTest() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(new User());

        given(userController.getAllUsers()).willReturn(users);

        ResultActions response = mockMvc.perform(get("/api/users"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(users.size())));
    }

    @Test
    @Order(3)
    public void getUserByIdTest() throws Exception {
        given(userController.getUserById(user.getId()));

        ResultActions response = mockMvc.perform(get("/api/users/{id}", user.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    @Order(4)
    public void updateUserTest() throws Exception {
        given(userController.getUserById(user.getId()));

        user.setUsername("Youmna");
        given(userController.updateUser(any(Long.class), any(User.class)));

        ResultActions response = mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(user.getUsername())));
    }

    @Test
    @Order(5)
    public void deleteUserTest() throws Exception {
        willDoNothing().given(userController).deleteUser(user.getId());

        ResultActions response = mockMvc.perform(delete("/api/users/{id}", user.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }
}




	
	