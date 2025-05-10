package view;

import controller.CarController;
import controller.VehicleSpecificationController;
import model.Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;

    private JTable carTable;
    private DefaultTableModel tableModel;

    private JTextField modelField, rentField, seatField;
    private JComboBox<String> fuelBox, transBox, colorBox, statusBox;
    private JButton addButton, deleteButton, updateButton;

    public AdminPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        setLayout(new BorderLayout());

        // === Top Panel (Form Area) ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        modelField = new JTextField();
        rentField = new JTextField();
        seatField = new JTextField();

        fuelBox = new JComboBox<>(new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{"Black", "White", "Red", "Blue", "Silver"});
        statusBox = new JComboBox<>(new String[]{"available", "reserved"});

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(modelField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Transmission:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(transBox, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Rent/Day:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(rentField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Seats:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(seatField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(fuelBox, gbc);
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(colorBox, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(statusBox, gbc);

        add(formPanel, BorderLayout.NORTH);

        // === Table Panel ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Rent", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Events ===
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
                    car.getRentalPrice(), car.getStatus()
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
        statusBox.setSelectedItem(tableModel.getValueAt(row, 7).toString());
    }

    private void clearForm() {
        modelField.setText("");
        rentField.setText("");
        seatField.setText("");
        fuelBox.setSelectedIndex(0);
        transBox.setSelectedIndex(0);
        colorBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        carTable.clearSelection();
    }

    private void onAddCar() {
        try {
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();
            String color = (String) colorBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();

            boolean specAdded = VehicleSpecificationController.addSpecification(color, fuel, trans, seats);
            if (!specAdded) {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle specification.");
                return;
            }

            int lastSpecId = VehicleSpecificationController.getAllSpecifications().size();
            boolean added = CarController.addCar(model, rent, 0.0, 0, status, lastSpecId);

            if (added) {
                JOptionPane.showMessageDialog(this, "Car successfully added.");
                loadCars();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add car.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rent and seat fields must be valid numbers.");
        }
    }

    private void onDeleteCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.");
            return;
        }
        int carId = (int) tableModel.getValueAt(row, 0);
        if (CarController.deleteCar(carId)) {
            JOptionPane.showMessageDialog(this, "Car successfully deleted.");
            loadCars();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete car.");
        }
    }

    private void onUpdateCar() {
        int row = carTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to update.");
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
            String status = (String) statusBox.getSelectedItem();

            boolean carOk = CarController.updateCar(carId, model, rent, status);
            int specId = CarController.getSpecificationIdForCar(carId);
            boolean specOk = false;

            if (specId > 0) {
                specOk = VehicleSpecificationController.updateSpecification(specId, color, fuel, trans, seats);
            }

            if (carOk && specOk) {
                JOptionPane.showMessageDialog(this, "Car successfully updated.");
                loadCars();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update car.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }
}
