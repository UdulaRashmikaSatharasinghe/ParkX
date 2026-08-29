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
                ParkXTheme.BACKGROUND);

        createInterface();

        setVisible(true);
    }

    private void createInterface() {

        JLabel logo =
                new JLabel("ParkX");

        logo.setFont(
                ParkXTheme.titleFont(34));

        logo.setForeground(
                ParkXTheme.AMBER);

        logo.setHorizontalAlignment(
                SwingConstants.CENTER);

        logo.setBounds(
                110,
                35,
                300,
                45);

        add(logo);


        JLabel subtitle =
                new JLabel(
                        "Smart Parking Management");

        subtitle.setFont(
                ParkXTheme.normalFont(13));

        subtitle.setForeground(
                ParkXTheme.MUTED);

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER);

        subtitle.setBounds(
                110,
                78,
                300,
                25);

        add(subtitle);


        JPanel card =
                new JPanel();

        card.setLayout(null);
        card.setBackground(
                ParkXTheme.CARD);

        card.setBounds(
                70,
                130,
                380,
                275);

        card.setBorder(
                BorderFactory.createLineBorder(
                        ParkXTheme.BORDER));

        add(card);


        JLabel loginTitle =
                new JLabel(
                        "Welcome Back");

        loginTitle.setFont(
                ParkXTheme.titleFont(21));

        loginTitle.setForeground(
                ParkXTheme.TEXT);

        loginTitle.setBounds(
                30,
                20,
                250,
                30);

        card.add(loginTitle);


        JLabel user =
                new JLabel("Username");

        user.setForeground(
                ParkXTheme.MUTED);

        user.setBounds(
                30,
                70,
                100,
                25);

        card.add(user);


        txtUsername =
                new JTextField();

        txtUsername.setBounds(
                30,
                95,
                320,
                38);

        styleField(txtUsername);

        card.add(txtUsername);


        JLabel pass =
                new JLabel("Password");

        pass.setForeground(
                ParkXTheme.MUTED);

        pass.setBounds(
                30,
                145,
                100,
                25);

        card.add(pass);


        txtPassword =
                new JPasswordField();

        txtPassword.setBounds(
                30,
                170,
                320,
                38);

        styleField(txtPassword);

        card.add(txtPassword);


        JButton btnLogin =
                new JButton("LOGIN");

        btnLogin.setBounds(
                30,
                220,
                320,
                40);

        btnLogin.setBackground(
                ParkXTheme.BLUE);

        btnLogin.setForeground(
                Color.WHITE);

        btnLogin.setFocusPainted(false);

        card.add(btnLogin);

        btnLogin.addActionListener(
                e -> login());
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
                                10,
                                5,
                                10)
                ));
    }

    private void login() {

        String username =
                txtUsername
                        .getText()
                        .trim();

        String password =
                new String(
                        txtPassword
                                .getPassword());

        boolean valid =
                userDAO.authenticate(
                        username,
                        password);

        if (valid) {
            dispose();
            new Dashboard();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
