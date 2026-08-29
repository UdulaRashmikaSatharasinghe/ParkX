import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private UserDAO userDAO;

    public LoginForm() {

        userDAO = new UserDAO();

        setTitle("ParkX - Login");
        setSize(520, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(
                ParkXTheme.BACKGROUND
        );

        createInterface();

        setVisible(true);
    }

    private void createInterface() {

        JLabel logo = new JLabel("ParkX");

        logo.setFont(
                ParkXTheme.titleFont(34)
        );

        logo.setForeground(
                ParkXTheme.AMBER
        );

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        logo.setBounds(
                110,
                35,
                300,
                45
        );

        add(logo);


        JLabel subtitle =
                new JLabel(
                        "Parking Management System"
                );

        subtitle.setFont(
                ParkXTheme.normalFont(13)
        );

        subtitle.setForeground(
                ParkXTheme.MUTED
        );

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        subtitle.setBounds(
                110,
                78,
                300,
                25
        );

        add(subtitle);


        JPanel card = new JPanel();

        card.setLayout(null);

        card.setBackground(
                ParkXTheme.CARD
        );

        card.setBounds(
                70,
                130,
                380,
                275
        );

        card.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER
                )
        );

        add(card);


        JLabel loginTitle =
                new JLabel(
                        "Welcome Back"
                );

        loginTitle.setFont(
                ParkXTheme.titleFont(21)
        );

        loginTitle.setForeground(
                ParkXTheme.TEXT
        );

        loginTitle.setBounds(
                30,
                20,
                250,
                30
        );

        card.add(loginTitle);


        JLabel user =
                new JLabel("Username");

        user.setForeground(
                ParkXTheme.MUTED
        );

        user.setFont(
                ParkXTheme.normalFont(13)
        );

        user.setBounds(
                30,
                70,
                100,
                25
        );

        card.add(user);


        txtUsername =
                new JTextField();

        txtUsername.setBounds(
                30,
                95,
                320,
                38
        );

        styleField(txtUsername);

        card.add(txtUsername);


        JLabel pass =
                new JLabel("Password");

        pass.setForeground(
                ParkXTheme.MUTED
        );

        pass.setFont(
                ParkXTheme.normalFont(13)
        );

        pass.setBounds(
                30,
                145,
                100,
                25
        );

        card.add(pass);


        txtPassword =
                new JPasswordField();

        txtPassword.setBounds(
                30,
                170,
                320,
                38
        );

        styleField(txtPassword);

        card.add(txtPassword);


        JButton btnLogin =
                new JButton("LOGIN");

        btnLogin.setBounds(
                30,
                220,
                320,
                40
        );

        btnLogin.setBackground(
                ParkXTheme.BLUE
        );

        btnLogin.setForeground(
                Color.WHITE
        );

        btnLogin.setFont(
                ParkXTheme.normalFont(13)
        );

        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);

        btnLogin.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        card.add(btnLogin);


        btnLogin.addActionListener(
                e -> login()
        );


        txtPassword.addActionListener(
                e -> login()
        );
    }


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
                ParkXTheme.normalFont(13)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                ParkXTheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );
    }


    private void login() {

        String username =
                txtUsername
                        .getText()
                        .trim();

        String password =
                new String(
                        txtPassword
                                .getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            showLoginError(
                    "Missing Information",
                    "Please enter your username and password."
            );

            return;
        }


        boolean valid =
                userDAO.authenticate(
                        username,
                        password
                );


        if (valid) {

            dispose();

            new Dashboard();

        } else {

            showLoginError(
                    "Login Failed",
                    "Incorrect username or password."
            );
        }
    }


    private void showLoginError(
            String titleText,
            String messageText) {

        JDialog dialog =
                new JDialog(
                        this,
                        "ParkX",
                        true
                );

        dialog.setSize(
                400,
                245
        );

        dialog.setLocationRelativeTo(this);

        dialog.setResizable(false);

        dialog.setLayout(null);

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.getContentPane().setBackground(
                ParkXTheme.CARD
        );


        /*
         * Error icon
         */
        JPanel iconPanel =
                new JPanel() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                new Color(
                                        220,
                                        53,
                                        69
                                )
                        );

                        g2.fillOval(
                                0,
                                0,
                                getWidth(),
                                getHeight()
                        );

                        g2.setColor(
                                Color.WHITE
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

                        String text = "!";

                        int x =
                                (getWidth()
                                        - fm.stringWidth(text))
                                        / 2;

                        int y =
                                ((getHeight()
                                        - fm.getHeight())
                                        / 2)
                                        + fm.getAscent();

                        g2.drawString(
                                text,
                                x,
                                y
                        );
                    }
                };


        iconPanel.setOpaque(false);

        iconPanel.setBounds(
                170,
                20,
                60,
                60
        );

        dialog.add(iconPanel);


        /*
         * Error title
         */
        JLabel title =
                new JLabel(
                        titleText,
                        SwingConstants.CENTER
                );

        title.setBounds(
                40,
                90,
                320,
                30
        );

        title.setFont(
                ParkXTheme.titleFont(20)
        );

        title.setForeground(
                ParkXTheme.TEXT
        );

        dialog.add(title);


        /*
         * Error message
         */
        JLabel message =
                new JLabel(
                        messageText,
                        SwingConstants.CENTER
                );

        message.setBounds(
                25,
                122,
                350,
                28
        );

        message.setFont(
                ParkXTheme.normalFont(13)
        );

        message.setForeground(
                ParkXTheme.MUTED
        );

        dialog.add(message);


        /*
         * Try Again button
         */
        JButton btnTryAgain =
                new JButton(
                        "TRY AGAIN"
                );

        btnTryAgain.setBounds(
                110,
                165,
                180,
                40
        );

        btnTryAgain.setBackground(
                ParkXTheme.BLUE
        );

        btnTryAgain.setForeground(
                Color.WHITE
        );

        btnTryAgain.setFont(
                ParkXTheme.normalFont(13)
        );

        btnTryAgain.setFocusPainted(false);

        btnTryAgain.setBorderPainted(false);

        btnTryAgain.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        btnTryAgain.addActionListener(
                e -> {

                    dialog.dispose();

                    txtPassword.setText("");

                    txtUsername.requestFocus();
                }
        );


        dialog.add(btnTryAgain);


        /*
         * ENTER key closes popup
         */
        dialog.getRootPane()
                .setDefaultButton(
                        btnTryAgain
                );


        dialog.setVisible(true);
    }
}