import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParkingHistoryDAO {

    public List<ParkingHistoryRecord> findByExitDateRange(
            LocalDate from, LocalDate to) throws Exception {

        String sql = "SELECT id, vehicle_number, owner_name, vehicle_type, "
                + "slot_id, entry_time, exit_time, charged_hours, "
                + "hourly_rate, total_fee FROM parking_history "
                + "WHERE exit_time >= ? AND exit_time < ? "
                + "ORDER BY exit_time DESC";

        List<ParkingHistoryRecord> records = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(from.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(to.plusDays(1).atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(new ParkingHistoryRecord(
                            rs.getInt("id"),
                            rs.getString("vehicle_number"),
                            rs.getString("owner_name"),
                            rs.getString("vehicle_type"),
                            rs.getString("slot_id"),
                            rs.getTimestamp("entry_time").toLocalDateTime(),
                            rs.getTimestamp("exit_time").toLocalDateTime(),
                            rs.getLong("charged_hours"),
                            rs.getDouble("hourly_rate"),
                            rs.getDouble("total_fee")));
                }
            }
        }

        return records;
    }

    public void insertHistory(
            Connection con,
            Vehicle vehicle,
            String slotId,
            LocalDateTime exitTime)
            throws Exception {

        String sql =
                "INSERT INTO parking_history "
                + "(vehicle_number, owner_name, vehicle_type, "
                + "slot_id, entry_time, exit_time, "
                + "charged_hours, hourly_rate, total_fee) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {
            ps.setString(
                    1,
                    vehicle.getVehicleNumber());

            ps.setString(
                    2,
                    vehicle.getOwnerName());

            ps.setString(
                    3,
                    vehicle.getType());

            ps.setString(
                    4,
                    slotId);

            ps.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            vehicle.getEntryTime()));

            ps.setTimestamp(
                    6,
                    Timestamp.valueOf(exitTime));

            ps.setLong(
                    7,
                    vehicle.getChargedHoursAt(exitTime));

            ps.setDouble(
                    8,
                    vehicle.getHourlyRate());

            ps.setDouble(
                    9,
                    vehicle.calculateFeeAt(exitTime));

            ps.executeUpdate();
        }
    }
}
