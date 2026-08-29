import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ParkingHistoryDAO {

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
