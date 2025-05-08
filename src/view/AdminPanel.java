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
    private JComboBox<String> fuelBox, transBox;
    private JButton addButton, deleteButton, updateButton;

    public AdminPanel(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        setLayout(new BorderLayout());

        // === ÜST Panel: Form alanları ===
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));

        modelField = new JTextField();
        rentField = new JTextField();
        seatField = new JTextField();
        fuelBox = new JComboBox<>(new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        transBox = new JComboBox<>(new String[]{"Automatic", "Manual"});

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

        add(formPanel, BorderLayout.NORTH);

        // === ORTA Panel: Araç tablosu ===
        String[] columns = {"ID", "Model", "Fuel", "Transmission", "Seats", "Rent", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        // === ALT Panel: Butonlar ===
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
        // now shows ALL cars, not just available
        List<Car> cars = CarController.getAllCarsAsObjects();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getId(),
                    car.getModel(),
                    car.getFuelType(),
                    car.getTransmission(),
                    car.getSeatingCapacity(),
                    car.getRentalPrice(),
                    car.isAvailable()
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
        rentField.setText(tableModel.getValueAt(row, 5).toString());
    }

    private void onAddCar() {
        try {
            String model = modelField.getText().trim();
            double rent = Double.parseDouble(rentField.getText().trim());
            int seats = Integer.parseInt(seatField.getText().trim());
            String fuel = (String) fuelBox.getSelectedItem();
            String trans = (String) transBox.getSelectedItem();

            // 1. Spec'i veritabanına ekle
            boolean specAdded = VehicleSpecificationController.addSpecification("black", fuel, trans, seats); // renk geçici
            if (!specAdded) {
                JOptionPane.showMessageDialog(this, "Teknik özellik eklenemedi.");
                return;
            }

            // 2. Son eklenen spec_id'yi bul (en yüksek ID varsayımı)
            List<String> allSpecs = VehicleSpecificationController.getAllSpecifications();
            int lastSpecId = allSpecs.size(); // En son eklenenin ID'si olduğunu varsayarsak

            // 3. Aracı ekle
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
        boolean success = CarController.deleteCar(carId);
        if (success) {
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

            // 1) update Car table
            boolean carOk = CarController.updateCar(carId, model, rent, "available");

            // 2) update its VehicleSpecification row
            int specId = CarController.getSpecificationIdForCar(carId);
            boolean specOk = false;
            if (specId > 0) {
                specOk = VehicleSpecificationController.updateSpecification(
                        specId,
                        "black",   // or add a color field later
                        fuel,
                        trans,
                        seats
                );
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
