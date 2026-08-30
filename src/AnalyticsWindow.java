import javax.swing.*;
import java.awt.*;

public class AnalyticsWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private ParkingManager manager;


    public AnalyticsWindow(
            ParkingManager manager) {

        this.manager = manager;

        setTitle("ParkX - Analytics");

        setSize(1100, 720);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE);

        setResizable(false);

        setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND);

        createInterface();

        setVisible(true);
    }


    private void createInterface() {

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "Parking Analytics");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30));

        title.setForeground(
                ParkXTheme.TEXT);

        title.setBounds(
                40,
                25,
                400,
                40);

        getContentPane().add(title);


        JLabel subtitle =
                new JLabel(
                        "Real-time overview of your ParkX parking facility");

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13));

        subtitle.setForeground(
                ParkXTheme.MUTED);

        subtitle.setBounds(
                40,
                65,
                450,
                25);

        getContentPane().add(subtitle);


        JLabel live =
                new JLabel(
                        "●  LIVE");

        live.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12));

        live.setForeground(
                ParkXTheme.GREEN);

        live.setBounds(
                940,
                35,
                100,
                30);

        getContentPane().add(live);


        // =====================================================
        // TOTAL SPACES CARD
        // =====================================================

        JPanel totalCard =
                new JPanel();

        totalCard.setLayout(null);

        totalCard.setBackground(
                ParkXTheme.CARD);

        totalCard.setBounds(
                40,
                120,
                220,
                150);

        totalCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(totalCard);


        JPanel totalBar =
                new JPanel();

        totalBar.setBackground(
                ParkXTheme.BLUE);

        totalBar.setBounds(
                0,
                0,
                6,
                150);

        totalCard.add(totalBar);


        JPanel totalIndicator =
                new JPanel();

        totalIndicator.setBackground(
                ParkXTheme.BLUE_DARK);

        totalIndicator.setBounds(
                170,
                20,
                30,
                30);

        totalCard.add(totalIndicator);


        JLabel totalTitle =
                new JLabel(
                        "TOTAL SPACES");

        totalTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12));

        totalTitle.setForeground(
                ParkXTheme.MUTED);

        totalTitle.setBounds(
                22,
                18,
                150,
                25);

        totalCard.add(totalTitle);


        JLabel totalValue =
                new JLabel(
                        "80");

        totalValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        35));

        totalValue.setForeground(
                ParkXTheme.BLUE);

        totalValue.setBounds(
                22,
                50,
                150,
                45);

        totalCard.add(totalValue);


        JLabel totalDescription =
                new JLabel(
                        "All parking slots");

        totalDescription.setForeground(
                ParkXTheme.MUTED);

        totalDescription.setBounds(
                22,
                108,
                180,
                20);

        totalCard.add(totalDescription);


        // =====================================================
        // OCCUPIED CARD
        // =====================================================

        JPanel occupiedCard =
                new JPanel();

        occupiedCard.setLayout(null);

        occupiedCard.setBackground(
                ParkXTheme.CARD);

        occupiedCard.setBounds(
                300,
                120,
                220,
                150);

        occupiedCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(occupiedCard);


        JPanel occupiedBar =
                new JPanel();

        occupiedBar.setBackground(
                ParkXTheme.RED);

        occupiedBar.setBounds(
                0,
                0,
                6,
                150);

        occupiedCard.add(occupiedBar);


        JPanel occupiedIndicator =
                new JPanel();

        occupiedIndicator.setBackground(
                ParkXTheme.RED_DARK);

        occupiedIndicator.setBounds(
                170,
                20,
                30,
                30);

        occupiedCard.add(
                occupiedIndicator);


        JLabel occupiedTitle =
                new JLabel(
                        "OCCUPIED");

        occupiedTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12));

        occupiedTitle.setForeground(
                ParkXTheme.MUTED);

        occupiedTitle.setBounds(
                22,
                18,
                150,
                25);

        occupiedCard.add(
                occupiedTitle);


        JLabel occupiedValue =
                new JLabel(
                        String.valueOf(
                                manager
                                        .getOccupiedSlots()));

        occupiedValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        35));

        occupiedValue.setForeground(
                ParkXTheme.RED);

        occupiedValue.setBounds(
                22,
                50,
                150,
                45);

        occupiedCard.add(
                occupiedValue);


        JLabel occupiedDescription =
                new JLabel(
                        "Currently occupied");

        occupiedDescription.setForeground(
                ParkXTheme.MUTED);

        occupiedDescription.setBounds(
                22,
                108,
                180,
                20);

        occupiedCard.add(
                occupiedDescription);


        // =====================================================
        // AVAILABLE CARD
        // =====================================================

        JPanel availableCard =
                new JPanel();

        availableCard.setLayout(null);

        availableCard.setBackground(
                ParkXTheme.CARD);

        availableCard.setBounds(
                560,
                120,
                220,
                150);

        availableCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                availableCard);


        JPanel availableBar =
                new JPanel();

        availableBar.setBackground(
                ParkXTheme.GREEN);

        availableBar.setBounds(
                0,
                0,
                6,
                150);

        availableCard.add(
                availableBar);


        JPanel availableIndicator =
                new JPanel();

        availableIndicator.setBackground(
                ParkXTheme.GREEN_DARK);

        availableIndicator.setBounds(
                170,
                20,
                30,
                30);

        availableCard.add(
                availableIndicator);


        JLabel availableTitle =
                new JLabel(
                        "AVAILABLE");

        availableTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12));

        availableTitle.setForeground(
                ParkXTheme.MUTED);

        availableTitle.setBounds(
                22,
                18,
                150,
                25);

        availableCard.add(
                availableTitle);


        JLabel availableValue =
                new JLabel(
                        String.valueOf(
                                manager
                                        .getAvailableSlots()));

        availableValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        35));

        availableValue.setForeground(
                ParkXTheme.GREEN);

        availableValue.setBounds(
                22,
                50,
                150,
                45);

        availableCard.add(
                availableValue);


        JLabel availableDescription =
                new JLabel(
                        "Ready for parking");

        availableDescription.setForeground(
                ParkXTheme.MUTED);

        availableDescription.setBounds(
                22,
                108,
                180,
                20);

        availableCard.add(
                availableDescription);


        // =====================================================
        // ACTIVE SESSIONS CARD
        // =====================================================

        JPanel activeCard =
                new JPanel();

        activeCard.setLayout(null);

        activeCard.setBackground(
                ParkXTheme.CARD);

        activeCard.setBounds(
                820,
                120,
                220,
                150);

        activeCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                activeCard);


        JPanel activeBar =
                new JPanel();

        activeBar.setBackground(
                ParkXTheme.PURPLE);

        activeBar.setBounds(
                0,
                0,
                6,
                150);

        activeCard.add(activeBar);


        JPanel activeIndicator =
                new JPanel();

        activeIndicator.setBackground(
                ParkXTheme.PURPLE_DARK);

        activeIndicator.setBounds(
                170,
                20,
                30,
                30);

        activeCard.add(
                activeIndicator);


        JLabel activeTitle =
                new JLabel(
                        "ACTIVE SESSIONS");

        activeTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12));

        activeTitle.setForeground(
                ParkXTheme.MUTED);

        activeTitle.setBounds(
                22,
                18,
                150,
                25);

        activeCard.add(
                activeTitle);


        JLabel activeValue =
                new JLabel(
                        String.valueOf(
                                manager
                                        .getVehicles()
                                        .size()));

        activeValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        35));

        activeValue.setForeground(
                ParkXTheme.PURPLE);

        activeValue.setBounds(
                22,
                50,
                150,
                45);

        activeCard.add(
                activeValue);


        JLabel activeDescription =
                new JLabel(
                        "Vehicles currently parked");

        activeDescription.setForeground(
                ParkXTheme.MUTED);

        activeDescription.setBounds(
                22,
                108,
                190,
                20);

        activeCard.add(
                activeDescription);


        // =====================================================
        // FLOOR TITLE
        // =====================================================

        JLabel floorTitle =
                new JLabel(
                        "Floor Occupancy");

        floorTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21));

        floorTitle.setForeground(
                ParkXTheme.TEXT);

        floorTitle.setBounds(
                40,
                315,
                300,
                35);

        getContentPane().add(
                floorTitle);


        JLabel floorSubtitle =
                new JLabel(
                        "Current parking usage across all four floors");

        floorSubtitle.setForeground(
                ParkXTheme.MUTED);

        floorSubtitle.setBounds(
                40,
                345,
                350,
                25);

        getContentPane().add(
                floorSubtitle);


        // =====================================================
        // GROUND FLOOR CARD
        // =====================================================

        int groundOccupied =
                manager
                        .getFloorOccupiedCount(
                                "Ground Floor");


        JPanel groundCard =
                new JPanel();

        groundCard.setLayout(null);

        groundCard.setBackground(
                ParkXTheme.CARD);

        groundCard.setBounds(
                40,
                390,
                220,
                190);

        groundCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                groundCard);


        JLabel groundTitle =
                new JLabel(
                        "Ground Floor");

        groundTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16));

        groundTitle.setForeground(
                ParkXTheme.TEXT);

        groundTitle.setBounds(
                20,
                15,
                170,
                30);

        groundCard.add(
                groundTitle);


        JLabel groundSlots =
                new JLabel(
                        "G1 - G20");

        groundSlots.setForeground(
                ParkXTheme.MUTED);

        groundSlots.setBounds(
                20,
                43,
                150,
                20);

        groundCard.add(
                groundSlots);


        JLabel groundValue =
                new JLabel(
                        groundOccupied
                                + " / 20");

        groundValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25));

        groundValue.setForeground(
                ParkXTheme.BLUE);

        groundValue.setBounds(
                20,
                75,
                160,
                35);

        groundCard.add(
                groundValue);


        JLabel groundText =
                new JLabel(
                        "spaces occupied");

        groundText.setForeground(
                ParkXTheme.MUTED);

        groundText.setBounds(
                20,
                108,
                160,
                20);

        groundCard.add(
                groundText);


        JProgressBar groundProgress =
                new JProgressBar(
                        0,
                        20);

        groundProgress.setValue(
                groundOccupied);

        groundProgress.setForeground(
                ParkXTheme.BLUE);

        groundProgress.setBackground(
                ParkXTheme.INPUT);

        groundProgress.setBorderPainted(
                false);

        groundProgress.setBounds(
                20,
                140,
                180,
                10);

        groundCard.add(
                groundProgress);


        // =====================================================
        // FIRST FLOOR CARD
        // =====================================================

        int firstOccupied =
                manager
                        .getFloorOccupiedCount(
                                "First Floor");


        JPanel firstCard =
                new JPanel();

        firstCard.setLayout(null);

        firstCard.setBackground(
                ParkXTheme.CARD);

        firstCard.setBounds(
                300,
                390,
                220,
                190);

        firstCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                firstCard);


        JLabel firstTitle =
                new JLabel(
                        "First Floor");

        firstTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16));

        firstTitle.setForeground(
                ParkXTheme.TEXT);

        firstTitle.setBounds(
                20,
                15,
                170,
                30);

        firstCard.add(
                firstTitle);


        JLabel firstSlots =
                new JLabel(
                        "F1 - F20");

        firstSlots.setForeground(
                ParkXTheme.MUTED);

        firstSlots.setBounds(
                20,
                43,
                150,
                20);

        firstCard.add(
                firstSlots);


        JLabel firstValue =
                new JLabel(
                        firstOccupied
                                + " / 20");

        firstValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25));

        firstValue.setForeground(
                ParkXTheme.PURPLE);

        firstValue.setBounds(
                20,
                75,
                160,
                35);

        firstCard.add(
                firstValue);


        JLabel firstText =
                new JLabel(
                        "spaces occupied");

        firstText.setForeground(
                ParkXTheme.MUTED);

        firstText.setBounds(
                20,
                108,
                160,
                20);

        firstCard.add(
                firstText);


        JProgressBar firstProgress =
                new JProgressBar(
                        0,
                        20);

        firstProgress.setValue(
                firstOccupied);

        firstProgress.setForeground(
                ParkXTheme.PURPLE);

        firstProgress.setBackground(
                ParkXTheme.INPUT);

        firstProgress.setBorderPainted(
                false);

        firstProgress.setBounds(
                20,
                140,
                180,
                10);

        firstCard.add(
                firstProgress);


        // =====================================================
        // SECOND FLOOR CARD
        // =====================================================

        int secondOccupied =
                manager
                        .getFloorOccupiedCount(
                                "Second Floor");


        JPanel secondCard =
                new JPanel();

        secondCard.setLayout(null);

        secondCard.setBackground(
                ParkXTheme.CARD);

        secondCard.setBounds(
                560,
                390,
                220,
                190);

        secondCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                secondCard);


        JLabel secondTitle =
                new JLabel(
                        "Second Floor");

        secondTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16));

        secondTitle.setForeground(
                ParkXTheme.TEXT);

        secondTitle.setBounds(
                20,
                15,
                170,
                30);

        secondCard.add(
                secondTitle);


        JLabel secondSlots =
                new JLabel(
                        "S1 - S20");

        secondSlots.setForeground(
                ParkXTheme.MUTED);

        secondSlots.setBounds(
                20,
                43,
                150,
                20);

        secondCard.add(
                secondSlots);


        JLabel secondValue =
                new JLabel(
                        secondOccupied
                                + " / 20");

        secondValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25));

        secondValue.setForeground(
                ParkXTheme.AMBER);

        secondValue.setBounds(
                20,
                75,
                160,
                35);

        secondCard.add(
                secondValue);


        JLabel secondText =
                new JLabel(
                        "spaces occupied");

        secondText.setForeground(
                ParkXTheme.MUTED);

        secondText.setBounds(
                20,
                108,
                160,
                20);

        secondCard.add(
                secondText);


        JProgressBar secondProgress =
                new JProgressBar(
                        0,
                        20);

        secondProgress.setValue(
                secondOccupied);

        secondProgress.setForeground(
                ParkXTheme.AMBER);

        secondProgress.setBackground(
                ParkXTheme.INPUT);

        secondProgress.setBorderPainted(
                false);

        secondProgress.setBounds(
                20,
                140,
                180,
                10);

        secondCard.add(
                secondProgress);


        // =====================================================
        // THIRD FLOOR CARD
        // =====================================================

        int thirdOccupied =
                manager
                        .getFloorOccupiedCount(
                                "Third Floor");


        JPanel thirdCard =
                new JPanel();

        thirdCard.setLayout(null);

        thirdCard.setBackground(
                ParkXTheme.CARD);

        thirdCard.setBounds(
                820,
                390,
                220,
                190);

        thirdCard.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        getContentPane().add(
                thirdCard);


        JLabel thirdTitle =
                new JLabel(
                        "Third Floor");

        thirdTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16));

        thirdTitle.setForeground(
                ParkXTheme.TEXT);

        thirdTitle.setBounds(
                20,
                15,
                170,
                30);

        thirdCard.add(
                thirdTitle);


        JLabel thirdSlots =
                new JLabel(
                        "T1 - T20");

        thirdSlots.setForeground(
                ParkXTheme.MUTED);

        thirdSlots.setBounds(
                20,
                43,
                150,
                20);

        thirdCard.add(
                thirdSlots);


        JLabel thirdValue =
                new JLabel(
                        thirdOccupied
                                + " / 20");

        thirdValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25));

        thirdValue.setForeground(
                ParkXTheme.GREEN);

        thirdValue.setBounds(
                20,
                75,
                160,
                35);

        thirdCard.add(
                thirdValue);


        JLabel thirdText =
                new JLabel(
                        "spaces occupied");

        thirdText.setForeground(
                ParkXTheme.MUTED);

        thirdText.setBounds(
                20,
                108,
                160,
                20);

        thirdCard.add(
                thirdText);


        JProgressBar thirdProgress =
                new JProgressBar(
                        0,
                        20);

        thirdProgress.setValue(
                thirdOccupied);

        thirdProgress.setForeground(
                ParkXTheme.GREEN);

        thirdProgress.setBackground(
                ParkXTheme.INPUT);

        thirdProgress.setBorderPainted(
                false);

        thirdProgress.setBounds(
                20,
                140,
                180,
                10);

        thirdCard.add(
                thirdProgress);


        // =====================================================
        // FOOTER
        // =====================================================

        JLabel footer =
                new JLabel(
                        "ParkX  •  Parking Management System");

        footer.setForeground(
                ParkXTheme.MUTED);

        footer.setHorizontalAlignment(
                SwingConstants.CENTER);

        footer.setBounds(
                300,
                620,
                500,
                25);

        getContentPane().add(
                footer);
    }
}