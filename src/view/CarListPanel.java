package view;

import controller.CarController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarListPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;

    private JTable carTable;
    private JComboBox<String> fuelFilterBox;
    private JComboBox<String> transmissionFilterBox;
    private JComboBox<String> sortBox;
    private JButton rentButton;

    private List<Car> allCars;

    public CarListPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        setLayout(new BorderLayout());

        // === Top Panel: Filter / Sort ===
        JPanel topPanel = new JPanel(new FlowLayout());

        fuelFilterBox = new JComboBox<>(new String[]{"All", "Gasoline", "Diesel", "Electric", "Hybrid"});
        transmissionFilterBox = new JComboBox<>(new String[]{"All", "Automatic", "Manual"});
        sortBox = new JComboBox<>(new String[]{"Sort by Price ↑", "Sort by Price ↓"});

        topPanel.add(new JLabel("Fuel:"));
        topPanel.add(fuelFilterBox);
        topPanel.add(new JLabel("Transmission:"));
        topPanel.add(transmissionFilterBox);
        topPanel.add(sortBox);

        add(topPanel, BorderLayout.NORTH);

        // === Center Panel: Car Table ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === Bottom Panel: Rent Button ===
        rentButton = new JButton("Rent");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(rentButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Events ===
        fuelFilterBox.addActionListener(e -> applyFilters());
        transmissionFilterBox.addActionListener(e -> applyFilters());
        sortBox.addActionListener(e -> applyFilters());
        rentButton.addActionListener(e -> onRentButton());

        // === Load Initial Data ===
        loadCars();
    }

    private void loadCars() {
        allCars = CarController.getAvailableCarsAsObjects();
        applyFilters();
    }

    private void applyFilters() {
        String selectedFuel = (String) fuelFilterBox.getSelectedItem();
        String selectedTrans = (String) transmissionFilterBox.getSelectedItem();
        String sortOption    = (String) sortBox.getSelectedItem();

        List<Car> filtered = allCars.stream()
                .filter(c -> selectedFuel.equals("All") || c.getFuelType().equalsIgnoreCase(selectedFuel))
                .filter(c -> selectedTrans.equals("All") || c.getTransmission().equalsIgnoreCase(selectedTrans))
                .collect(Collectors.toList());

        if (sortOption.equals("Sort by Price ↑")) {
            filtered.sort((a, b) -> Double.compare(a.getRentalPrice(), b.getRentalPrice()));
        } else {
            filtered.sort((a, b) -> Double.compare(b.getRentalPrice(), a.getRentalPrice()));
        }

        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        model.setRowCount(0);
        for (Car c : filtered) {
            model.addRow(new Object[]{
                    c.getId(), c.getModel(),
                    c.getFuelType(), c.getTransmission(),
                    c.getSeatingCapacity(), c.getColor(),
                    c.getRentalPrice(), c.isAvailable()
            });
        }
    }

    private void onRentButton() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.");
            return;
        }

        int carId = (int) carTable.getValueAt(row, 0);
        Car selected = allCars.stream()
                .filter(c -> c.getId() == carId)
                .findFirst()
                .orElse(null);

        if (selected != null) {
            BookingPanel bookingPanel = new BookingPanel(cardLayout, container, selected, 0);
            container.add(bookingPanel, "booking");
            cardLayout.show(container, "booking");
        }
    }
}
