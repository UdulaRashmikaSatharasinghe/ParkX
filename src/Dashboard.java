import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private ParkingManager manager;

    private JComboBox<String> cmbType;
    private JComboBox<String> cmbSlot;

    private JTextField txtVehicleNo;
    private JTextField txtOwner;
    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblAvailable;
    private JLabel lblOccupied;
    private JLabel lblDateTime;

    private final DateTimeFormatter entryFormatter =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy - hh:mm a");

    public Dashboard() {

        manager = new ParkingManager();

        setTitle("ParkX - Parking Management System");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND);

        createHeader();
        createVehiclePanel();
        createSearchSection();
        createTable();
        createStatistics();
        createNavigationButtons();
        createLiveClock();

        refreshAvailableSlots();
        refreshTable();

        Timer feeTimer =
                new Timer(
                        60000,
                        e -> refreshTable());

        feeTimer.start();

        setVisible(true);
    }

    private void createHeader() {

        JPanel header =
                new JPanel();

        header.setLayout(null);
        header.setBackground(
                ParkXTheme.HEADER);

        header.setBounds(
                0,
                0,
                1200,
                100);

        getContentPane().add(header);


        JLabel logo =
                new JLabel("PARKX");

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        31));

        logo.setForeground(
                ParkXTheme.AMBER);

        logo.setBounds(
                35,
                15,
                180,
                40);

        header.add(logo);


        JLabel subtitle =
                new JLabel(
                        "Parking Management System");

        subtitle.setForeground(
                ParkXTheme.MUTED);

        subtitle.setBounds(
                35,
                55,
                260,
                25);

        header.add(subtitle);


        lblDateTime =
                new JLabel();

        lblDateTime.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13));

        lblDateTime.setForeground(
                ParkXTheme.TEXT);

        lblDateTime.setHorizontalAlignment(
                SwingConstants.RIGHT);

        lblDateTime.setBounds(
                690,
                28,
                300,
                34);

        header.add(lblDateTime);


        JButton btnLogout =
                new JButton("Logout");

        btnLogout.setBounds(
                1035,
                27,
                100,
                38);

        btnLogout.setBackground(
                ParkXTheme.CARD_LIGHT);

        btnLogout.setForeground(
                ParkXTheme.TEXT);

        header.add(btnLogout);

        btnLogout.addActionListener(
                e -> logout());
    }

    private void createVehiclePanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(null);
        panel.setBackground(
                ParkXTheme.CARD);

        panel.setBounds(
                30,
                135,
                350,
                480);

        panel.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(panel);


        JLabel title =
                new JLabel(
                        "Vehicle Registration");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20));

        title.setForeground(
                ParkXTheme.TEXT);

        title.setBounds(
                25,
                20,
                280,
                30);

        panel.add(title);


        JLabel lblType =
                new JLabel(
                        "Vehicle Type");

        lblType.setForeground(
                ParkXTheme.TEXT);

        lblType.setBounds(
                25,
                85,
                120,
                30);

        panel.add(lblType);


        cmbType =
                new JComboBox<>(
                        new String[] {
                                "Car",
                                "Motorcycle",
                                "Van",
                                "Tricycle"
                        });

        cmbType.setBounds(
                145,
                85,
                175,
                36);

        cmbType.setBackground(
                ParkXTheme.INPUT);

        cmbType.setForeground(
                ParkXTheme.TEXT);

        panel.add(cmbType);


        JLabel lblNumber =
                new JLabel("Vehicle No.");

        lblNumber.setForeground(
                ParkXTheme.TEXT);

        lblNumber.setBounds(
                25,
                150,
                120,
                30);

        panel.add(lblNumber);


        txtVehicleNo =
                new JTextField();

        txtVehicleNo.setBounds(
                145,
                150,
                175,
                36);

        styleField(txtVehicleNo);

        panel.add(txtVehicleNo);


        JLabel lblOwner =
                new JLabel("Owner Name");

        lblOwner.setForeground(
                ParkXTheme.TEXT);

        lblOwner.setBounds(
                25,
                215,
                120,
                30);

        panel.add(lblOwner);


        txtOwner =
                new JTextField();

        txtOwner.setBounds(
                145,
                215,
                175,
                36);

        styleField(txtOwner);

        panel.add(txtOwner);


        JLabel lblSlot =
                new JLabel("Parking Slot");

        lblSlot.setForeground(
                ParkXTheme.TEXT);

        lblSlot.setBounds(
                25,
                280,
                120,
                30);

        panel.add(lblSlot);


        cmbSlot =
                new JComboBox<>();

        cmbSlot.setBounds(
                145,
                280,
                175,
                36);

        cmbSlot.setBackground(
                ParkXTheme.INPUT);

        cmbSlot.setForeground(
                ParkXTheme.TEXT);

        panel.add(cmbSlot);


        JLabel info =
                new JLabel(
                        "Entry time is recorded automatically");

        info.setFont(
                new Font(
                        "SansSerif",
                        Font.ITALIC,
                        11));

        info.setForeground(
                ParkXTheme.MUTED);

        info.setBounds(
                25,
                325,
                300,
                25);

        panel.add(info);


        JButton btnPark =
                new JButton(
                        "Park Vehicle");

        btnPark.setBounds(
                25,
                365,
                295,
                42);

        btnPark.setBackground(
                ParkXTheme.BLUE);

        btnPark.setForeground(
                Color.WHITE);

        panel.add(btnPark);


        JButton btnRemove =
                new JButton("Remove");

        btnRemove.setBounds(
                25,
                420,
                140,
                38);

        btnRemove.setBackground(
                ParkXTheme.RED);

        btnRemove.setForeground(
                Color.WHITE);

        panel.add(btnRemove);


        JButton btnReceipt =
                new JButton("Receipt");

        btnReceipt.setBounds(
                180,
                420,
                140,
                38);

        btnReceipt.setBackground(
                ParkXTheme.AMBER);

        btnReceipt.setForeground(
                new Color(20, 30, 45));

        panel.add(btnReceipt);


        btnPark.addActionListener(
                e -> parkVehicle());

        btnRemove.addActionListener(
                e -> removeVehicle());

        btnReceipt.addActionListener(
                e -> generateReceipt());
    }

    private void createSearchSection() {

        JLabel lblSearch =
                new JLabel("Vehicle Search");

        lblSearch.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15));

        lblSearch.setForeground(
                ParkXTheme.TEXT);

        lblSearch.setBounds(
                420,
                135,
                125,
                35);

        getContentPane().add(lblSearch);


        txtSearch =
                new JTextField();

        txtSearch.setBounds(
                545,
                135,
                250,
                36);

        styleField(txtSearch);

        getContentPane().add(txtSearch);


        JButton btnSearch =
                new JButton("Search");

        btnSearch.setBounds(
                810,
                135,
                100,
                36);

        btnSearch.setBackground(
                ParkXTheme.BLUE);

        btnSearch.setForeground(
                Color.WHITE);

        getContentPane().add(btnSearch);


        JButton btnShowAll =
                new JButton("Show All");

        btnShowAll.setBounds(
                920,
                135,
                105,
                36);

        btnShowAll.setBackground(
                ParkXTheme.CARD_LIGHT);

        btnShowAll.setForeground(
                ParkXTheme.TEXT);

        getContentPane().add(btnShowAll);


        btnSearch.addActionListener(
                e -> searchVehicle());

        btnShowAll.addActionListener(
                e -> {
                    txtSearch.setText("");
                    refreshTable();
                });
    }

    private void createTable() {

        String[] columns = {
                "Slot",
                "Vehicle No",
                "Type",
                "Owner",
                "Entry Time",
                "Current Fee"
        };

        model =
                new DefaultTableModel(
                        columns,
                        0) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        table = new JTable(model);

        table.setRowHeight(32);
        table.setBackground(
                ParkXTheme.CARD);

        table.setForeground(
                ParkXTheme.TEXT);

        table.setGridColor(
                ParkXTheme.BORDER);

        table.setSelectionBackground(
                ParkXTheme.BLUE_DARK);

        table.setSelectionForeground(
                Color.WHITE);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13));

        table.getTableHeader().setBackground(
                ParkXTheme.CARD_LIGHT);

        table.getTableHeader().setForeground(
                ParkXTheme.TEXT);

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBounds(
                420,
                190,
                730,
                330);

        scroll.getViewport().setBackground(
                ParkXTheme.CARD);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(scroll);
    }

    private void createStatistics() {

        lblAvailable =
                new JLabel();

        lblAvailable.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18));

        lblAvailable.setForeground(
                ParkXTheme.GREEN);

        lblAvailable.setBounds(
                420,
                545,
                280,
                35);

        getContentPane().add(lblAvailable);


        lblOccupied =
                new JLabel();

        lblOccupied.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18));

        lblOccupied.setForeground(
                ParkXTheme.RED);

        lblOccupied.setBounds(
                700,
                545,
                280,
                35);

        getContentPane().add(lblOccupied);

        updateStatistics();
    }

    private void createNavigationButtons() {

        JButton btnSpaces =
                new JButton(
                        "Parking Spaces");

        btnSpaces.setBounds(
                420,
                600,
                220,
                45);

        btnSpaces.setBackground(
                ParkXTheme.PURPLE);

        btnSpaces.setForeground(
                Color.WHITE);

        getContentPane().add(btnSpaces);


        JButton btnAnalytics =
                new JButton("Analytics");

        btnAnalytics.setBounds(
                660,
                600,
                220,
                45);

        btnAnalytics.setBackground(
                ParkXTheme.BLUE);

        btnAnalytics.setForeground(
                Color.WHITE);

        getContentPane().add(btnAnalytics);


        btnSpaces.addActionListener(
                e ->
                        new ParkingSpacesWindow(
                                manager));

        btnAnalytics.addActionListener(
                e ->
                        new AnalyticsWindow(
                                manager));
    }

    private void createLiveClock() {

        updateLiveClock();

        Timer timer =
                new Timer(
                        1000,
                        e -> updateLiveClock());

        timer.start();
    }

    private void updateLiveClock() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy | hh:mm:ss a");

        lblDateTime.setText(
                LocalDateTime.now()
                        .format(formatter));
    }

    private void parkVehicle() {

        String number =
                txtVehicleNo
                        .getText()
                        .trim()
                        .toUpperCase();

        String owner =
                txtOwner
                        .getText()
                        .trim();

        if (number.isEmpty()
                || owner.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all fields.");

            return;
        }

        if (cmbSlot.getSelectedItem()
                == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No parking spaces available.");

            return;
        }

        String type =
                cmbType
                        .getSelectedItem()
                        .toString();

        Vehicle vehicle =
                VehicleFactory.createVehicle(
                        type,
                        number,
                        owner);

        String slot =
                cmbSlot
                        .getSelectedItem()
                        .toString();

        boolean success =
                manager.parkVehicle(
                        vehicle,
                        slot);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle parked successfully!\n\n"
                            + "Vehicle: "
                            + number
                            + "\nParking Slot: "
                            + slot
                            + "\nEntry Time: "
                            + vehicle.getEntryTime()
                                    .format(entryFormatter));

            clearFields();
            refreshAvailableSlots();
            refreshTable();
            updateStatistics();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to park vehicle.\n"
                            + "Vehicle may already be parked "
                            + "or the slot may be occupied.");
        }
    }

    private void removeVehicle() {

        int row =
                table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a vehicle.");

            return;
        }

        String number =
                table.getValueAt(
                        row,
                        1)
                        .toString();

        Vehicle vehicle =
                manager.searchVehicle(
                        number);

        if (vehicle == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle not found.");

            return;
        }

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Vehicle: "
                                + number
                                + "\nCurrent Fee: Rs. "
                                + String.format(
                                        "%.2f",
                                        vehicle.calculateFee())
                                + "\n\nConfirm vehicle exit?",
                        "Vehicle Exit",
                        JOptionPane.YES_NO_OPTION);

        if (answer !=
                JOptionPane.YES_OPTION) {

            return;
        }

        // Show receipt first.
        new BillWindow(vehicle);

        Vehicle removed =
                manager.removeVehicle(
                        number);

        if (removed == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database update failed. Vehicle was not removed.");
            return;
        }

        refreshAvailableSlots();
        refreshTable();
        updateStatistics();
    }

    private void generateReceipt() {

        int row =
                table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a vehicle.");
            return;
        }

        String number =
                table.getValueAt(
                        row,
                        1)
                        .toString();

        Vehicle vehicle =
                manager.searchVehicle(
                        number);

        if (vehicle != null) {
            new BillWindow(vehicle);
        }
    }

    private void searchVehicle() {

        String number =
                txtSearch
                        .getText()
                        .trim();

        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a vehicle number.");
            return;
        }

        Vehicle vehicle =
                manager.searchVehicle(
                        number);

        if (vehicle == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle not found.");
            return;
        }

        model.setRowCount(0);
        addVehicleToTable(vehicle);
    }

    private void refreshTable() {

        model.setRowCount(0);

        for (Vehicle vehicle :
                manager.getVehicles()) {

            addVehicleToTable(vehicle);
        }
    }

    private void addVehicleToTable(
            Vehicle vehicle) {

        model.addRow(
                new Object[] {

                        manager.getVehicleSlot(
                                vehicle.getVehicleNumber()),

                        vehicle.getVehicleNumber(),

                        vehicle.getType(),

                        vehicle.getOwnerName(),

                        vehicle.getEntryTime()
                                .format(entryFormatter),

                        "Rs. "
                                + String.format(
                                        "%.2f",
                                        vehicle.calculateFee())
                });
    }

    private void refreshAvailableSlots() {

        cmbSlot.removeAllItems();

        for (ParkingSpace space :
                manager.getParkingSpaces()) {

            if (!space.isOccupied()) {
                cmbSlot.addItem(
                        space.getSlotId());
            }
        }
    }

    private void updateStatistics() {

        lblAvailable.setText(
                "Available Spaces: "
                        + manager.getAvailableSlots()
                        + " / 80");

        lblOccupied.setText(
                "Occupied Spaces: "
                        + manager.getOccupiedSlots()
                        + " / 80");
    }

    private void styleField(
            JTextField field) {

        field.setBackground(
                ParkXTheme.INPUT);

        field.setForeground(
                ParkXTheme.TEXT);

        field.setCaretColor(
                ParkXTheme.TEXT);

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                ParkXTheme.BORDER),

                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8)
                ));
    }

    private void clearFields() {
        txtVehicleNo.setText("");
        txtOwner.setText("");
        cmbType.setSelectedIndex(0);
        txtVehicleNo.requestFocus();
    }

    private void logout() {

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION);

        if (answer ==
                JOptionPane.YES_OPTION) {

            dispose();
            new LoginForm();
        }
    }
}
