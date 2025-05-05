package view;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Sisteme uygun görünüm
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Ana pencere
            JFrame frame = new JFrame("SpeedyRent");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Kart sistemi: Tüm panellerin içinde tutulduğu yer
            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            // === Panellerin oluşturulması ===
            LoginPanel loginPanel = new LoginPanel(cardLayout, container);
            BookingPanel bookingPanel = new BookingPanel(cardLayout, container);
            CarListPanel carListPanel = new CarListPanel(cardLayout, container);

            // === Panellerin karta eklenmesi ===
            container.add(loginPanel, "login");
            container.add(bookingPanel, "booking");
            container.add(carListPanel, "carlist");

            // === Pencereye yerleştirme ===
            frame.setContentPane(container);
            frame.setVisible(true);

            // İlk açılışta login ekranı göster
            cardLayout.show(container, "login");
        });
    }
}
