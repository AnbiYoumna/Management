package com.EmployeSystem.Management.UI;

import javax.swing.*;

import org.json.JSONObject;

import net.miginfocom.swing.MigLayout;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RegisterUI {
    private JFrame frame;

    public void showRegisterScreen() {
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new MigLayout("", "[][grow]", "[][][][]20[]"));
        
        JLabel lblUserId = new JLabel("User ID:");
        JTextField txtUserId = new JTextField(20);
        
        JLabel lblFullName = new JLabel("Full Name:");
        JTextField txtFullName = new JTextField(20);

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(20);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        JPasswordField txtConfirmPassword = new JPasswordField(20);

        JLabel lblRole = new JLabel("Role:");
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"HR", "Manager", "Admin"});

        JLabel lblDepartment = new JLabel("Department:");
        JTextField txtDepartment = new JTextField(20);
        
        JLabel lblContactInfo = new JLabel("Contact Info:");
        JTextField txtContactInfo = new JTextField(20);

        JButton btnRegister = new JButton("Register");
        JButton btnMain = new JButton("Back");

        panel.add(lblUserId);
        panel.add(txtUserId, "growx, wrap");

        panel.add(lblFullName);
        panel.add(txtFullName, "growx, wrap");

        panel.add(lblUsername);
        panel.add(txtUsername, "growx, wrap");

        panel.add(lblPassword);
        panel.add(txtPassword, "growx, wrap");

        panel.add(lblConfirmPassword);
        panel.add(txtConfirmPassword, "growx, wrap");

        panel.add(lblRole);
        panel.add(cmbRole, "growx, wrap");

        panel.add(lblDepartment);
        panel.add(txtDepartment, "growx, wrap");
        
        panel.add(lblContactInfo);
        panel.add(txtContactInfo, "growx, wrap");

        panel.add(btnRegister, "split 2, center");
        panel.add(btnMain, "center");

        btnRegister.addActionListener(e -> register(
                txtUserId.getText(),
                txtFullName.getText(),
                txtUsername.getText(),
                new String(txtPassword.getPassword()),
                new String(txtConfirmPassword.getPassword()),
                cmbRole.getSelectedItem().toString(),
                txtDepartment.getText(),
                txtContactInfo.getText()
        ));

        btnMain.addActionListener(e -> {
            frame.dispose();
            new MainUI().showMainScreen();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void register(String userId, String fullName, String username, String password, String confirmPassword, String role, String department, String contactInfo) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match!");
            return;
        }

        try {
            String json = String.format("{\"userId\":\"%s\", \"fullName\":\"%s\", \"username\":\"%s\", \"password\":\"%s\", \"role\":\"%s\", \"department\":\"%s\", \"contactInfo\":\"%s\"}", 
                                         userId, fullName, username, password, role, department, contactInfo);

            // Sending registration request
            URL url = new URL("http://localhost:8080/api/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully registered; Now attempt to get the JWT token
                String authToken = getAuthToken(username, password);
                
                if (authToken != null) {
                    JOptionPane.showMessageDialog(frame, "Registration successful!");
                    frame.dispose(); // Dispose of the current frame
                    new EmployeeFormUI(authToken).showForm(role); // Pass the JWT token to the EmployeeFormUI
                } else {
                    JOptionPane.showMessageDialog(frame, "Error retrieving JWT token.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed! Server returned: " + responseCode);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    // Method to obtain the JWT token after registration
    private String getAuthToken(String username, String password) {
        try {
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

            // Request to get JWT token
            URL url = new URL("http://localhost:8080/api/authenticate"); // Assuming the endpoint for login is '/api/authenticate'
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Parse the response to extract the token
                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                JSONObject jsonResponse = new JSONObject(response);
                return jsonResponse.getString("authToken"); // Assuming the token is in 'authToken'
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
