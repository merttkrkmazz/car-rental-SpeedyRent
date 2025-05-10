package view;

import controller.CarController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarListPanel extends JPanel {
    private final int currentUserId;

    private final CardLayout cardLayout;
    private final JPanel container;


    private JTable carTable;
    private JTextField minPriceField, maxPriceField;
    private JTextField minSeatField, maxSeatField;
    private JComboBox<String> fuelBox, transBox, colorBox, sortBox;
    private JButton applyButton, resetButton, rentButton;

    private List<Car> allCars;

    public CarListPanel(CardLayout cardLayout, JPanel container, int currentUserId) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.currentUserId  = currentUserId;
        setLayout(new BorderLayout());

        // === Top Filter Panel ===
        JPanel filterPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        fuelBox = new JComboBox<>(new String[]{"All", "Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"All", "Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{"All", "Black", "White", "Red", "Blue", "Grey"});
        sortBox = new JComboBox<>(new String[]{"Sort by Price ↑", "Sort by Price ↓"});

        minPriceField = new JTextField(8);
        maxPriceField = new JTextField(8);
        minSeatField = new JTextField(8);
        maxSeatField = new JTextField(8);

        filterPanel.add(new JLabel("Fuel Type:"));
        filterPanel.add(fuelBox);
        filterPanel.add(new JLabel("Transmission Type:"));
        filterPanel.add(transBox);

        filterPanel.add(new JLabel("Min Price:"));
        filterPanel.add(minPriceField);
        filterPanel.add(new JLabel("Max Price:"));
        filterPanel.add(maxPriceField);

        filterPanel.add(new JLabel("Min Seats:"));
        filterPanel.add(minSeatField);
        filterPanel.add(new JLabel("Max Seats:"));
        filterPanel.add(maxSeatField);

        filterPanel.add(new JLabel("Color:"));
        filterPanel.add(colorBox);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortBox);

        applyButton = new JButton("Apply Filters");
        resetButton = new JButton("Reset Filters");

        filterPanel.add(resetButton);
        filterPanel.add(applyButton);
        filterPanel.add(new JLabel());
        filterPanel.add(new JLabel());

        add(filterPanel, BorderLayout.NORTH);

        // === Table Panel ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        carTable = new JTable(model);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(carTable);
        add(scrollPane, BorderLayout.CENTER);

        // Hide the ID column
        carTable.getColumnModel().getColumn(0).setMinWidth(0);
        carTable.getColumnModel().getColumn(0).setMaxWidth(0);
        carTable.getColumnModel().getColumn(0).setWidth(0);

        // === Bottom Panel ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rentButton = new JButton("Rent");
        bottomPanel.add(rentButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Events ===
        applyButton.addActionListener(e -> applyFilters());
        resetButton.addActionListener(e -> {
            fuelBox.setSelectedIndex(0);
            transBox.setSelectedIndex(0);
            colorBox.setSelectedIndex(0);
            sortBox.setSelectedIndex(0);
            minPriceField.setText("");
            maxPriceField.setText("");
            minSeatField.setText("");
            maxSeatField.setText("");
            applyFilters();
        });
        rentButton.addActionListener(e -> onRent());

        loadCars();

        // **BURAYA EKLE**
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadCars();
            }
        });
    }

    private void loadCars() {
        allCars = CarController.getAvailableCarsAsObjects();
        applyFilters();
    }

    private void applyFilters() {
        String fuel = (String) fuelBox.getSelectedItem();
        String trans = (String) transBox.getSelectedItem();
        String color = (String) colorBox.getSelectedItem();
        String sort = (String) sortBox.getSelectedItem();

        double minPrice = parseDouble(minPriceField.getText(), 0);
        double maxPrice = parseDouble(maxPriceField.getText(), Double.MAX_VALUE);
        int minSeats = parseInt(minSeatField.getText(), 0);
        int maxSeats = parseInt(maxSeatField.getText(), Integer.MAX_VALUE);

        List<Car> filtered = allCars.stream()
                .filter(c -> fuel.equals("All") || c.getFuelType().equalsIgnoreCase(fuel))
                .filter(c -> trans.equals("All") || c.getTransmission().equalsIgnoreCase(trans))
                .filter(c -> color.equals("All") || c.getColor().equalsIgnoreCase(color))
                .filter(c -> c.getRentalPrice() >= minPrice && c.getRentalPrice() <= maxPrice)
                .filter(c -> c.getSeatingCapacity() >= minSeats && c.getSeatingCapacity() <= maxSeats)
                .collect(Collectors.toList());

        if (sort.equals("Sort by Price ↑")) {
            filtered.sort((a, b) -> Double.compare(a.getRentalPrice(), b.getRentalPrice()));
        } else {
            filtered.sort((a, b) -> Double.compare(b.getRentalPrice(), a.getRentalPrice()));
        }

        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        model.setRowCount(0);
        for (Car c : filtered) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getModel(),
                    c.getFuelType(),
                    c.getTransmission(),
                    c.getSeatingCapacity(),
                    c.getColor(),
                    c.getRentalPrice(),
                    c.getStatus()
            });
        }
    }

    private double parseDouble(String text, double defaultVal) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private int parseInt(String text, int defaultVal) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private void onRent() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.");
            return;
        }

        int carId = (int) carTable.getValueAt(row, 0);
        Car selected = allCars.stream().filter(c -> c.getId() == carId).findFirst().orElse(null);

        if (selected != null) {
            BookingPanel bookingPanel = new BookingPanel(cardLayout, container, selected, currentUserId);
            container.add(bookingPanel, "booking");
            cardLayout.show(container, "booking");
        }
    }
}
