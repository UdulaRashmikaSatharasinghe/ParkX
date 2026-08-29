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
                    "dd MMM yyyy - hh:mm a");

    public ParkingSpacesWindow(
            ParkingManager manager) {

        this.manager = manager;

        setTitle("ParkX - Parking Spaces");
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND);

        createInterface();
        showFloor("Ground Floor");

        setVisible(true);
    }

    private void createInterface() {

        JLabel title =
                new JLabel("Parking Spaces");

        title.setFont(
                ParkXTheme.titleFont(28));

        title.setForeground(
                ParkXTheme.TEXT);

        title.setBounds(
                40,
                25,
                300,
                40);

        add(title);


        JLabel subtitle =
                new JLabel(
                        "Live floor occupancy overview");

        subtitle.setForeground(
                ParkXTheme.MUTED);

        subtitle.setBounds(
                40,
                65,
                300,
                25);

        add(subtitle);


        JLabel lblFloor =
                new JLabel("Select Floor");

        lblFloor.setForeground(
                ParkXTheme.TEXT);

        lblFloor.setBounds(
                40,
                115,
                100,
                30);

        add(lblFloor);


        cmbFloor =
                new JComboBox<>(
                        new String[] {
                                "Ground Floor",
                                "First Floor",
                                "Second Floor",
                                "Third Floor"
                        });

        cmbFloor.setBounds(
                145,
                115,
                190,
                35);

        cmbFloor.setBackground(
                ParkXTheme.INPUT);

        cmbFloor.setForeground(
                ParkXTheme.TEXT);

        add(cmbFloor);


        lblAvailable =
                new JLabel();

        lblAvailable.setFont(
                ParkXTheme.boldFont(16));

        lblAvailable.setForeground(
                ParkXTheme.GREEN);

        lblAvailable.setBounds(
                580,
                115,
                170,
                35);

        add(lblAvailable);


        lblOccupied =
                new JLabel();

        lblOccupied.setFont(
                ParkXTheme.boldFont(16));

        lblOccupied.setForeground(
                ParkXTheme.RED);

        lblOccupied.setBounds(
                760,
                115,
                170,
                35);

        add(lblOccupied);


        slotsPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                5,
                                15,
                                15));

        slotsPanel.setBackground(
                ParkXTheme.BACKGROUND);


        JScrollPane scroll =
                new JScrollPane(
                        slotsPanel);

        scroll.setBounds(
                40,
                180,
                900,
                400);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(
                ParkXTheme.BACKGROUND);

        add(scroll);


        cmbFloor.addActionListener(
                e ->
                        showFloor(
                                cmbFloor
                                        .getSelectedItem()
                                        .toString()));
    }

    private void showFloor(
            String floor) {

        slotsPanel.removeAll();

        int available = 0;
        int occupied = 0;

        for (ParkingSpace space :
                manager.getParkingSpaces()) {

            if (!space.getFloor()
                    .equals(floor)) {

                continue;
            }

            JButton slot =
                    new JButton();

            slot.setFocusPainted(false);
            slot.setFont(
                    ParkXTheme.boldFont(14));

            if (space.isOccupied()) {

                occupied++;

                Vehicle vehicle =
                        space.getVehicle();

                slot.setText(
                        "<html><center><b>"
                                + space.getSlotId()
                                + "</b><br><br>"
                                + vehicle.getVehicleNumber()
                                + "<br>"
                                + vehicle.getType()
                                + "<br><br>OCCUPIED"
                                + "</center></html>");

                slot.setBackground(
                        ParkXTheme.RED_DARK);

                slot.setForeground(
                        new Color(
                                255,
                                150,
                                150));

                slot.addActionListener(
                        e ->
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Slot: "
                                                + space.getSlotId()
                                                + "\nVehicle: "
                                                + vehicle.getVehicleNumber()
                                                + "\nType: "
                                                + vehicle.getType()
                                                + "\nOwner: "
                                                + vehicle.getOwnerName()
                                                + "\nEntry Time: "
                                                + vehicle.getEntryTime()
                                                        .format(formatter)));

            } else {

                available++;

                slot.setText(
                        "<html><center><b>"
                                + space.getSlotId()
                                + "</b><br><br>"
                                + "AVAILABLE"
                                + "</center></html>");

                slot.setBackground(
                        ParkXTheme.GREEN_DARK);

                slot.setForeground(
                        new Color(
                                100,
                                240,
                                170));
            }

            slotsPanel.add(slot);
        }

        lblAvailable.setText(
                "Available: "
                        + available);

        lblOccupied.setText(
                "Occupied: "
                        + occupied);

        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
}
