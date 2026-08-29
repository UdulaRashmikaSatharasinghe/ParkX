import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
                    "dd MMM yyyy - hh:mm a"
            );

    public Dashboard() {

        manager = new ParkingManager();

        setTitle("ParkX - Parking Management System");

        setSize(
                1200,
                760
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setResizable(false);

        getContentPane().setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND
        );

        createHeader();
        createVehiclePanel();
        createSearchSection();
        createTable();
        createStatistics();
        createNavigationButtons();
        createLiveClock();

        refreshAvailableSlots();
        refreshTable();
        updateStatistics();

        Timer feeTimer =
                new Timer(
                        60000,
                        e -> refreshTable()
                );

        feeTimer.start();

        setVisible(true);
    }


    /*
     * =========================================================
     * HEADER
     * =========================================================
     */

    private void createHeader() {

        JPanel header =
                new JPanel();

        header.setLayout(null);

        header.setBackground(
                ParkXTheme.HEADER
        );

        header.setBounds(
                0,
                0,
                1200,
                100
        );

        getContentPane().add(
                header
        );


        JLabel logo =
                new JLabel(
                        "ParkX"
                );

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        31
                )
        );

        logo.setForeground(
                ParkXTheme.AMBER
        );

        logo.setBounds(
                35,
                15,
                180,
                40
        );

        header.add(
                logo
        );


        JLabel subtitle =
                new JLabel(
                        "Parking Management System"
                );

        subtitle.setForeground(
                ParkXTheme.MUTED
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setBounds(
                35,
                55,
                260,
                25
        );

        header.add(
                subtitle
        );


        lblDateTime =
                new JLabel();

        lblDateTime.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        lblDateTime.setForeground(
                ParkXTheme.TEXT
        );

        lblDateTime.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblDateTime.setBounds(
                690,
                28,
                300,
                34
        );

        header.add(
                lblDateTime
        );


        JButton btnLogout =
                new JButton(
                        "Logout"
                );

        btnLogout.setBounds(
                1035,
                27,
                100,
                38
        );

        btnLogout.setBackground(
                ParkXTheme.CARD_LIGHT
        );

        btnLogout.setForeground(
                ParkXTheme.TEXT
        );

        btnLogout.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);

        btnLogout.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        header.add(
                btnLogout
        );

        btnLogout.addActionListener(
                e -> logout()
        );
    }


    /*
     * =========================================================
     * VEHICLE REGISTRATION PANEL
     * =========================================================
     */

    private void createVehiclePanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(null);

        panel.setBackground(
                ParkXTheme.CARD
        );

        panel.setBounds(
                30,
                135,
                350,
                480
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER
                )
        );

        getContentPane().add(
                panel
        );


        JLabel title =
                new JLabel(
                        "Vehicle Registration"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        title.setBounds(
                25,
                20,
                280,
                30
        );

        panel.add(
                title
        );


        JLabel lblType =
                new JLabel(
                        "Vehicle Type"
                );

        lblType.setForeground(
                ParkXTheme.TEXT
        );

        lblType.setBounds(
                25,
                85,
                120,
                30
        );

        panel.add(
                lblType
        );


        cmbType =
                new JComboBox<>(
                        new String[]{
                                "Car",
                                "Motorcycle",
                                "Van",
                                "Tricycle"
                        }
                );

        cmbType.setBounds(
                145,
                85,
                175,
                36
        );

        cmbType.setBackground(
                ParkXTheme.INPUT
        );

        cmbType.setForeground(
                ParkXTheme.TEXT
        );

        cmbType.setFocusable(false);

        panel.add(
                cmbType
        );


        JLabel lblNumber =
                new JLabel(
                        "Vehicle No."
                );

        lblNumber.setForeground(
                ParkXTheme.TEXT
        );

        lblNumber.setBounds(
                25,
                150,
                120,
                30
        );

        panel.add(
                lblNumber
        );


        txtVehicleNo =
                new JTextField();

        txtVehicleNo.setBounds(
                145,
                150,
                175,
                36
        );

        styleField(
                txtVehicleNo
        );

        panel.add(
                txtVehicleNo
        );


        JLabel lblOwner =
                new JLabel(
                        "Owner Name"
                );

        lblOwner.setForeground(
                ParkXTheme.TEXT
        );

        lblOwner.setBounds(
                25,
                215,
                120,
                30
        );

        panel.add(
                lblOwner
        );


        txtOwner =
                new JTextField();

        txtOwner.setBounds(
                145,
                215,
                175,
                36
        );

        styleField(
                txtOwner
        );

        panel.add(
                txtOwner
        );


        JLabel lblSlot =
                new JLabel(
                        "Parking Slot"
                );

        lblSlot.setForeground(
                ParkXTheme.TEXT
        );

        lblSlot.setBounds(
                25,
                280,
                120,
                30
        );

        panel.add(
                lblSlot
        );


        cmbSlot =
                new JComboBox<>();

        cmbSlot.setBounds(
                145,
                280,
                175,
                36
        );

        cmbSlot.setBackground(
                ParkXTheme.INPUT
        );

        cmbSlot.setForeground(
                ParkXTheme.TEXT
        );

        cmbSlot.setFocusable(false);

        panel.add(
                cmbSlot
        );


        JLabel info =
                new JLabel(
                        "Entry time is recorded automatically"
                );

        info.setFont(
                new Font(
                        "SansSerif",
                        Font.ITALIC,
                        11
                )
        );

        info.setForeground(
                ParkXTheme.MUTED
        );

        info.setBounds(
                25,
                325,
                300,
                25
        );

        panel.add(
                info
        );


        JButton btnPark =
                new JButton(
                        "Park Vehicle"
                );

        btnPark.setBounds(
                25,
                365,
                295,
                42
        );

        btnPark.setBackground(
                ParkXTheme.BLUE
        );

        btnPark.setForeground(
                Color.WHITE
        );

        btnPark.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        btnPark.setFocusPainted(false);
        btnPark.setBorderPainted(false);

        btnPark.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        panel.add(
                btnPark
        );


        JButton btnRemove =
                new JButton(
                        "Remove"
                );

        btnRemove.setBounds(
                25,
                420,
                140,
                38
        );

        btnRemove.setBackground(
                ParkXTheme.RED
        );

        btnRemove.setForeground(
                Color.WHITE
        );

        btnRemove.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnRemove.setFocusPainted(false);
        btnRemove.setBorderPainted(false);

        btnRemove.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        panel.add(
                btnRemove
        );


        JButton btnReceipt =
                new JButton(
                        "Receipt"
                );

        btnReceipt.setBounds(
                180,
                420,
                140,
                38
        );

        btnReceipt.setBackground(
                ParkXTheme.AMBER
        );

        btnReceipt.setForeground(
                new Color(255, 255, 255)
        );

        btnReceipt.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnReceipt.setFocusPainted(false);
        btnReceipt.setBorderPainted(false);

        btnReceipt.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        panel.add(
                btnReceipt
        );


        btnPark.addActionListener(
                e -> parkVehicle()
        );

        btnRemove.addActionListener(
                e -> removeVehicle()
        );

        btnReceipt.addActionListener(
                e -> generateReceipt()
        );
    }


    /*
     * =========================================================
     * SEARCH SECTION
     * =========================================================
     */

    private void createSearchSection() {

        JLabel lblSearch =
                new JLabel(
                        "Vehicle Search"
                );

        lblSearch.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        lblSearch.setForeground(
                ParkXTheme.TEXT
        );

        lblSearch.setBounds(
                420,
                135,
                125,
                35
        );

        getContentPane().add(
                lblSearch
        );


        txtSearch =
                new JTextField();

        txtSearch.setBounds(
                545,
                135,
                250,
                36
        );

        styleField(
                txtSearch
        );

        getContentPane().add(
                txtSearch
        );


        JButton btnSearch =
                new JButton(
                        "Search"
                );

        btnSearch.setBounds(
                810,
                135,
                100,
                36
        );

        btnSearch.setBackground(
                ParkXTheme.BLUE
        );

        btnSearch.setForeground(
                Color.WHITE
        );

        btnSearch.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);

        btnSearch.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        getContentPane().add(
                btnSearch
        );


        JButton btnShowAll =
                new JButton(
                        "Show All"
                );

        btnShowAll.setBounds(
                920,
                135,
                105,
                36
        );

        btnShowAll.setBackground(
                ParkXTheme.CARD_LIGHT
        );

        btnShowAll.setForeground(
                ParkXTheme.TEXT
        );

        btnShowAll.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnShowAll.setFocusPainted(false);
        btnShowAll.setBorderPainted(false);

        btnShowAll.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        getContentPane().add(
                btnShowAll
        );


        btnSearch.addActionListener(
                e -> searchVehicle()
        );

        btnShowAll.addActionListener(
                e -> {

                    txtSearch.setText("");

                    refreshTable();
                }
        );

        txtSearch.addActionListener(
                e -> searchVehicle()
        );
    }


    /*
     * =========================================================
     * TABLE
     * =========================================================
     */

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
                        0
                ) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };


        table =
                new JTable(
                        model
                );

        table.setRowHeight(
                32
        );

        table.setBackground(
                ParkXTheme.CARD
        );

        table.setForeground(
                ParkXTheme.TEXT
        );

        table.setGridColor(
                ParkXTheme.BORDER
        );

        table.setSelectionBackground(
                ParkXTheme.BLUE_DARK
        );

        table.setSelectionForeground(
                Color.WHITE
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        table.getTableHeader().setBackground(
                ParkXTheme.CARD_LIGHT
        );

        table.getTableHeader().setForeground(
                ParkXTheme.TEXT
        );

        table.getTableHeader().setReorderingAllowed(
                false
        );


        JScrollPane scroll =
                new JScrollPane(
                        table
                );

        scroll.setBounds(
                420,
                190,
                730,
                330
        );

        scroll.getViewport().setBackground(
                ParkXTheme.CARD
        );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER
                )
        );

        getContentPane().add(
                scroll
        );
    }


    /*
     * =========================================================
     * STATISTICS
     * =========================================================
     */

    private void createStatistics() {

        lblAvailable =
                new JLabel();

        lblAvailable.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        lblAvailable.setForeground(
                ParkXTheme.GREEN
        );

        lblAvailable.setBounds(
                420,
                545,
                280,
                35
        );

        getContentPane().add(
                lblAvailable
        );


        lblOccupied =
                new JLabel();

        lblOccupied.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        lblOccupied.setForeground(
                ParkXTheme.RED
        );

        lblOccupied.setBounds(
                700,
                545,
                280,
                35
        );

        getContentPane().add(
                lblOccupied
        );
    }


    /*
     * =========================================================
     * NAVIGATION BUTTONS
     * =========================================================
     */

    private void createNavigationButtons() {

        JButton btnSpaces =
                new JButton(
                        "Parking Spaces"
                );

        btnSpaces.setBounds(
                420,
                600,
                220,
                45
        );

        btnSpaces.setBackground(
                ParkXTheme.PURPLE
        );

        btnSpaces.setForeground(
                Color.WHITE
        );

        btnSpaces.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        btnSpaces.setFocusPainted(false);
        btnSpaces.setBorderPainted(false);

        btnSpaces.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        getContentPane().add(
                btnSpaces
        );


        JButton btnAnalytics =
                new JButton(
                        "Analytics"
                );

        btnAnalytics.setBounds(
                660,
                600,
                220,
                45
        );

        btnAnalytics.setBackground(
                ParkXTheme.BLUE
        );

        btnAnalytics.setForeground(
                Color.WHITE
        );

        btnAnalytics.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        btnAnalytics.setFocusPainted(false);
        btnAnalytics.setBorderPainted(false);

        btnAnalytics.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        getContentPane().add(
                btnAnalytics
        );

        JButton btnHistory = new JButton("History");
        btnHistory.setBounds(900, 600, 220, 45);
        btnHistory.setBackground(ParkXTheme.GREEN);
        btnHistory.setForeground(Color.WHITE);
        btnHistory.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHistory.setFocusPainted(false);
        btnHistory.setBorderPainted(false);
        btnHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getContentPane().add(btnHistory);


        btnSpaces.addActionListener(
                e ->
                        new ParkingSpacesWindow(
                                manager
                        )
        );

        btnAnalytics.addActionListener(
                e ->
                        new AnalyticsWindow(
                                manager
                        )
        );

        btnHistory.addActionListener(e -> new HistoryWindow());
    }


    /*
     * =========================================================
     * CLOCK
     * =========================================================
     */

    private void createLiveClock() {

        updateLiveClock();

        Timer timer =
                new Timer(
                        1000,
                        e -> updateLiveClock()
                );

        timer.start();
    }


    private void updateLiveClock() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy | hh:mm:ss a"
                );

        lblDateTime.setText(
                LocalDateTime.now()
                        .format(
                                formatter
                        )
        );
    }


    /*
     * =========================================================
     * PARK VEHICLE
     * =========================================================
     */

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

            showMessageDialog(
                    "Missing Information",
                    "Please complete all required fields.",
                    DialogType.WARNING
            );

            return;
        }


        if (cmbSlot.getSelectedItem()
                == null) {

            showMessageDialog(
                    "Parking Full",
                    "There are currently no available parking spaces.",
                    DialogType.ERROR
            );

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
                        owner
                );


        if (vehicle == null) {

            showMessageDialog(
                    "Vehicle Error",
                    "Unable to create this vehicle.",
                    DialogType.ERROR
            );

            return;
        }


        String slot =
                cmbSlot
                        .getSelectedItem()
                        .toString();


        boolean success =
                manager.parkVehicle(
                        vehicle,
                        slot
                );


        if (success) {

            String message =
                    "<html>"
                            + "<div style='text-align:center;'>"
                            + "Vehicle <b>"
                            + number
                            + "</b> has been parked successfully."
                            + "<br><br>"
                            + "Parking Slot: <b>"
                            + slot
                            + "</b>"
                            + "<br>"
                            + "Entry Time: <b>"
                            + vehicle.getEntryTime()
                            .format(
                                    entryFormatter
                            )
                            + "</b>"
                            + "</div>"
                            + "</html>";


            showMessageDialog(
                    "Vehicle Parked",
                    message,
                    DialogType.SUCCESS
            );


            clearFields();
            refreshAvailableSlots();
            refreshTable();
            updateStatistics();

        } else {

            showMessageDialog(
                    "Parking Failed",
                    "<html>"
                            + "<div style='text-align:center;'>"
                            + "Unable to park this vehicle."
                            + "<br><br>"
                            + "The vehicle may already be parked"
                            + "<br>"
                            + "or the selected slot is occupied."
                            + "</div>"
                            + "</html>",
                    DialogType.ERROR
            );
        }
    }


    /*
     * =========================================================
     * REMOVE VEHICLE - FIXED
     * =========================================================
     */

    private void removeVehicle() {

        int row =
                table.getSelectedRow();


        if (row == -1) {

            showMessageDialog(
                    "No Vehicle Selected",
                    "Please select a vehicle from the table first.",
                    DialogType.WARNING
            );

            return;
        }


        String number =
                table.getValueAt(
                        row,
                        1
                ).toString();


        Vehicle vehicle =
                manager.searchVehicle(
                        number
                );


        if (vehicle == null) {

            showMessageDialog(
                    "Vehicle Not Found",
                    "The selected vehicle could not be found.",
                    DialogType.ERROR
            );

            return;
        }


        String message =
                "<html>"
                        + "<div style='text-align:center;'>"
                        + "Vehicle: <b>"
                        + number
                        + "</b>"
                        + "<br><br>"
                        + "Current Fee: <b>Rs. "
                        + String.format(
                                "%.2f",
                                vehicle.calculateFee()
                        )
                        + "</b>"
                        + "<br><br>"
                        + "Confirm this vehicle exit?"
                        + "</div>"
                        + "</html>";


        boolean confirmed =
                showConfirmDialog(
                        "Vehicle Exit",
                        message,
                        "CONFIRM EXIT",
                        "CANCEL"
                );


        if (!confirmed) {
            return;
        }


        /*
         * Open receipt first.
         */
        BillWindow billWindow =
                new BillWindow(
                        vehicle
                );


        /*
         * Vehicle is removed ONLY after
         * the receipt window is closed.
         */
        billWindow.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosed(
                            WindowEvent e) {

                        Vehicle removed =
                                manager.removeVehicle(
                                        number
                                );


                        if (removed == null) {

                            showMessageDialog(
                                    "Database Error",
                                    "Vehicle could not be removed from the database.",
                                    DialogType.ERROR
                            );

                            return;
                        }


                        refreshAvailableSlots();
                        refreshTable();
                        updateStatistics();


                        showMessageDialog(
                                "Vehicle Removed",
                                "<html>"
                                        + "<div style='text-align:center;'>"
                                        + "Vehicle <b>"
                                        + number
                                        + "</b> exited successfully."
                                        + "<br><br>"
                                        + "The parking space is now available."
                                        + "</div>"
                                        + "</html>",
                                DialogType.SUCCESS
                        );
                    }
                }
        );
    }


    /*
     * =========================================================
     * RECEIPT
     * =========================================================
     */

    private void generateReceipt() {

        int row =
                table.getSelectedRow();


        if (row == -1) {

            showMessageDialog(
                    "No Vehicle Selected",
                    "Please select a vehicle before generating a receipt.",
                    DialogType.WARNING
            );

            return;
        }


        String number =
                table.getValueAt(
                        row,
                        1
                ).toString();


        Vehicle vehicle =
                manager.searchVehicle(
                        number
                );


        if (vehicle != null) {

            new BillWindow(
                    vehicle
            );

        } else {

            showMessageDialog(
                    "Receipt Error",
                    "Unable to find the selected vehicle.",
                    DialogType.ERROR
            );
        }
    }


    /*
     * =========================================================
     * SEARCH
     * =========================================================
     */

    private void searchVehicle() {

        String number =
                txtSearch
                        .getText()
                        .trim()
                        .toUpperCase();


        if (number.isEmpty()) {

            showMessageDialog(
                    "Search Required",
                    "Please enter a vehicle number to search.",
                    DialogType.WARNING
            );

            return;
        }


        Vehicle vehicle =
                manager.searchVehicle(
                        number
                );


        if (vehicle == null) {

            showMessageDialog(
                    "Vehicle Not Found",
                    "<html>"
                            + "<div style='text-align:center;'>"
                            + "No parked vehicle was found"
                            + "<br>"
                            + "with number <b>"
                            + number
                            + "</b>."
                            + "</div>"
                            + "</html>",
                    DialogType.ERROR
            );

            return;
        }


        model.setRowCount(
                0
        );

        addVehicleToTable(
                vehicle
        );
    }


    /*
     * =========================================================
     * REFRESH
     * =========================================================
     */

    private void refreshTable() {

        model.setRowCount(
                0
        );


        for (Vehicle vehicle :
                manager.getVehicles()) {

            addVehicleToTable(
                    vehicle
            );
        }
    }


    private void addVehicleToTable(
            Vehicle vehicle) {

        model.addRow(
                new Object[]{

                        manager.getVehicleSlot(
                                vehicle.getVehicleNumber()
                        ),

                        vehicle.getVehicleNumber(),

                        vehicle.getType(),

                        vehicle.getOwnerName(),

                        vehicle.getEntryTime()
                                .format(
                                        entryFormatter
                                ),

                        "Rs. "
                                + String.format(
                                        "%.2f",
                                        vehicle.calculateFee()
                                )
                }
        );
    }


    private void refreshAvailableSlots() {

        cmbSlot.removeAllItems();


        for (ParkingSpace space :
                manager.getParkingSpaces()) {

            if (!space.isOccupied()) {

                cmbSlot.addItem(
                        space.getSlotId()
                );
            }
        }
    }


    private void updateStatistics() {

        lblAvailable.setText(
                "Available Spaces: "
                        + manager.getAvailableSlots()
                        + " / 80"
        );


        lblOccupied.setText(
                "Occupied Spaces: "
                        + manager.getOccupiedSlots()
                        + " / 80"
        );
    }


    /*
     * =========================================================
     * STYLE
     * =========================================================
     */

    private void styleField(
            JTextField field) {

        field.setBackground(
                ParkXTheme.INPUT
        );

        field.setForeground(
                ParkXTheme.TEXT
        );

        field.setCaretColor(
                ParkXTheme.TEXT
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                ParkXTheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );
    }


    private void clearFields() {

        txtVehicleNo.setText("");

        txtOwner.setText("");

        cmbType.setSelectedIndex(
                0
        );

        txtVehicleNo.requestFocus();
    }


    /*
     * =========================================================
     * LOGOUT
     * =========================================================
     */

    private void logout() {

        boolean confirmed =
                showConfirmDialog(
                        "Logout",
                        "<html>"
                                + "<div style='text-align:center;'>"
                                + "Are you sure you want to logout"
                                + "<br>"
                                + "from the ParkX system?"
                                + "</div>"
                                + "</html>",
                        "LOGOUT",
                        "CANCEL"
                );


        if (confirmed) {

            dispose();

            new LoginForm();
        }
    }


    /*
     * =========================================================
     * DIALOG TYPE
     * =========================================================
     */

    private enum DialogType {

        SUCCESS,
        ERROR,
        WARNING,
        INFO
    }


    /*
     * =========================================================
     * CUSTOM MESSAGE DIALOG
     * =========================================================
     */

    private void showMessageDialog(
            String titleText,
            String messageText,
            DialogType type) {

        JDialog dialog =
                new JDialog(
                        this,
                        "ParkX",
                        true
                );


        dialog.setSize(
                430,
                300
        );

        dialog.setResizable(false);

        dialog.setLocationRelativeTo(
                this
        );

        dialog.getContentPane().setLayout(null);

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.getContentPane()
                .setBackground(
                        ParkXTheme.CARD
                );


        Color accentColor;

        String iconText;


        switch (type) {

            case SUCCESS:

                accentColor =
                        ParkXTheme.GREEN;

                iconText =
                        "✓";

                break;


            case ERROR:

                accentColor =
                        ParkXTheme.RED;

                iconText =
                        "!";

                break;


            case WARNING:

                accentColor =
                        ParkXTheme.AMBER;

                iconText =
                        "!";

                break;


            default:

                accentColor =
                        ParkXTheme.BLUE;

                iconText =
                        "i";

                break;
        }


        JPanel topBar =
                new JPanel();

        topBar.setBackground(
                accentColor
        );

        topBar.setBounds(
                0,
                0,
                430,
                6
        );

        dialog.getContentPane().add(
                topBar
        );


        JPanel iconPanel =
                createCircleIcon(
                        iconText,
                        accentColor
                );

        iconPanel.setBounds(
                175,
                25,
                70,
                70
        );

        dialog.getContentPane().add(
                iconPanel
        );


        JLabel title =
                new JLabel(
                        titleText,
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        title.setBounds(
                40,
                105,
                350,
                30
        );

        dialog.getContentPane().add(
                title
        );


        JLabel message =
                new JLabel(
                        messageText,
                        SwingConstants.CENTER
                );

        message.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        message.setForeground(
                ParkXTheme.MUTED
        );

        message.setBounds(
                30,
                140,
                370,
                70
        );

        dialog.getContentPane().add(
                message
        );


        JButton btnOk =
                new JButton(
                        "OK"
                );

        btnOk.setBounds(
                135,
                220,
                160,
                40
        );

        btnOk.setBackground(
                accentColor
        );

        btnOk.setForeground(
                type == DialogType.WARNING
                        ? new Color(
                                20,
                                30,
                                45
                        )
                        : Color.WHITE
        );

        btnOk.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnOk.setFocusPainted(false);
        btnOk.setBorderPainted(false);

        btnOk.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        dialog.getContentPane().add(
                btnOk
        );


        btnOk.addActionListener(
                e -> dialog.dispose()
        );


        dialog.getRootPane()
                .setDefaultButton(
                        btnOk
                );


        dialog.setVisible(
                true
        );
    }


    /*
     * =========================================================
     * CUSTOM CONFIRM DIALOG
     * =========================================================
     */

    private boolean showConfirmDialog(
            String titleText,
            String messageText,
            String confirmText,
            String cancelText) {

        final boolean[] confirmed =
                {
                        false
                };


        JDialog dialog =
                new JDialog(
                        this,
                        "ParkX",
                        true
                );


        dialog.setSize(
                450,
                320
        );

        dialog.setResizable(false);

        dialog.setLocationRelativeTo(
                this
        );

        dialog.getContentPane().setLayout(null);

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.getContentPane()
                .setBackground(
                        ParkXTheme.CARD
                );


        JPanel topBar =
                new JPanel();

        topBar.setBackground(
                ParkXTheme.AMBER
        );

        topBar.setBounds(
                0,
                0,
                450,
                6
        );

        dialog.getContentPane().add(
                topBar
        );


        JPanel iconPanel =
                createCircleIcon(
                        "?",
                        ParkXTheme.AMBER
                );

        iconPanel.setBounds(
                190,
                25,
                70,
                70
        );

        dialog.getContentPane().add(
                iconPanel
        );


        JLabel title =
                new JLabel(
                        titleText,
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        title.setBounds(
                40,
                105,
                370,
                30
        );

        dialog.getContentPane().add(
                title
        );


        JLabel message =
                new JLabel(
                        messageText,
                        SwingConstants.CENTER
                );

        message.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        message.setForeground(
                ParkXTheme.MUTED
        );

        message.setBounds(
                30,
                140,
                390,
                80
        );

        dialog.getContentPane().add(
                message
        );


        JButton btnConfirm =
                new JButton(
                        confirmText
                );

        btnConfirm.setBounds(
                55,
                235,
                160,
                40
        );

        btnConfirm.setBackground(
                ParkXTheme.RED
        );

        btnConfirm.setForeground(
                Color.WHITE
        );

        btnConfirm.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorderPainted(false);

        btnConfirm.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        dialog.getContentPane().add(
                btnConfirm
        );


        JButton btnCancel =
                new JButton(
                        cancelText
                );

        btnCancel.setBounds(
                235,
                235,
                160,
                40
        );

        btnCancel.setBackground(
                ParkXTheme.CARD_LIGHT
        );

        btnCancel.setForeground(
                ParkXTheme.TEXT
        );

        btnCancel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);

        btnCancel.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        dialog.getContentPane().add(
                btnCancel
        );


        btnConfirm.addActionListener(
                e -> {

                    confirmed[0] =
                            true;

                    dialog.dispose();
                }
        );


        btnCancel.addActionListener(
                e -> {

                    confirmed[0] =
                            false;

                    dialog.dispose();
                }
        );


        dialog.setVisible(
                true
        );


        return confirmed[0];
    }


    /*
     * =========================================================
     * CIRCLE ICON
     * =========================================================
     */

    private JPanel createCircleIcon(
            String text,
            Color color) {

        JPanel panel =
                new JPanel() {

                    private static final long serialVersionUID = 1L;


                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        super.paintComponent(
                                g
                        );


                        Graphics2D g2 =
                                (Graphics2D) g.create();


                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );


                        g2.setColor(
                                new Color(
                                        color.getRed(),
                                        color.getGreen(),
                                        color.getBlue(),
                                        35
                                )
                        );


                        g2.fillOval(
                                0,
                                0,
                                getWidth(),
                                getHeight()
                        );


                        g2.setColor(
                                color
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        3f
                                )
                        );


                        g2.drawOval(
                                4,
                                4,
                                getWidth() - 8,
                                getHeight() - 8
                        );


                        g2.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        30
                                )
                        );


                        FontMetrics fm =
                                g2.getFontMetrics();


                        int x =
                                (
                                        getWidth()
                                                - fm.stringWidth(
                                                text
                                        )
                                ) / 2;


                        int y =
                                (
                                        (
                                                getHeight()
                                                        - fm.getHeight()
                                        ) / 2
                                )
                                        + fm.getAscent();


                        g2.drawString(
                                text,
                                x,
                                y
                        );


                        g2.dispose();
                    }
                };


        panel.setOpaque(
                false
        );


        return panel;
    }
}
