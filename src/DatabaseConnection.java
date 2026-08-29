import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String DATABASE_NAME = "parkx_db";

    public static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC";

    public static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/"
            + DATABASE_NAME
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC";

    private static final String USER =
            System.getenv().getOrDefault("PARKX_DB_USER", "root");

    private static final String PASSWORD =
            System.getenv().getOrDefault("PARKX_DB_PASSWORD", "");

    public static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(
                SERVER_URL,
                USER,
                PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE_URL,
                USER,
                PASSWORD);
    }

    private DatabaseConnection() {
    }
}
