package view;

import controller.BookingController;
import util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationManagementPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel container;
    private final boolean isAdmin;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton updateButton, cancelButton, backButton;
    private JSpinner startDateSpinner, endDateSpinner;

    public ReservationManagementPanel(CardLayout cardLayout, JPanel container, boolean isAdmin) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.isAdmin = isAdmin;

        setLayout(new BorderLayout(10, 10));
        initComponents();
        loadReservations();
    }

    private void initComponents() {
        String[] columns;
        if (isAdmin) {
            columns = new String[]{"Booking ID", "Customer", "Car", "Start Date", "End Date", "Status", "Amount"};
        } else {
            columns = new String[]{"Booking ID", "Car", "Start Date", "End Date", "Status", "Amount"};
        }
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(new JLabel("New Start Date:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        datePanel.add(startDateSpinner);
        datePanel.add(new JLabel("New End Date:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        datePanel.add(endDateSpinner);
        add(datePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        updateButton = new JButton("Update Dates");
        cancelButton = new JButton("Cancel Booking");
        backButton = new JButton("Back");
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> onUpdateBooking());
        cancelButton.addActionListener(e -> onCancelBooking());
        backButton.addActionListener(e -> {
            cardLayout.show(container, isAdmin ? "admin" : "carlist");
        });
    }

    private void loadReservations() {
        List<String> allBookings = isAdmin
                ? BookingController.getAllBookings()
                : BookingController.getBookingsByUser(Session.getCurrentUserId());

        List<String[]> active = new ArrayList<>();
        List<String[]> cancelled = new ArrayList<>();

        for (String booking : allBookings) {
            try {
                boolean multiline = booking.contains("\n");
                int bookingId;
                String user = "";
                String car, start, end, status, amount;

                if (multiline) {
                    String[] lines = booking.split("\\n");
                    bookingId = Integer.parseInt(lines[0].split("#")[1].split("\\|")[0].trim());
                    user      = lines[0].split("User:")[1].split("\\|")[0].trim();
                    car       = lines[0].split("Car:")[1].trim();
                    String[] parts = lines[1].split("\\|");
                    start     = parts[0].replace("Start:", "").trim();
                    end       = parts[1].replace("End:", "").trim();
                    status    = parts[2].replace("Status:", "").trim();
                    amount    = parts[4].replace("Amount:", "").trim();
                } else {
                    bookingId = Integer.parseInt(booking.split("#")[1].split(":")[0].trim());
                    car       = booking.split("\\(")[1].split("\\)")[0];
                    String dates = booking.split(",")[1].trim(); // "2025-05-10 to 2025-05-10 - cancelled [$500,00]"
                    String[] dateStatus = dates.split(" - ");
                    String[] de       = dateStatus[0].split(" to ");
                    start     = de[0].trim();
                    end       = de[1].trim();
                    status    = dateStatus[1].split("\\[")[0].trim();
                    amount    = dateStatus[1].split("\\[")[1].replace("]", "").trim();
                }

                String[] row;
                if (isAdmin) {
                    row = new String[]{
                            String.valueOf(bookingId),
                            user,
                            car,
                            start,
                            end,
                            status,
                            amount
                    };
                } else {
                    row = new String[]{
                            String.valueOf(bookingId),
                            car,
                            start,
                            end,
                            status,
                            amount
                    };
                }

                if ("cancelled".equalsIgnoreCase(status)) cancelled.add(row);
                else                                   active.add(row);
            } catch (Exception ex) {
                System.err.println("Parsing failed:\n" + booking);
            }
        }

        // === Tabloya yaz ===
        tableModel.setRowCount(0);
        if (isAdmin) {
            tableModel.addRow(new String[]{"--- ACTIVE BOOKINGS ---","","","","","",""});
        }
        active.forEach(r -> tableModel.addRow(r));

        if (isAdmin && !cancelled.isEmpty()) {
            tableModel.addRow(new String[]{"--- CANCELLED BOOKINGS ---","","","","","",""});
        }
        cancelled.forEach(r -> tableModel.addRow(r));
    }

    private void onUpdateBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking.");
            return;
        }

        Object val = tableModel.getValueAt(row, 0);
        if (!(val instanceof String) || !((String) val).matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please select a valid booking row.");
            return;
        }
        int bookingId = Integer.parseInt((String) val);

        LocalDate newStart = convertToLocalDate((Date) startDateSpinner.getValue());
        LocalDate newEnd   = convertToLocalDate((Date) endDateSpinner.getValue());
        if (!newEnd.isAfter(newStart)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date.");
            return;
        }

        boolean success = BookingController.updateBookingDatesAndAmount(
                bookingId,
                newStart.toString(),
                newEnd.toString()
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Booking dates and amount updated.");
            loadReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update booking.");
        }
    }

    private void onCancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row<0) { JOptionPane.showMessageDialog(this,"Select a booking."); return; }

        Object val = tableModel.getValueAt(row, 0);
        if (!(val instanceof String) || !((String)val).matches("\\d+")) {
            JOptionPane.showMessageDialog(this,"Select a valid booking row."); return;
        }
        int bookingId = Integer.parseInt((String)val);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel booking #"+bookingId+"?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm!=JOptionPane.YES_OPTION) return;

        boolean ok = BookingController.cancelBooking(bookingId);
        JOptionPane.showMessageDialog(this,
                ok ? "Booking cancelled." : "Cancellation failed."
        );
        // panel kapanıp admin/carlist’e dönüyor
        cardLayout.show(container, isAdmin?"admin":"carlist");
        loadReservations();
    }

    private LocalDate convertToLocalDate(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
