package view;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SpeedyRent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            LoginPanel loginPanel = new LoginPanel(cardLayout, container);
            BookingPanel bookingPanel = new BookingPanel(cardLayout, container);

            container.add(loginPanel, "login");
            container.add(bookingPanel, "booking");

            frame.setContentPane(container);
            frame.setVisible(true);

            cardLayout.show(container, "login");
        });
    }
}
