import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private final ParkingHistoryDAO historyDAO = new ParkingHistoryDAO();
    private final DateTimeFormatter dateTimeFormat =
            DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm a");
    private final JSpinner fromDate;
    private final JSpinner toDate;
    private final DefaultTableModel model;
    private final JLabel summary;

    public HistoryWindow() {
        setTitle("ParkX - Parking History");
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(ParkXTheme.BACKGROUND);

        JLabel title = label("Parking History", 30, Font.BOLD, ParkXTheme.TEXT);
        title.setBounds(40, 25, 400, 42);
        add(title);

        JLabel subtitle = label("Completed vehicle parking sessions and payments",
                13, Font.PLAIN, ParkXTheme.MUTED);
        subtitle.setBounds(40, 65, 500, 24);
        add(subtitle);

        JPanel filters = new JPanel(null);
        filters.setBackground(ParkXTheme.CARD);
        filters.setBorder(BorderFactory.createLineBorder(ParkXTheme.BORDER));
        filters.setBounds(40, 110, 1090, 90);
        add(filters);

        filters.add(positionedLabel("From Date", 24, 15, 100, 24));
        filters.add(positionedLabel("To Date", 330, 15, 100, 24));

        LocalDate today = LocalDate.now();
        fromDate = createDateSpinner(today.minusDays(30));
        fromDate.setBounds(110, 20, 180, 38);
        filters.add(fromDate);

        toDate = createDateSpinner(today);
        toDate.setBounds(405, 20, 180, 38);
        filters.add(toDate);

        JButton filterButton = button("Filter History", ParkXTheme.BLUE);
        filterButton.setBounds(620, 20, 180, 38);
        filterButton.addActionListener(e -> loadHistory());
        filters.add(filterButton);

        JButton last30Days = button("Last 30 Days", ParkXTheme.BLUE_DARK);
        last30Days.setBounds(820, 20, 170, 38);
        last30Days.addActionListener(e -> {
            setSpinnerDate(fromDate, LocalDate.now().minusDays(30));
            setSpinnerDate(toDate, LocalDate.now());
            loadHistory();
        });
        filters.add(last30Days);

        model = new DefaultTableModel(new String[] {
                "Receipt", "Vehicle No", "Owner", "Type", "Slot",
                "Parking Time", "Park Out Time", "Hours", "Rate", "Amount"
        }, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(ParkXTheme.CARD);
        table.setForeground(ParkXTheme.TEXT);
        table.setGridColor(ParkXTheme.BORDER);
        table.setSelectionBackground(ParkXTheme.BLUE_DARK);
        table.setSelectionForeground(ParkXTheme.TEXT);
        table.setRowHeight(38);
        table.setFont(ParkXTheme.normalFont(12));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(ParkXTheme.INPUT);
        header.setForeground(ParkXTheme.TEXT);
        header.setFont(ParkXTheme.boldFont(12));
        header.setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer();
        moneyRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(8).setCellRenderer(moneyRenderer);
        table.getColumnModel().getColumn(9).setCellRenderer(moneyRenderer);

        int[] widths = {55, 85, 120, 75, 45, 155, 155, 45, 65, 80};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 220, 1090, 390);
        scroll.setBorder(BorderFactory.createLineBorder(ParkXTheme.BORDER));
        scroll.getViewport().setBackground(ParkXTheme.CARD);
        add(scroll);

        summary = label("", 14, Font.BOLD, ParkXTheme.GREEN);
        summary.setBounds(40, 625, 850, 30);
        add(summary);

        loadHistory();
        setVisible(true);
    }

    private void loadHistory() {
        LocalDate from = spinnerDate(fromDate);
        LocalDate to = spinnerDate(toDate);
        if (from.isAfter(to)) {
            JOptionPane.showMessageDialog(this,
                    "From Date cannot be after To Date.", "Invalid Date Range",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<ParkingHistoryRecord> records =
                    historyDAO.findByExitDateRange(from, to);
            model.setRowCount(0);
            double total = 0;
            for (ParkingHistoryRecord record : records) {
                total += record.getTotalFee();
                model.addRow(new Object[] {
                        record.getId(), record.getVehicleNumber(),
                        record.getOwnerName(), record.getVehicleType(),
                        record.getSlotId(),
                        record.getEntryTime().format(dateTimeFormat),
                        record.getExitTime().format(dateTimeFormat),
                        record.getChargedHours(),
                        String.format("Rs. %,.2f", record.getHourlyRate()),
                        String.format("Rs. %,.2f", record.getTotalFee())
                });
            }
            summary.setText(String.format(
                    "%d completed session%s   |   Total collected: Rs. %,.2f",
                    records.size(), records.size() == 1 ? "" : "s", total));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load parking history.\n" + ex.getMessage(),
                    "History Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JSpinner createDateSpinner(LocalDate date) {
        SpinnerDateModel spinnerModel = new SpinnerDateModel(
                toDate(date), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd MMM yyyy"));
        spinner.setFont(ParkXTheme.normalFont(13));
        return spinner;
    }

    private LocalDate spinnerDate(JSpinner spinner) {
        return ((Date) spinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void setSpinnerDate(JSpinner spinner, LocalDate date) {
        spinner.setValue(toDate(date));
    }

    private Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private JLabel positionedLabel(String text, int x, int y, int w, int h) {
        JLabel label = label(text, 13, Font.BOLD, ParkXTheme.TEXT);
        label.setBounds(x, y, w, h);
        return label;
    }

    private JLabel label(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);
        return label;
    }

    private JButton button(String text, Color background) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(ParkXTheme.boldFont(12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
