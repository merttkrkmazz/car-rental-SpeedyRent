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
    private JComboBox<String> fuelBox, transBox, colorBox;
    private JButton addButton, deleteButton, updateButton;

    public AdminPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        setLayout(new BorderLayout());

        // === Top Panel: Form Fields ===
        JPanel formPanel = new JPanel(new GridLayout(2, 7, 5, 5));

        modelField = new JTextField();
        rentField = new JTextField();
        seatField = new JTextField();
        fuelBox  = new JComboBox<>(new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"Automatic", "Manual"});
        colorBox = new JComboBox<>(new String[]{"Black", "White", "Red", "Blue", "Silver"});

        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Rent per Day:"));
        formPanel.add(rentField);
        formPanel.add(new JLabel("Seats:"));
        formPanel.add(seatField);
        formPanel.add(new JLabel("Fuel Type:"));
        formPanel.add(fuelBox);
        formPanel.add(new JLabel("Transmission:"));
        formPanel.add(transBox);
        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorBox);

        add(formPanel, BorderLayout.NORTH);

        // === Center Panel: Car Table ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Color", "Rent", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === Bottom Panel: Buttons ===
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

    private void clearForm() {
        modelField.setText("");
        rentField.setText("");
        seatField.setText("");
        fuelBox.setSelectedIndex(0);
        transBox.setSelectedIndex(0);
        colorBox.setSelectedIndex(0);
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

            boolean specAdded = VehicleSpecificationController.addSpecification(color, fuel, trans, seats);
            if (!specAdded) {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle specifications.");
                return;
            }

            int lastSpecId = VehicleSpecificationController.getAllSpecifications().size();
            boolean added = CarController.addCar(model, rent, 0.0, 0, "available", lastSpecId);

            if (added) {
                JOptionPane.showMessageDialog(this, "Car successfully added.");
                loadCars();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add car.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rent and seat count must be numeric.");
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

            boolean carOk = CarController.updateCar(carId, model, rent, "available");
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
