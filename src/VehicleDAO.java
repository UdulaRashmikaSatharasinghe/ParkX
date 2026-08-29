import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class VehicleDAO {

    public void insertActiveVehicle(
            Connection con,
            Vehicle vehicle,
            String slotId)
            throws Exception {

        String sql =
                "INSERT INTO active_vehicles "
                + "(vehicle_number, owner_name, vehicle_type, "
                + "slot_id, entry_time) "
                + "VALUES (?, ?, ?, ?, ?)";

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

            ps.executeUpdate();
        }
    }

    public void deleteActiveVehicle(
            Connection con,
            String vehicleNumber)
            throws Exception {

        String sql =
                "DELETE FROM active_vehicles "
                + "WHERE vehicle_number = ?";

        try (
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {
            ps.setString(
                    1,
                    vehicleNumber);

            ps.executeUpdate();
        }
    }

    public ArrayList<LoadedVehicleRecord>
    loadAllActiveVehicles() {

        ArrayList<LoadedVehicleRecord> records =
                new ArrayList<>();

        String sql =
                "SELECT vehicle_number, owner_name, "
                + "vehicle_type, slot_id, entry_time "
                + "FROM active_vehicles "
                + "ORDER BY entry_time ASC";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {
            while (rs.next()) {

                Vehicle vehicle =
                        VehicleFactory.createVehicle(
                                rs.getString("vehicle_type"),
                                rs.getString("vehicle_number"),
                                rs.getString("owner_name"),
                                rs.getTimestamp("entry_time")
                                        .toLocalDateTime());

                if (vehicle != null) {
                    records.add(
                            new LoadedVehicleRecord(
                                    vehicle,
                                    rs.getString("slot_id")));
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "Unable to load active vehicles: "
                    + e.getMessage());
        }

        return records;
    }

    public static class LoadedVehicleRecord {

        private Vehicle vehicle;
        private String slotId;

        public LoadedVehicleRecord(
                Vehicle vehicle,
                String slotId) {

            this.vehicle = vehicle;
            this.slotId = slotId;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public String getSlotId() {
            return slotId;
        }
    }
}
