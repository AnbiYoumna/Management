package com.EmployeSystem.Management.UI;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginUI {
    private JFrame frame;
    private static String authToken = ""; // Store the authentication token globally

    public void showLoginScreen() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new MigLayout("", "[][grow]", "[][]20[]"));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(20);

        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Login");
        JButton btnMain = new JButton("Back");

        panel.add(lblUsername);
        panel.add(txtUsername, "growx, wrap");

        panel.add(lblPassword);
        panel.add(txtPassword, "growx, wrap");

        panel.add(btnLogin, "split 2, center");
        panel.add(btnMain, "center");

        btnLogin.addActionListener(e -> login(txtUsername.getText(), new String(txtPassword.getPassword())));
        btnMain.addActionListener(e -> {
            frame.dispose();
            new MainUI().showMainScreen();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!");
            return;
        }

        try {
            String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

            URL url = new URL("http://localhost:8080/api/users/login");
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
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    String response = scanner.nextLine();
                    System.out.println("Raw Response: " + response);

                    if (!response.trim().startsWith("{")) {
                        JOptionPane.showMessageDialog(frame, "Unexpected server response: " + response);
                        return;
                    }
                    JSONObject jsonResponse = new JSONObject(response);
                    authToken = jsonResponse.getString("authToken"); // Store the token

                    // Print the token to the console
                    System.out.println("Received JWT Token: " + authToken);

                    // Add the "Bearer " prefix to the token for authorization
                    String bearerToken = "Bearer " + authToken;
                    System.out.println("Authorization Token: " + bearerToken);

                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new EmployeeFormUI(bearerToken).showForm("ROLE_HR");
                }
              
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
        }
    }

    public static String getAuthToken() {
        return authToken; // Return the token when needed
    }
}
