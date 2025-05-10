package view;

import controller.AuthenticationController;
import controller.AuthenticationController.UserRole;

import javax.swing.*;
import java.awt.*;

/**
 * Giriş ekranı. Kullanıcı adı ve şifre ile giriş yapar.
 */
public class LoginPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

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
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField();
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField();
        gbc.gridx = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserRole role = AuthenticationController.login(username, password);
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
                CarListPanel clp = new CarListPanel(cardLayout, container);
                container.add(clp, "carlist");
                cardLayout.show(container, "carlist");
            }
        }
    }
    
}
