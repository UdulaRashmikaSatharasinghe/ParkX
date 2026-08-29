import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportSaveDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JTextField fileNameField;
    private final JTextField folderField;
    private Path selectedPath;

    public static Path showDialog(Window owner, LocalDate from, LocalDate to) {
        ReportSaveDialog dialog = new ReportSaveDialog(owner, from, to);
        dialog.setVisible(true);
        return dialog.selectedPath;
    }

    private ReportSaveDialog(Window owner, LocalDate from, LocalDate to) {
        super(owner, "Export ParkX Report", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 530);
        setResizable(false);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ParkXTheme.BACKGROUND);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(null);
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 34, 0, 34));
        root.add(content, BorderLayout.CENTER);

        JPanel reportCard = roundedPanel(ParkXTheme.CARD);
        reportCard.setLayout(null);
        reportCard.setBounds(34, 24, 566, 90);
        content.add(reportCard);

        JLabel pdfBadge = new JLabel("PDF", SwingConstants.CENTER);
        pdfBadge.setFont(ParkXTheme.boldFont(13));
        pdfBadge.setForeground(Color.WHITE);
        pdfBadge.setOpaque(true);
        pdfBadge.setBackground(ParkXTheme.RED);
        pdfBadge.setBounds(20, 20, 52, 50);
        reportCard.add(pdfBadge);

        JLabel reportTitle = text("Parking History & Analytics Report", 17,
                Font.BOLD, ParkXTheme.TEXT);
        reportTitle.setBounds(92, 17, 390, 27);
        reportCard.add(reportTitle);

        DateTimeFormatter periodFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        JLabel period = text("Reporting period  •  " + from.format(periodFormat)
                + "  —  " + to.format(periodFormat), 12, Font.PLAIN, ParkXTheme.MUTED);
        period.setBounds(92, 46, 430, 24);
        reportCard.add(period);

        JLabel fileLabel = text("FILE NAME", 11, Font.BOLD, ParkXTheme.MUTED);
        fileLabel.setBounds(34, 140, 160, 20);
        content.add(fileLabel);

        fileNameField = input();
        fileNameField.setText("PARKX-Analytics-Report-"
                + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".pdf");
        fileNameField.setBounds(34, 164, 566, 44);
        content.add(fileNameField);

        JLabel folderLabel = text("SAVE LOCATION", 11, Font.BOLD, ParkXTheme.MUTED);
        folderLabel.setBounds(34, 228, 160, 20);
        content.add(folderLabel);

        folderField = input();
        folderField.setText(defaultFolder().toString());
        folderField.setBounds(34, 252, 440, 44);
        content.add(folderField);

        JButton browse = new ActionButton("Browse", ParkXTheme.BLUE_DARK, 12);
        browse.setBounds(486, 252, 114, 44);
        browse.addActionListener(e -> browseFolder());
        content.add(browse);

        JPanel actions = new JPanel(null);
        actions.setBackground(ParkXTheme.HEADER);
        actions.setPreferredSize(new Dimension(650, 82));
        root.add(actions, BorderLayout.SOUTH);

        JButton cancel = new ActionButton("Cancel", ParkXTheme.CARD_LIGHT, 13);
        cancel.setBounds(350, 20, 110, 42);
        cancel.addActionListener(e -> dispose());
        actions.add(cancel);

        JButton export = new ActionButton("Export PDF  →", ParkXTheme.BLUE, 13);
        export.setBounds(474, 20, 142, 42);
        export.addActionListener(e -> approve());
        actions.add(export);
        getRootPane().setDefaultButton(export);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(null);
        header.setBackground(ParkXTheme.HEADER);
        header.setPreferredSize(new Dimension(650, 86));

        JLabel brand = text("ParkX", 23, Font.BOLD, ParkXTheme.AMBER);
        brand.setBounds(34, 17, 100, 30);
        header.add(brand);
        JLabel title = text("Export Analytics Report", 19, Font.BOLD, ParkXTheme.TEXT);
        title.setBounds(34, 46, 300, 28);
        header.add(title);
        JLabel secure = text("READY TO EXPORT", 11, Font.BOLD, ParkXTheme.GREEN);
        secure.setHorizontalAlignment(SwingConstants.RIGHT);
        secure.setBounds(440, 31, 176, 24);
        header.add(secure);
        return header;
    }

    private void browseFolder() {
        JFileChooser chooser = new JFileChooser(folderField.getText().trim());
        chooser.setDialogTitle("Choose Report Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            folderField.setText(chooser.getSelectedFile().toPath().toAbsolutePath().toString());
    }

    private void approve() {
        String fileName = fileNameField.getText().trim();
        String folder = folderField.getText().trim();
        if (fileName.isEmpty() || folder.isEmpty()) {
            showValidation("Enter both a file name and save location.");
            return;
        }
        if (!fileName.toLowerCase().endsWith(".pdf")) fileName += ".pdf";
        if (fileName.contains("/") || fileName.contains("\\")) {
            showValidation("The file name cannot contain folder separators.");
            return;
        }

        Path directory;
        try {
            directory = Path.of(folder).toAbsolutePath().normalize();
        } catch (Exception ex) {
            showValidation("The save location is not valid.");
            return;
        }
        if (!Files.isDirectory(directory)) {
            showValidation("The selected save location does not exist.");
            return;
        }

        Path output = directory.resolve(fileName);
        if (Files.exists(output)) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "A report with this name already exists.\nReplace the existing file?",
                    "Replace Existing Report", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (answer != JOptionPane.YES_OPTION) return;
        }
        selectedPath = output;
        dispose();
    }

    private void showValidation(String message) {
        JOptionPane.showMessageDialog(this, message, "Check Export Details",
                JOptionPane.WARNING_MESSAGE);
    }

    private Path defaultFolder() {
        Path documents = Path.of(System.getProperty("user.home"), "Documents");
        return Files.isDirectory(documents) ? documents : Path.of(System.getProperty("user.home"));
    }

    private JTextField input() {
        JTextField field = new JTextField();
        field.setFont(ParkXTheme.normalFont(13));
        field.setForeground(ParkXTheme.TEXT);
        field.setCaretColor(ParkXTheme.TEXT);
        field.setBackground(ParkXTheme.INPUT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ParkXTheme.BORDER),
                new EmptyBorder(0, 13, 0, 13)));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ParkXTheme.BLUE, 2),
                        new EmptyBorder(0, 12, 0, 12)));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ParkXTheme.BORDER),
                        new EmptyBorder(0, 13, 0, 13)));
            }
        });
        return field;
    }

    private JPanel roundedPanel(Color color) {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(ParkXTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    private JLabel text(String value, int size, int style, Color color) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);
        return label;
    }

    private static class ActionButton extends JButton {
        private static final long serialVersionUID = 1L;
        private final Color normal;
        private boolean hover;

        ActionButton(String text, Color normal, int fontSize) {
            super(text);
            this.normal = normal;
            setForeground(Color.WHITE);
            setFont(ParkXTheme.boldFont(fontSize));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? normal.brighter() : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 13, 13);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
