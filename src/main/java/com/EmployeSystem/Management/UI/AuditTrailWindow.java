package com.EmployeSystem.Management.UI;

import com.EmployeSystem.Management.model.AuditTrail;
import com.EmployeSystem.Management.security.JwtTokenUtil;
import com.EmployeSystem.Management.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AuditTrailWindow extends JFrame {
	
	
    private JwtTokenUtil jwtTokenUtil;

    
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    public AuditTrailWindow(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;}

    private String authToken; // Store the JWT token

    private JTable auditTrailTable;
    private DefaultTableModel tableModel;

    private RestTemplate restTemplate;

    // Constructor that accepts the JWT token
    public AuditTrailWindow(String authToken) {
        this.authToken = authToken; // Set the token
        setTitle("Employee Audit Trail");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize RestTemplate
        restTemplate = new RestTemplate();

        // Table model setup
        tableModel = new DefaultTableModel(new Object[]{"Employee Name", "Change Details", "Changed By", "Change Date"}, 0);
        auditTrailTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(auditTrailTable);
        add(scrollPane, BorderLayout.CENTER);

        // Control panel with button
        JPanel panel = new JPanel(new FlowLayout());
        JButton fetchButton = new JButton("Load Audit Logs");
        fetchButton.addActionListener(e -> loadAuditLogs());
        panel.add(fetchButton);
        add(panel, BorderLayout.SOUTH);
    }

    private void loadAuditLogs() {
        try {
            if (isUserAuthorized(authToken)) {
                // Setup headers with JWT token for authorization
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + authToken);

                // Setup HTTP request with headers
                HttpEntity<String> entity = new HttpEntity<>(headers);

                // Make API call to get audit logs
                ResponseEntity<List> response = restTemplate.exchange(
                        "http://localhost:8080/api/audit-trails", // Replace with your actual API endpoint
                        HttpMethod.GET,
                        entity,
                        List.class
                );

                // Get the audit trail data from the response
                List<AuditTrail> auditTrails = response.getBody();

                // Populate the table with data
                tableModel.setRowCount(0); // Clear existing rows
                for (AuditTrail log : auditTrails) {
                    tableModel.addRow(new Object[]{
                            log.getEmployee().getFullName(),
                            log.getChangeDetails(),
                            log.getUser().getUsername(),
                            log.getChangeDate()
                    });
                }

            } else {
                JOptionPane.showMessageDialog(this, "You do not have permission to view the audit logs.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + e.getMessage());
        }
    }

    private boolean isUserAuthorized(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false; // Token is missing or empty
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return customUserDetailsService.loadUserByUsername(username)
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_HR"));
    }
}
