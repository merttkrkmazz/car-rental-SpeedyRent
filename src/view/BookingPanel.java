// src/view/BookingPanel.java
package view;

import controller.BookingController;
import controller.CarController;
import model.Car;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * BookingPanel provides a user interface for confirming a car booking.
 * It displays selected car details, allows date selection, additional services,
 * calculates the total cost, and invokes booking logic.
 */
public class BookingPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel     container;
    private final Car        selectedCar;
    private final int        currentUserId;

    private JSpinner  spinnerStartDate;
    private JSpinner  spinnerEndDate;
    private JCheckBox chkGPS;
    private JCheckBox chkChildSeat;
    private JCheckBox chkInsurance;
    private JLabel    lblTotalCost;
    private JButton   btnConfirm;
    private JButton   btnCancel;

    // Service rates per day
    private static final double GPS_RATE       = 10.0;
    private static final double CHILD_SEAT_RATE= 5.0;
    private static final double INSURANCE_RATE = 20.0;

    public BookingPanel(CardLayout cardLayout,
                        JPanel container,
                        Car selectedCar,
                        int currentUserId) {
        this.cardLayout    = cardLayout;
        this.container     = container;
        this.selectedCar   = selectedCar;
        this.currentUserId = currentUserId;
        setLayout(new BorderLayout(10, 10));
        initComponents();
        registerListeners();
        updateTotalCost();
    }

    private void initComponents() {
        // Title
        JLabel title = new JLabel("Booking: " + selectedCar.getModel(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Center panel with form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Start Date:"));
        spinnerStartDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerStartDate.setEditor(new JSpinner.DateEditor(spinnerStartDate, "yyyy-MM-dd"));
        formPanel.add(spinnerStartDate);

        formPanel.add(new JLabel("End Date:"));
        spinnerEndDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerEndDate.setEditor(new JSpinner.DateEditor(spinnerEndDate, "yyyy-MM-dd"));
        formPanel.add(spinnerEndDate);

        formPanel.add(new JLabel("Additional Services:"));
        JPanel servicesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkGPS       = new JCheckBox("GPS");
        chkChildSeat = new JCheckBox("Child Seat");
        chkInsurance = new JCheckBox("Insurance");
        servicesPanel.add(chkGPS);
        servicesPanel.add(chkChildSeat);
        servicesPanel.add(chkInsurance);
        formPanel.add(servicesPanel);

        formPanel.add(new JLabel("Total Cost:"));
        lblTotalCost = new JLabel();
        lblTotalCost.setFont(lblTotalCost.getFont().deriveFont(Font.BOLD));
        formPanel.add(lblTotalCost);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnConfirm = new JButton("Confirm Booking");
        btnCancel  = new JButton("Cancel");
        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void registerListeners() {
        spinnerStartDate.addChangeListener(e -> updateTotalCost());
        spinnerEndDate.addChangeListener(e -> updateTotalCost());
        chkGPS.addItemListener(e -> updateTotalCost());
        chkChildSeat.addItemListener(e -> updateTotalCost());
        chkInsurance.addItemListener(e -> updateTotalCost());

        btnConfirm.addActionListener(e -> doConfirmBooking());
        btnCancel.addActionListener(e -> cardLayout.show(container, "carlist"));
    }

    private void updateTotalCost() {
        LocalDate start = ((Date) spinnerStartDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) spinnerEndDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days < 1) days = 1;

        double base     = selectedCar.getRentalPrice() * days;
        double services = 0;
        if (chkGPS.isSelected())       services += GPS_RATE * days;
        if (chkChildSeat.isSelected()) services += CHILD_SEAT_RATE * days;
        if (chkInsurance.isSelected()) services += INSURANCE_RATE * days;

        double total = base + services;
        lblTotalCost.setText(String.format("$%.2f", total));
    }

    private void doConfirmBooking() {
        // Check availability
        if (!CarController.isCarAvailable(selectedCar.getId())) {
            JOptionPane.showMessageDialog(this,
                "Sorry, this car is no longer available.",
                "Unavailable", JOptionPane.WARNING_MESSAGE);
            cardLayout.show(container, "carlist");
            return;
        }

        LocalDate start = ((Date) spinnerStartDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = ((Date) spinnerEndDate.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        String startStr = start.toString();
        String endStr   = end.toString();
        double totalCost= Double.parseDouble(lblTotalCost.getText().replace("$", ""));

        boolean success = BookingController.createBooking(
                currentUserId,
                selectedCar.getId(),
                startStr,
                endStr,
                0.0,          // deposit
                totalCost,
                "self",       // drive option
                0,            // initial odometer
                startStr      // date out
        );
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Booking confirmed!\nTotal: " + lblTotalCost.getText(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(container, "carlist");
        } else {
            JOptionPane.showMessageDialog(this,
                "Booking failed. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
