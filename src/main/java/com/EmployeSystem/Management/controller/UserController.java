package com.EmployeSystem.Management.controller;

import com.EmployeSystem.Management.model.User;
import com.EmployeSystem.Management.repository.UserRepository;
import com.EmployeSystem.Management.security.JwtTokenUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Check if username already exists
        if (repository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists.");
        }

        // Validate password
        if (user.getPassword().length() < 6) {
            return ResponseEntity.status(400).body("Password must be at least 6 characters long.");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        User savedUser = repository.save(user);

        // Generate JWT token for the newly created user
        String token = jwtTokenUtil.generateToken(savedUser);

        // Return the generated token as a JSON response
        return ResponseEntity.status(201).body("{\"authToken\": \"" + token + "\"}");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginRequest) {
        User user = repository.findByUsername(loginRequest.getUsername());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("{ \"message\": \"Invalid username or password\" }");
        }

        // Generate JWT token upon successful login
        String token = jwtTokenUtil.generateToken(user);
        
        

        // Print the token to the console
        System.out.println("JWT Token sent to client: " + token);

        

        // Return the generated token as a response
        return ResponseEntity.ok("{ \"authToken\": \"" + token + "\" }");
    }



    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setRole(userDetails.getRole());
        user.setDepartment(userDetails.getDepartment());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return ResponseEntity.ok(repository.save(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
