package com.EmployeSystem.Management.UI;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmployeeFormUI {
    private JFrame frame;
    private JTextField txtFullName;
    private JTextField txtEmployeeId;
    private JTextField txtJobTitle;
    private JTextField txtDepartment;
    private JTextField txtHireDate;
    private JTextField txtEmploymentStatus;
    private JTextField txtContactInfo;
    private JTextField txtAddress;

    private final String authToken; // Store the token passed from LoginUI
    private String employeeIdToUpdate = null; // Used to identify employee for update

    public EmployeeFormUI(String authToken) {
        this.authToken = authToken;
    }

    public void showForm(String role) {
        frame = new JFrame(role + " Employee Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new MigLayout("fill", "[grow][]", "[]20[][][][][][][][][]"));

        JButton btnDeconnexion = new JButton("LogOut");
        panel.add(btnDeconnexion, "cell 1 0, right, wrap");

        // Form fields
        JLabel lblFullName = new JLabel("Full Name:");
        txtFullName = new JTextField(20);

        JLabel lblEmployeeId = new JLabel("Employee ID:");
        txtEmployeeId = new JTextField(20);

        JLabel lblJobTitle = new JLabel("Job Title:");
        txtJobTitle = new JTextField(20);

        JLabel lblDepartment = new JLabel("Department:");
        txtDepartment = new JTextField(20);

        JLabel lblHireDate = new JLabel("Hire Date:");
        txtHireDate = new JTextField(20);

        JLabel lblEmploymentStatus = new JLabel("Employment Status:");
        txtEmploymentStatus = new JTextField(20);

        JLabel lblContactInfo = new JLabel("Contact Info:");
        txtContactInfo = new JTextField(20);

        JLabel lblAddress = new JLabel("Address:");
        txtAddress = new JTextField(20);

        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnViewEmployees = new JButton("View All Employees");
        JButton btnAudit = new JButton("Audit Trail");

        // Add components to the panel
        panel.add(lblEmployeeId, "cell 0 1");
        panel.add(txtEmployeeId, "cell 1 1, growx, wrap");

        panel.add(lblFullName, "cell 0 2");
        panel.add(txtFullName, "cell 1 2, growx, wrap");

        panel.add(lblJobTitle, "cell 0 3");
        panel.add(txtJobTitle, "cell 1 3, growx, wrap");

        panel.add(lblDepartment, "cell 0 4");
        panel.add(txtDepartment, "cell 1 4, growx, wrap");

        panel.add(lblHireDate, "cell 0 5");
        panel.add(txtHireDate, "cell 1 5, growx, wrap");

        panel.add(lblEmploymentStatus, "cell 0 6");
        panel.add(txtEmploymentStatus, "cell 1 6, growx, wrap");

        panel.add(lblContactInfo, "cell 0 7");
        panel.add(txtContactInfo, "cell 1 7, growx, wrap");

        panel.add(lblAddress, "cell 0 8");
        panel.add(txtAddress, "cell 1 8, growx, wrap");

        panel.add(btnSave, "cell 0 9, span 2, split 2, center");
        panel.add(btnUpdate);
        panel.add(btnViewEmployees);
        panel.add(btnAudit);

        // Button actions
        btnSave.addActionListener(e -> saveEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnViewEmployees.addActionListener(e -> {
            frame.dispose();
            new EmployeeListUI().main(null);
        });

        btnAudit.addActionListener(e -> {
            frame.dispose();
            
            
            AuditTrailWindow auditWindow = new AuditTrailWindow(authToken);
            auditWindow.setVisible(true);
        });
        
        btnDeconnexion.addActionListener(e -> {
            frame.dispose();
            new MainUI().showMainScreen();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void saveEmployee() {
        String fullName = txtFullName.getText();
        String employeeId = txtEmployeeId.getText();
        String jobTitle = txtJobTitle.getText();
        String department = txtDepartment.getText();
        String hireDate = txtHireDate.getText();
        String employmentStatus = txtEmploymentStatus.getText();
        String contactInfo = txtContactInfo.getText();
        String address = txtAddress.getText();

        if (fullName.isEmpty() || employeeId.isEmpty() || jobTitle.isEmpty() || department.isEmpty() || hireDate.isEmpty() || employmentStatus.isEmpty() || contactInfo.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!");
            return;
        }

        try {
            String json = String.format(
                    "{\"fullName\":\"%s\", \"employeeId\":\"%s\", \"jobTitle\":\"%s\", \"department\":\"%s\", \"hireDate\":\"%s\", \"employmentStatus\":\"%s\", \"contactInfo\":\"%s\", \"address\":\"%s\"}",
                    fullName, employeeId, jobTitle, department, hireDate, employmentStatus, contactInfo, address
            );

            URL url = new URL("http://localhost:8080/api/employees");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(frame, "Employee saved successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to save employee!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        String fullName = txtFullName.getText();
        String employeeId = txtEmployeeId.getText();
        String jobTitle = txtJobTitle.getText();
        String department = txtDepartment.getText();
        String hireDate = txtHireDate.getText();
        String employmentStatus = txtEmploymentStatus.getText();
        String contactInfo = txtContactInfo.getText();
        String address = txtAddress.getText();

        if (employeeIdToUpdate == null || employeeIdToUpdate.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No employee selected for update!");
            return;
        }

        if (fullName.isEmpty() || jobTitle.isEmpty() || department.isEmpty() || hireDate.isEmpty() || employmentStatus.isEmpty() || contactInfo.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!");
            return;
        }

        try {
            String json = String.format(
                    "{\"fullName\":\"%s\", \"employeeId\":\"%s\", \"jobTitle\":\"%s\", \"department\":\"%s\", \"hireDate\":\"%s\", \"employmentStatus\":\"%s\", \"contactInfo\":\"%s\", \"address\":\"%s\"}",
                    fullName, employeeId, jobTitle, department, hireDate, employmentStatus, contactInfo, address
            );

            URL url = new URL("http://localhost:8080/api/employees/" + employeeIdToUpdate);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(frame, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update employee!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    // Add populateForm method
    public void populateForm(JSONObject emp) {
        // Populate the form fields with the employee data from the JSONObject
        txtEmployeeId.setText(emp.optString("employeeId"));
        txtFullName.setText(emp.optString("fullName"));
        txtJobTitle.setText(emp.optString("jobTitle"));
        txtDepartment.setText(emp.optString("department"));
        txtHireDate.setText(emp.optString("hireDate"));
        txtEmploymentStatus.setText(emp.optString("employmentStatus"));
        txtContactInfo.setText(emp.optString("contactInfo"));
        txtAddress.setText(emp.optString("address"));
        
        // Save the employee ID for updating
        employeeIdToUpdate = emp.optString("employeeId");
    }
}
