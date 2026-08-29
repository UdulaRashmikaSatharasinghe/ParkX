import javax.swing.*;
import java.awt.*;

public class BillWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextArea receiptArea;

    public BillWindow(
            Vehicle vehicle) {

        setTitle("ParkX - Parking Receipt");
        setSize(540, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND);

        JLabel title =
                new JLabel("PARKX");

        title.setFont(
                ParkXTheme.titleFont(28));

        title.setForeground(
                ParkXTheme.AMBER);

        title.setHorizontalAlignment(
                SwingConstants.CENTER);

        title.setBounds(
                100,
                20,
                320,
                40);

        add(title);


        JLabel subtitle =
                new JLabel(
                        "Parking Receipt");

        subtitle.setForeground(
                ParkXTheme.MUTED);

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER);

        subtitle.setBounds(
                100,
                53,
                320,
                25);

        add(subtitle);


        receiptArea =
                new JTextArea(
                        Bill.generateBill(
                                vehicle));

        receiptArea.setEditable(false);

        receiptArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14));

        receiptArea.setBackground(
                ParkXTheme.CARD);

        receiptArea.setForeground(
                ParkXTheme.TEXT);

        receiptArea.setCaretColor(
                ParkXTheme.TEXT);

        receiptArea.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15));

        JScrollPane scroll =
                new JScrollPane(
                        receiptArea);

        scroll.setBounds(
                55,
                90,
                410,
                410);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        add(scroll);


        JButton btnPrint =
                new JButton(
                        "Print Receipt");

        btnPrint.setBounds(
                70,
                535,
                170,
                42);

        btnPrint.setBackground(
                ParkXTheme.BLUE);

        btnPrint.setForeground(
                Color.WHITE);

        add(btnPrint);


        JButton btnClose =
                new JButton("Close");

        btnClose.setBounds(
                280,
                535,
                170,
                42);

        btnClose.setBackground(
                ParkXTheme.CARD_LIGHT);

        btnClose.setForeground(
                ParkXTheme.TEXT);

        add(btnClose);


        btnPrint.addActionListener(
                e -> printReceipt());

        btnClose.addActionListener(
                e -> dispose());

        setVisible(true);
    }

    private void printReceipt() {

        try {
            receiptArea.print();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to print receipt.");
        }
    }
}
