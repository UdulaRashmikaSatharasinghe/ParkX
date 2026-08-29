import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());

        } catch (Exception e) {

            e.printStackTrace();
        }

        boolean ready =
                DatabaseInitializer.initializeDatabase();

        if (!ready) {

            System.out.println(
                    "ParkX database initialization failed.");

            return;
        }

        SwingUtilities.invokeLater(
                () -> new LoginForm());
    }
}