package com.EmployeSystem.Management.UI;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class MainUI {
    private JFrame frame;

    public void showMainScreen() {
        frame = new JFrame("Employee System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Updated MigLayout constraints
        JPanel panel = new JPanel(new MigLayout("fill, align center", "[center]", "[]20[]20[]"));

        // Centered label
        JLabel label = new JLabel("WELCOME TO EMPLOYEE MANAGEMENT APPLICATION !");
        panel.add(label, "cell 0 0, align center, wrap");

        // Buttons placed below the label
        JButton btnRegister = new JButton("Register");
        JButton btnLogin = new JButton("Login");

        panel.add(btnRegister, "split 2, center"); 
        panel.add(btnLogin, "center"); 

        // Button actions
        btnRegister.addActionListener(e -> {
            frame.dispose();
            new RegisterUI().showRegisterScreen();
        });

        btnLogin.addActionListener(e -> {
            frame.dispose();
            new LoginUI().showLoginScreen();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.showMainScreen();
        });
    }
}
