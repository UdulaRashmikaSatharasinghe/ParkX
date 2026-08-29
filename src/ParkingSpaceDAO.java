import java.sql.Connection;
import java.sql.PreparedStatement;

public class ParkingSpaceDAO {

    public void occupySpace(
            Connection con,
            String slotId,
            String vehicleNumber)
            throws Exception {

        String sql =
                "UPDATE parking_spaces "
                + "SET status = 'OCCUPIED', "
                + "vehicle_number = ? "
                + "WHERE slot_id = ?";

        try (
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {
            ps.setString(1, vehicleNumber);
            ps.setString(2, slotId);
            ps.executeUpdate();
        }
    }

    public void releaseSpace(
            Connection con,
            String slotId)
            throws Exception {

        String sql =
                "UPDATE parking_spaces "
                + "SET status = 'AVAILABLE', "
                + "vehicle_number = NULL "
                + "WHERE slot_id = ?";

        try (
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {
            ps.setString(1, slotId);
            ps.executeUpdate();
        }
    }
}
