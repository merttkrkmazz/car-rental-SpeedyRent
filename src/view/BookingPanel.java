package view;

import javax.swing.*;
import java.awt.*;

/**
 * Rezervasyon paneli. Logout yaparak login ekranına döner.
 */
public class BookingPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;

    public BookingPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new BorderLayout());

        // Başlık
        JLabel title = new JLabel("Booking Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Orta içerik
        JPanel center = new JPanel(new FlowLayout());
        center.add(new JLabel("Welcome to SpeedyRent Booking!"));
        add(center, BorderLayout.CENTER);

        // Alt bölüm: Logout
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        south.add(logoutButton);
        add(south, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> cardLayout.show(container, "login"));
    }
}
