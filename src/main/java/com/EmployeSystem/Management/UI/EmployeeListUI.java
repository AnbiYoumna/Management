package com.EmployeSystem.Management.UI;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeListUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add buttons at the top (Back and Déconnexion)
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("Back");
        JButton btnDeconnexion = new JButton("LogOut");

        topPanel.add(btnBack, BorderLayout.WEST);      // Back button to the top left
        topPanel.add(btnDeconnexion, BorderLayout.EAST); // Déconnexion button to the top right
        frame.add(topPanel, BorderLayout.NORTH);

        // Labels for columns
        JLabel lblId = new JLabel("Employee ID");
        JLabel lblName = new JLabel("Full Name");
        JLabel lblDepartment = new JLabel("Department");
        JLabel lblStatus = new JLabel("Status");
        JLabel lblActions = new JLabel("Actions");

        gbc.gridx = 0;
        gbc.gridy = 0; // Start adding from the first row
        panel.add(lblId, gbc);

        gbc.gridx = 1;
        panel.add(lblName, gbc);

        gbc.gridx = 2;
        panel.add(lblDepartment, gbc);

        gbc.gridx = 3;
        panel.add(lblStatus, gbc);

        gbc.gridx = 4;
        panel.add(lblActions, gbc);

        // Fetch employees using the authToken
        try {
            URL url = new URL("http://localhost:8080/api/employees");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            System.out.println("Authorization Token: " + "Bearer " + LoginUI.getAuthToken());
            conn.setRequestProperty("Authorization", "Bearer " + LoginUI.getAuthToken()); // Add Authorization header

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

            JSONArray employees = new JSONArray(response.toString());
            int row = 1; // Start adding employees from the second row

            for (int i = 0; i < employees.length(); i++) {
                JSONObject emp = employees.getJSONObject(i);

                gbc.gridy = row;
                gbc.gridx = 0;
                panel.add(new JLabel(emp.getString("employeeId")), gbc);

                gbc.gridx = 1;
                panel.add(new JLabel(emp.getString("fullName")), gbc);

                gbc.gridx = 2;
                panel.add(new JLabel(emp.getString("department")), gbc);

                gbc.gridx = 3;
                panel.add(new JLabel(emp.getString("employmentStatus")), gbc);

                // Update Button
                JButton btnUpdate = new JButton("Update");
                gbc.gridx = 4;
                panel.add(btnUpdate, gbc);

                // Delete Button
                JButton btnDelete = new JButton("Delete");
                gbc.gridx = 5;
                panel.add(btnDelete, gbc);

                // Update Button Action
                String employeeId = emp.getString("employeeId");
                btnUpdate.addActionListener(e -> {
                    frame.dispose();
                    EmployeeFormUI employeeFormUI = new EmployeeFormUI(LoginUI.getAuthToken());
                    employeeFormUI.showForm("ROLE_HR"); // Pass authToken
                    employeeFormUI.populateForm(emp);
                });

                // Delete Button Action
                btnDelete.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this employee?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            URL deleteUrl = new URL("http://localhost:8080/api/employees/" + employeeId);
                            HttpURLConnection deleteConn = (HttpURLConnection) deleteUrl.openConnection();
                            deleteConn.setRequestMethod("DELETE");
                            deleteConn.setRequestProperty("Authorization", "Bearer " + LoginUI.getAuthToken());

                            int responseCode = deleteConn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
                                JOptionPane.showMessageDialog(frame, "Employee deleted successfully!");
                                frame.dispose(); // Refresh UI to remove the deleted employee
                                main(args); // Reload the list
                            } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                                JOptionPane.showMessageDialog(frame, "Cannot delete employee due to linked audit trail information.");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Failed to delete employee! Response code: " + responseCode);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                        }
                    }
                });



                row++;
            }

            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to fetch employees: " + e.getMessage());
        }

        // Add the panel to the frame
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Back button action
        btnBack.addActionListener(e -> {
            frame.dispose();
            EmployeeFormUI employeeFormUI = new EmployeeFormUI(LoginUI.getAuthToken());
            employeeFormUI.showForm("User Role"); // Pass the authToken
        });

        // Déconnexion button action
        btnDeconnexion.addActionListener(e -> {
            frame.dispose();
            new LoginUI().showLoginScreen();
        });

        frame.setVisible(true);
    }
}
