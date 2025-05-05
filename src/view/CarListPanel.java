package view;

import app.Main;
import controller.CarController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarListPanel extends JPanel {
    private JTable carTable;
    private CarController carController;
    private JComboBox<String> fuelFilterBox;
    private JComboBox<String> transmissionFilterBox;
    private JComboBox<String> sortBox;
    private JButton rentButton;

    private List<Car> allCars;

    public CarListPanel() {
        this.carController = new CarController();
        setLayout(new BorderLayout());

        // === ÜST: Filtre ve Sıralama ===
        JPanel topPanel = new JPanel(new FlowLayout());

        fuelFilterBox = new JComboBox<>(new String[]{"All", "Gasoline", "Diesel", "Electric"});
        transmissionFilterBox = new JComboBox<>(new String[]{"All", "Automatic", "Manual"});
        sortBox = new JComboBox<>(new String[]{"Sort by Price ↑", "Sort by Price ↓"});

        topPanel.add(new JLabel("Fuel:"));
        topPanel.add(fuelFilterBox);
        topPanel.add(new JLabel("Transmission:"));
        topPanel.add(transmissionFilterBox);
        topPanel.add(sortBox);

        add(topPanel, BorderLayout.NORTH);

        // === ORTA: Araç Tablosu ===
        String[] columns = {"ID", "Brand", "Model", "Fuel", "Transmission", "Seats", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === ALT: Kirala Butonu ===
        rentButton = new JButton("Kirala");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rentButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Eventler ===
        fuelFilterBox.addActionListener(e -> applyFilters());
        transmissionFilterBox.addActionListener(e -> applyFilters());
        sortBox.addActionListener(e -> applyFilters());
        rentButton.addActionListener(e -> onRentButton());

        // === Veri Yükle ===
        loadCars();
    }

    private void loadCars() {
        allCars = carController.getAvailableCars(); // veritabanından tam liste
        applyFilters(); // filtre uygulanmış listeyi göster
    }

    private void applyFilters() {
        String selectedFuel = (String) fuelFilterBox.getSelectedItem();
        String selectedTrans = (String) transmissionFilterBox.getSelectedItem();
        String sortOption = (String) sortBox.getSelectedItem();

        List<Car> filtered = allCars.stream()
                .filter(car -> selectedFuel.equals("All") || car.getFuelType().equalsIgnoreCase(selectedFuel))
                .filter(car -> selectedTrans.equals("All") || car.getTransmission().equalsIgnoreCase(selectedTrans))
                .collect(Collectors.toList());

        if (sortOption.equals("Sort by Price ↑")) {
            filtered.sort((a, b) -> Double.compare(a.getRentalPrice(), b.getRentalPrice()));
        } else {
            filtered.sort((a, b) -> Double.compare(b.getRentalPrice(), a.getRentalPrice()));
        }

        // tabloyu güncelle
        DefaultTableModel model = (DefaultTableModel) carTable.getModel();
        model.setRowCount(0);
        for (Car car : filtered) {
            model.addRow(new Object[]{
                    car.getId(), car.getBrand(), car.getModel(), car.getFuelType(),
                    car.getTransmission(), car.getSeatingCapacity(), car.getRentalPrice()
            });
        }
    }

    private void onRentButton() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir araç seçin.");
            return;
        }

        int carId = (int) carTable.getValueAt(selectedRow, 0);
        Car selectedCar = allCars.stream().filter(c -> c.getId() == carId).findFirst().orElse(null);

        if (selectedCar != null) {
            // Panel geçişi
            JFrame topFrame = Main.frame; // app.Main.java'dan frame referansı al
            topFrame.setContentPane(new BookingPanel(selectedCar));
            topFrame.revalidate();
            topFrame.repaint();
        }
    }

}
