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

        // === ÜST Panel: Filtre/Sıralama ===
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

        // === ORTA Panel: Tablo ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === ALT Panel: Kirala butonu ===
        rentButton = new JButton("Kirala");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(rentButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Eventler ===
        fuelFilterBox.addActionListener(e -> applyFilters());
        transmissionFilterBox.addActionListener(e -> applyFilters());
        sortBox.addActionListener(e -> applyFilters());
        rentButton.addActionListener(e -> onRentButton());

        // === Verileri Yükle ===
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
            filtered.sort((a,b) -> Double.compare(a.getRentalPrice(), b.getRentalPrice()));
        } else {
            filtered.sort((a,b) -> Double.compare(b.getRentalPrice(), a.getRentalPrice()));
        }

        DefaultTableModel m = (DefaultTableModel)carTable.getModel();
        m.setRowCount(0);
        for (Car c : filtered) {
            m.addRow(new Object[]{
                c.getId(), c.getModel(),
                c.getFuelType(), c.getTransmission(),
                c.getSeatingCapacity(), c.getRentalPrice(),
                c.isAvailable()
            });
        }
    }

    private void onRentButton() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir araç seçin.");
            return;
        }

        int   carId = (int)carTable.getValueAt(row, 0);
        Car   sel   = allCars.stream().filter(c -> c.getId()==carId).findFirst().orElse(null);

        if (sel != null) {
            // now matches your constructor (Car + dummy userId = 0)
            BookingPanel bp = new BookingPanel(cardLayout, container, sel, 0);
            container.add(bp, "booking");
            cardLayout.show(container, "booking");
        }
    }
}
