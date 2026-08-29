import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static boolean initializeDatabase() {

        try {
            createDatabase();
            createTables();
            createDefaultUser();
            createParkingSpaces();

            System.out.println("ParkX MySQL database is ready.");
            return true;

        } catch (SQLException e) {
            System.out.println("Database initialization failed.");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void createDatabase() throws SQLException {

        String sql =
                "CREATE DATABASE IF NOT EXISTS "
                + DatabaseConnection.DATABASE_NAME;

        try (
                Connection con =
                        DatabaseConnection.getServerConnection();

                Statement st =
                        con.createStatement()
        ) {
            st.executeUpdate(sql);
        }
    }

    private static void createTables() throws SQLException {

        String users =
                "CREATE TABLE IF NOT EXISTS users ("
                + "username VARCHAR(50) PRIMARY KEY, "
                + "password VARCHAR(100) NOT NULL"
                + ")";

        String spaces =
                "CREATE TABLE IF NOT EXISTS parking_spaces ("
                + "slot_id VARCHAR(10) PRIMARY KEY, "
                + "floor_name VARCHAR(50) NOT NULL, "
                + "status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', "
                + "vehicle_number VARCHAR(20) NULL"
                + ")";

        String active =
                "CREATE TABLE IF NOT EXISTS active_vehicles ("
                + "vehicle_number VARCHAR(20) PRIMARY KEY, "
                + "owner_name VARCHAR(100) NOT NULL, "
                + "vehicle_type VARCHAR(30) NOT NULL, "
                + "slot_id VARCHAR(10) NOT NULL UNIQUE, "
                + "entry_time DATETIME NOT NULL"
                + ")";

        String history =
                "CREATE TABLE IF NOT EXISTS parking_history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "vehicle_number VARCHAR(20) NOT NULL, "
                + "owner_name VARCHAR(100) NOT NULL, "
                + "vehicle_type VARCHAR(30) NOT NULL, "
                + "slot_id VARCHAR(10) NOT NULL, "
                + "entry_time DATETIME NOT NULL, "
                + "exit_time DATETIME NOT NULL, "
                + "charged_hours BIGINT NOT NULL, "
                + "hourly_rate DOUBLE NOT NULL, "
                + "total_fee DOUBLE NOT NULL"
                + ")";

        try (
                Connection con = DatabaseConnection.getConnection();
                Statement st = con.createStatement()
        ) {
            st.executeUpdate(users);
            st.executeUpdate(spaces);
            st.executeUpdate(active);
            st.executeUpdate(history);
        }
    }

    private static void createDefaultUser() throws SQLException {

        String sql =
                "INSERT IGNORE INTO users "
                + "(username, password) "
                + "VALUES (?, ?)";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, "admin");
            ps.setString(2, "1234");
            ps.executeUpdate();
        }
    }

    private static void createParkingSpaces() throws SQLException {
        insertFloor("G", "Ground Floor");
        insertFloor("F", "First Floor");
        insertFloor("S", "Second Floor");
        insertFloor("T", "Third Floor");
    }

    private static void insertFloor(
            String prefix,
            String floorName)
            throws SQLException {

        String sql =
                "INSERT IGNORE INTO parking_spaces "
                + "(slot_id, floor_name, status, vehicle_number) "
                + "VALUES (?, ?, 'AVAILABLE', NULL)";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 20; i++) {
                ps.setString(1, prefix + i);
                ps.setString(2, floorName);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private DatabaseInitializer() {
    }
}
