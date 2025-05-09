package view;

import controller.CarController;
import controller.VehicleSpecificationController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// === imports aynı ===

public class AdminPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;
    private JTable carTable;
    private DefaultTableModel tableModel;

    private JTextField modelField, rentField, seatField;
    private JComboBox<String> fuelBox, transBox, colorBox;
    private JButton addButton, deleteButton, updateButton;

    public AdminPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        setLayout(new BorderLayout());

        // === ÜST Panel: Form alanları ===
        JPanel formPanel = new JPanel(new GridLayout(2, 7, 5, 5));

        modelField = new JTextField();
        rentField = new JTextField();
        seatField = new JTextField();
        fuelBox  = new JComboBox<>(new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{"Black", "White", "Red", "Blue", "Silver"});

        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Rent:"));
        formPanel.add(rentField);
        formPanel.add(new JLabel("Seats:"));
        formPanel.add(seatField);
        formPanel.add(new JLabel("Fuel:"));
        formPanel.add(fuelBox);
        formPanel.add(new JLabel("Transmission:"));
        formPanel.add(transBox);
        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorBox);

        add(formPanel, BorderLayout.NORTH);

        // === Orta Panel ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Rent", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === Alt Panel ===
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ekle");
        deleteButton = new JButton("Sil");
        updateButton = new JButton("Güncelle");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Eventler ===
        addButton.addActionListener(e -> onAddCar());
        deleteButton.addActionListener(e -> onDeleteCar());
        updateButton.addActionListener(e -> onUpdateCar());
        carTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        loadCars();
    }

    private void loadCars() {
        tableModel.setRowCount(0);
        List<Car> cars = CarController.getAllCarsAsObjects();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getId(), car.getModel(),
                    car.getFuelType(), car.getTransmission(),
                    car.getSeatingCapacity(), car.getColor(),
                    car.getRentalPrice(), car.isAvailable()
            });
        }
    }

    private void fillFormFromTable() {
        int row = carTable.getSelectedRow();
        if (row == -1) return;

        modelField.setText(tableModel.getValueAt(row, 1).toString());
        fuelBox.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        transBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        seatField.setText(tableModel.getValueAt(row, 4).toString());
        colorBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        rentField.setText(tableModel.getValueAt(row, 6).toString());
    }

    private void onAddCar() {
        try {
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();
            String color = (String) colorBox.getSelectedItem();

            boolean specAdded = VehicleSpecificationController.addSpecification(color, fuel, trans, seats);
            if (!specAdded) {
                JOptionPane.showMessageDialog(this, "Teknik özellik eklenemedi.");
                return;
            }

            int lastSpecId = VehicleSpecificationController.getAllSpecifications().size();
            boolean added = CarController.addCar(model, rent, 0.0, 0, "available", lastSpecId);

            if (added) {
                JOptionPane.showMessageDialog(this, "Araç başarıyla eklendi.");
                loadCars();
            } else {
                JOptionPane.showMessageDialog(this, "Araç eklenemedi.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kira ücreti ve koltuk sayısı sayısal olmalıdır.");
        }
    }

    private void onDeleteCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silmek için bir araç seçin.");
            return;
        }
        int carId = (int) tableModel.getValueAt(row, 0);
        if (CarController.deleteCar(carId)) {
            JOptionPane.showMessageDialog(this, "Araç silindi.");
            loadCars();
        } else {
            JOptionPane.showMessageDialog(this, "Silme işlemi başarısız oldu.");
        }
    }

    private void onUpdateCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Güncellemek için bir araç seçin.");
            return;
        }
        try {
            int carId = (int) tableModel.getValueAt(row, 0);
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();
            String color = (String) colorBox.getSelectedItem();

            boolean carOk = CarController.updateCar(carId, model, rent, "available");
            int specId = CarController.getSpecificationIdForCar(carId);
            boolean specOk = false;

            if (specId > 0) {
                specOk = VehicleSpecificationController.updateSpecification(specId, color, fuel, trans, seats);
            }

            if (carOk && specOk) {
                JOptionPane.showMessageDialog(this, "Araç başarıyla güncellendi.");
                loadCars();
            } else {
                JOptionPane.showMessageDialog(this, "Güncelleme başarısız oldu.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Geçerli sayısal değerler giriniz.");
        }
    }
}
