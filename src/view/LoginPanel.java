package view;

import controller.AuthenticationController;
import controller.AuthenticationController.UserRole;
import java.awt.*;
import javax.swing.*;

/**
 * Giriş ekranı. Kullanıcı ID ve isim ile login olur.
 */
public class LoginPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;
    private final JTextField userIdField;
    private final JTextField nameField;

    public LoginPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SpeedyRent Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("User ID:"), gbc);
        userIdField = new JTextField();
        gbc.gridx = 1;
        add(userIdField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Name:"), gbc);
        nameField = new JTextField();
        gbc.gridx = 1;
        add(nameField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String idText = userIdField.getText().trim();
        String name = nameField.getText().trim();
        int userId;
        try {
            userId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "User ID must be a valid number.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        UserRole role = AuthenticationController.login(userId, name);
        if (role == UserRole.UNKNOWN) {
            JOptionPane.showMessageDialog(this,
                    "Login failed. Please check your credentials.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Login successful: " + role,
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);
            
            if (role == UserRole.ADMIN) {
                cardLayout.show(container, "admin");
            } else if (role == UserRole.CUSTOMER) {
                // 1) Kullanıcı ID’sini CarListPanel’e geçiriyoruz
                CarListPanel clp = new CarListPanel(cardLayout, container, userId);
                container.add(clp, "carlist");
                cardLayout.show(container, "carlist");
            }
        }
    }
    
}
