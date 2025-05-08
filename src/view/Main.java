// src/view/Main.java
package view;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SpeedyRent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel container  = new JPanel(cardLayout);

            // === Panellerin oluşturulması ===
            LoginPanel    loginPanel   = new LoginPanel(cardLayout, container);
            CarListPanel  carListPanel = new CarListPanel(cardLayout, container);
            AdminPanel    adminPanel   = new AdminPanel(cardLayout, container);

            // === Panelleri karta ekle ===
            container.add(loginPanel,   "login");
            container.add(carListPanel, "carlist");
            container.add(adminPanel,   "admin");

            frame.setContentPane(container);
            frame.setVisible(true);

            // Başlangıç ekranı
            cardLayout.show(container, "login");
        });
    }
}
