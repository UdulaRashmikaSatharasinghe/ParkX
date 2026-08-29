import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ParkingSpacesWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private ParkingManager manager;
    private JComboBox<String> cmbFloor;
    private JPanel slotsPanel;
    private JLabel lblAvailable;
    private JLabel lblOccupied;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy - hh:mm a"
            );


    /*
     * =========================================================
     * CONSTRUCTOR
     * =========================================================
     */

    public ParkingSpacesWindow(
            ParkingManager manager) {

        this.manager = manager;

        setTitle(
                "ParkX - Parking Spaces"
        );

        setSize(
                1000,
                680
        );

        setLocationRelativeTo(
                null
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(
                false
        );

        setLayout(
                null
        );

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND
        );

        createInterface();

        showFloor(
                "Ground Floor"
        );

        setVisible(
                true
        );
    }


    /*
     * =========================================================
     * CREATE INTERFACE
     * =========================================================
     */

    private void createInterface() {

        /*
         * -----------------------------------------------------
         * TITLE
         * -----------------------------------------------------
         */

        JLabel title =
                new JLabel(
                        "Parking Spaces"
                );

        title.setFont(
                ParkXTheme.titleFont(
                        28
                )
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        title.setBounds(
                40,
                25,
                300,
                40
        );

        add(
                title
        );


        /*
         * -----------------------------------------------------
         * SUBTITLE
         * -----------------------------------------------------
         */

        JLabel subtitle =
                new JLabel(
                        "Live floor occupancy overview"
                );

        subtitle.setForeground(
                ParkXTheme.MUTED
        );

        subtitle.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        subtitle.setBounds(
                40,
                65,
                300,
                25
        );

        add(
                subtitle
        );


        /*
         * -----------------------------------------------------
         * FLOOR LABEL
         * -----------------------------------------------------
         */

        JLabel lblFloor =
                new JLabel(
                        "Select Floor"
                );

        lblFloor.setForeground(
                ParkXTheme.TEXT
        );

        lblFloor.setFont(
                ParkXTheme.boldFont(
                        13
                )
        );

        lblFloor.setBounds(
                40,
                115,
                100,
                30
        );

        add(
                lblFloor
        );


        /*
         * -----------------------------------------------------
         * FLOOR COMBO BOX
         * -----------------------------------------------------
         */

        cmbFloor =
                new JComboBox<>(
                        new String[]{
                                "Ground Floor",
                                "First Floor",
                                "Second Floor",
                                "Third Floor"
                        }
                );

        cmbFloor.setBounds(
                145,
                115,
                190,
                35
        );

        cmbFloor.setBackground(
                ParkXTheme.INPUT
        );

        cmbFloor.setForeground(
                ParkXTheme.TEXT
        );

        cmbFloor.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        cmbFloor.setFocusable(
                false
        );

        add(
                cmbFloor
        );


        /*
         * -----------------------------------------------------
         * AVAILABLE COUNT
         * -----------------------------------------------------
         */

        lblAvailable =
                new JLabel();

        lblAvailable.setFont(
                ParkXTheme.boldFont(
                        16
                )
        );

        lblAvailable.setForeground(
                ParkXTheme.GREEN
        );

        lblAvailable.setBounds(
                580,
                115,
                170,
                35
        );

        add(
                lblAvailable
        );


        /*
         * -----------------------------------------------------
         * OCCUPIED COUNT
         * -----------------------------------------------------
         */

        lblOccupied =
                new JLabel();

        lblOccupied.setFont(
                ParkXTheme.boldFont(
                        16
                )
        );

        lblOccupied.setForeground(
                ParkXTheme.RED
        );

        lblOccupied.setBounds(
                760,
                115,
                170,
                35
        );

        add(
                lblOccupied
        );


        /*
         * -----------------------------------------------------
         * PARKING SLOT PANEL
         * -----------------------------------------------------
         */

        slotsPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                5,
                                15,
                                15
                        )
                );

        slotsPanel.setBackground(
                ParkXTheme.BACKGROUND
        );


        /*
         * -----------------------------------------------------
         * SCROLL PANE
         * -----------------------------------------------------
         */

        JScrollPane scroll =
                new JScrollPane(
                        slotsPanel
                );

        scroll.setBounds(
                40,
                180,
                900,
                400
        );

        scroll.setBorder(
                null
        );

        scroll.getViewport().setBackground(
                ParkXTheme.BACKGROUND
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        add(
                scroll
        );


        /*
         * -----------------------------------------------------
         * FLOOR CHANGE EVENT
         * -----------------------------------------------------
         */

        cmbFloor.addActionListener(
                e -> {

                    Object selected =
                            cmbFloor.getSelectedItem();

                    if (selected != null) {

                        showFloor(
                                selected.toString()
                        );
                    }
                }
        );
    }


    /*
     * =========================================================
     * SHOW FLOOR
     * =========================================================
     */

    private void showFloor(
            String floor) {

        slotsPanel.removeAll();

        int available = 0;
        int occupied = 0;


        for (ParkingSpace space :
                manager.getParkingSpaces()) {


            /*
             * Only show parking spaces
             * belonging to selected floor.
             */

            if (!space.getFloor()
                    .equals(floor)) {

                continue;
            }


            JButton slot =
                    new JButton();

            slot.setFocusPainted(
                    false
            );

            slot.setBorderPainted(
                    false
            );

            slot.setFont(
                    ParkXTheme.boldFont(
                            14
                    )
            );

            slot.setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );


            /*
             * =================================================
             * OCCUPIED SLOT
             * =================================================
             */

            if (space.isOccupied()) {

                occupied++;

                Vehicle vehicle =
                        space.getVehicle();


                slot.setText(
                        "<html>"
                                + "<center>"
                                + "<b>"
                                + space.getSlotId()
                                + "</b>"
                                + "<br><br>"
                                + vehicle.getVehicleNumber()
                                + "<br>"
                                + vehicle.getType()
                                + "<br><br>"
                                + "OCCUPIED"
                                + "</center>"
                                + "</html>"
                );


                slot.setBackground(
                        ParkXTheme.RED_DARK
                );


                slot.setForeground(
                        new Color(
                                255,
                                150,
                                150
                        )
                );


                /*
                 * Click occupied slot
                 * to view vehicle information.
                 */

                slot.addActionListener(
                        e ->
                                showVehicleDetails(
                                        space,
                                        vehicle
                                )
                );

            }


            /*
             * =================================================
             * AVAILABLE SLOT
             * =================================================
             */

            else {

                available++;


                slot.setText(
                        "<html>"
                                + "<center>"
                                + "<b>"
                                + space.getSlotId()
                                + "</b>"
                                + "<br><br>"
                                + "AVAILABLE"
                                + "</center>"
                                + "</html>"
                );


                slot.setBackground(
                        ParkXTheme.GREEN_DARK
                );


                slot.setForeground(
                        new Color(
                                100,
                                240,
                                170
                        )
                );
            }


            slotsPanel.add(
                    slot
            );
        }


        /*
         * -----------------------------------------------------
         * UPDATE COUNTERS
         * -----------------------------------------------------
         */

        lblAvailable.setText(
                "Available: "
                        + available
        );


        lblOccupied.setText(
                "Occupied: "
                        + occupied
        );


        /*
         * -----------------------------------------------------
         * REFRESH SLOT PANEL
         * -----------------------------------------------------
         */

        slotsPanel.revalidate();

        slotsPanel.repaint();
    }


    /*
     * =========================================================
     * SHOW VEHICLE DETAILS
     * =========================================================
     */

    private void showVehicleDetails(
            ParkingSpace space,
            Vehicle vehicle) {


        /*
         * -----------------------------------------------------
         * CREATE DIALOG
         * -----------------------------------------------------
         */

        JDialog dialog =
                new JDialog(
                        this,
                        "ParkX - Parking Space",
                        true
                );


        /*
         * Increased height so CLOSE button
         * is not cropped on macOS / Windows.
         */

        dialog.setSize(
                450,
                440
        );


        dialog.setResizable(
                false
        );


        dialog.setLocationRelativeTo(
                this
        );


        dialog.setLayout(
                null
        );


        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );


        dialog.getContentPane()
                .setBackground(
                        ParkXTheme.CARD
                );


        /*
         * =====================================================
         * TOP BLUE ACCENT BAR
         * =====================================================
         */

        JPanel topBar =
                new JPanel();

        topBar.setBackground(
                ParkXTheme.BLUE
        );

        topBar.setBounds(
                0,
                0,
                450,
                6
        );

        dialog.add(
                topBar
        );


        /*
         * =====================================================
         * PARKING ICON
         * =====================================================
         */

        JPanel iconPanel =
                createCircleIcon(
                        "P",
                        ParkXTheme.BLUE
                );

        iconPanel.setBounds(
                190,
                22,
                70,
                70
        );

        dialog.add(
                iconPanel
        );


        /*
         * =====================================================
         * TITLE
         * =====================================================
         */

        JLabel title =
                new JLabel(
                        "Parking Space Details",
                        SwingConstants.CENTER
                );

        title.setFont(
                ParkXTheme.titleFont(
                        21
                )
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        title.setBounds(
                40,
                100,
                370,
                30
        );

        dialog.add(
                title
        );


        /*
         * =====================================================
         * SLOT NUMBER
         * =====================================================
         */

        JLabel lblSlot =
                new JLabel(
                        "Slot"
                );

        lblSlot.setForeground(
                ParkXTheme.MUTED
        );

        lblSlot.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        lblSlot.setBounds(
                70,
                150,
                120,
                25
        );

        dialog.add(
                lblSlot
        );


        JLabel valueSlot =
                new JLabel(
                        space.getSlotId()
                );

        valueSlot.setForeground(
                ParkXTheme.TEXT
        );

        valueSlot.setFont(
                ParkXTheme.boldFont(
                        13
                )
        );

        valueSlot.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        valueSlot.setBounds(
                220,
                150,
                160,
                25
        );

        dialog.add(
                valueSlot
        );


        /*
         * =====================================================
         * VEHICLE NUMBER
         * =====================================================
         */

        JLabel lblVehicle =
                new JLabel(
                        "Vehicle No."
                );

        lblVehicle.setForeground(
                ParkXTheme.MUTED
        );

        lblVehicle.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        lblVehicle.setBounds(
                70,
                185,
                120,
                25
        );

        dialog.add(
                lblVehicle
        );


        JLabel valueVehicle =
                new JLabel(
                        vehicle.getVehicleNumber()
                );

        valueVehicle.setForeground(
                ParkXTheme.TEXT
        );

        valueVehicle.setFont(
                ParkXTheme.boldFont(
                        13
                )
        );

        valueVehicle.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        valueVehicle.setBounds(
                220,
                185,
                160,
                25
        );

        dialog.add(
                valueVehicle
        );


        /*
         * =====================================================
         * VEHICLE TYPE
         * =====================================================
         */

        JLabel lblType =
                new JLabel(
                        "Vehicle Type"
                );

        lblType.setForeground(
                ParkXTheme.MUTED
        );

        lblType.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        lblType.setBounds(
                70,
                220,
                120,
                25
        );

        dialog.add(
                lblType
        );


        JLabel valueType =
                new JLabel(
                        vehicle.getType()
                );

        valueType.setForeground(
                ParkXTheme.TEXT
        );

        valueType.setFont(
                ParkXTheme.boldFont(
                        13
                )
        );

        valueType.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        valueType.setBounds(
                220,
                220,
                160,
                25
        );

        dialog.add(
                valueType
        );


        /*
         * =====================================================
         * OWNER NAME
         * =====================================================
         */

        JLabel lblOwner =
                new JLabel(
                        "Owner"
                );

        lblOwner.setForeground(
                ParkXTheme.MUTED
        );

        lblOwner.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        lblOwner.setBounds(
                70,
                255,
                120,
                25
        );

        dialog.add(
                lblOwner
        );


        JLabel valueOwner =
                new JLabel(
                        vehicle.getOwnerName()
                );

        valueOwner.setForeground(
                ParkXTheme.TEXT
        );

        valueOwner.setFont(
                ParkXTheme.boldFont(
                        13
                )
        );

        valueOwner.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        valueOwner.setBounds(
                200,
                255,
                180,
                25
        );

        dialog.add(
                valueOwner
        );


        /*
         * =====================================================
         * ENTRY TIME
         * =====================================================
         */

        JLabel lblEntry =
                new JLabel(
                        "Entry Time"
                );

        lblEntry.setForeground(
                ParkXTheme.MUTED
        );

        lblEntry.setFont(
                ParkXTheme.normalFont(
                        13
                )
        );

        lblEntry.setBounds(
                70,
                290,
                120,
                25
        );

        dialog.add(
                lblEntry
        );


        JLabel valueEntry =
                new JLabel(
                        vehicle.getEntryTime()
                                .format(
                                        formatter
                                )
                );

        valueEntry.setForeground(
                ParkXTheme.TEXT
        );

        valueEntry.setFont(
                ParkXTheme.boldFont(
                        12
                )
        );

        valueEntry.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        valueEntry.setBounds(
                180,
                290,
                200,
                25
        );

        dialog.add(
                valueEntry
        );


        /*
         * =====================================================
         * SEPARATOR
         * =====================================================
         */

        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                ParkXTheme.BORDER
        );

        separator.setBackground(
                ParkXTheme.BORDER
        );

        separator.setBounds(
                50,
                325,
                350,
                1
        );

        dialog.add(
                separator
        );


        /*
         * =====================================================
         * CLOSE BUTTON
         * =====================================================
         */

        JButton btnClose =
                new JButton(
                        "CLOSE"
                );

        /*
         * This position now fits safely
         * inside the 440px dialog.
         */

        btnClose.setBounds(
                145,
                345,
                160,
                42
        );


        btnClose.setBackground(
                ParkXTheme.BLUE
        );


        btnClose.setForeground(
                Color.WHITE
        );


        btnClose.setFont(
                ParkXTheme.boldFont(
                        12
                )
        );


        btnClose.setFocusPainted(
                false
        );


        btnClose.setBorderPainted(
                false
        );


        btnClose.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        /*
         * Close dialog properly.
         */

        btnClose.addActionListener(
                e ->
                        dialog.dispose()
        );


        dialog.add(
                btnClose
        );


        /*
         * Pressing ENTER also closes
         * the details window.
         */

        dialog.getRootPane()
                .setDefaultButton(
                        btnClose
                );


        /*
         * Pressing ESC closes dialog.
         */

        dialog.getRootPane()
                .registerKeyboardAction(

                        e ->
                                dialog.dispose(),

                        KeyStroke.getKeyStroke(
                                "ESCAPE"
                        ),

                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );


        /*
         * Finally show the dialog.
         */

        dialog.setVisible(
                true
        );
    }


    /*
     * =========================================================
     * CREATE CIRCLE ICON
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


                        /*
                         * Create Graphics2D copy.
                         */

                        Graphics2D g2 =
                                (Graphics2D) g.create();


                        /*
                         * Smooth edges.
                         */

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );


                        /*
                         * -------------------------------------------------
                         * TRANSPARENT BACKGROUND CIRCLE
                         * -------------------------------------------------
                         */

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


                        /*
                         * -------------------------------------------------
                         * CIRCLE BORDER
                         * -------------------------------------------------
                         */

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


                        /*
                         * -------------------------------------------------
                         * ICON TEXT
                         * -------------------------------------------------
                         */

                        g2.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        28
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


                        /*
                         * Release graphics resources.
                         */

                        g2.dispose();
                    }
                };


        panel.setOpaque(
                false
        );


        return panel;
    }
}