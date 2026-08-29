import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ParkingManager {

    private ArrayList<Vehicle> vehicles;
    private ArrayList<ParkingSpace> parkingSpaces;

    private VehicleDAO vehicleDAO;
    private ParkingSpaceDAO parkingSpaceDAO;
    private ParkingHistoryDAO historyDAO;

    public ParkingManager() {

        vehicles = new ArrayList<>();
        parkingSpaces = new ArrayList<>();

        vehicleDAO = new VehicleDAO();
        parkingSpaceDAO = new ParkingSpaceDAO();
        historyDAO = new ParkingHistoryDAO();

        createParkingSpaces();
        loadActiveVehiclesFromDatabase();
    }

    private void createParkingSpaces() {

        createFloor("G", "Ground Floor");
        createFloor("F", "First Floor");
        createFloor("S", "Second Floor");
        createFloor("T", "Third Floor");
    }

    private void createFloor(
            String prefix,
            String floorName) {

        for (int i = 1; i <= 20; i++) {
            parkingSpaces.add(
                    new ParkingSpace(
                            prefix + i,
                            floorName));
        }
    }

    private void loadActiveVehiclesFromDatabase() {

        ArrayList<VehicleDAO.LoadedVehicleRecord>
                records =
                vehicleDAO.loadAllActiveVehicles();

        for (VehicleDAO.LoadedVehicleRecord record :
                records) {

            Vehicle vehicle =
                    record.getVehicle();

            ParkingSpace space =
                    getParkingSpace(
                            record.getSlotId());

            if (vehicle != null
                    && space != null
                    && !space.isOccupied()) {

                vehicles.add(vehicle);
                space.occupy(vehicle);
            }
        }
    }

    public boolean parkVehicle(
            Vehicle vehicle,
            String slotId) {

        if (vehicle == null) {
            return false;
        }

        if (searchVehicle(
                vehicle.getVehicleNumber())
                != null) {

            return false;
        }

        ParkingSpace space =
                getParkingSpace(slotId);

        if (space == null
                || space.isOccupied()) {

            return false;
        }

        try (
                Connection con =
                        DatabaseConnection.getConnection()
        ) {
            try {
                con.setAutoCommit(false);

                vehicleDAO.insertActiveVehicle(
                        con,
                        vehicle,
                        slotId);

                parkingSpaceDAO.occupySpace(
                        con,
                        slotId,
                        vehicle.getVehicleNumber());

                con.commit();

                vehicles.add(vehicle);
                space.occupy(vehicle);

                return true;

            } catch (Exception e) {
                con.rollback();

                System.out.println(
                        "Park vehicle failed: "
                        + e.getMessage());

                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println(
                    "Database connection error: "
                    + e.getMessage());

            return false;
        }
    }

    public Vehicle removeVehicle(
            String vehicleNumber) {

        Vehicle vehicle =
                searchVehicle(vehicleNumber);

        if (vehicle == null) {
            return null;
        }

        String slotId =
                getVehicleSlot(vehicleNumber);

        LocalDateTime exitTime =
                LocalDateTime.now();

        try (
                Connection con =
                        DatabaseConnection.getConnection()
        ) {
            try {
                con.setAutoCommit(false);

                historyDAO.insertHistory(
                        con,
                        vehicle,
                        slotId,
                        exitTime);

                vehicleDAO.deleteActiveVehicle(
                        con,
                        vehicleNumber);

                parkingSpaceDAO.releaseSpace(
                        con,
                        slotId);

                con.commit();

                ParkingSpace space =
                        getParkingSpace(slotId);

                if (space != null) {
                    space.release();
                }

                vehicles.remove(vehicle);

                return vehicle;

            } catch (Exception e) {
                con.rollback();

                System.out.println(
                        "Remove vehicle failed: "
                        + e.getMessage());

                return null;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println(
                    "Database connection error: "
                    + e.getMessage());

            return null;
        }
    }

    public Vehicle searchVehicle(
            String vehicleNumber) {

        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleNumber()
                    .equalsIgnoreCase(vehicleNumber)) {

                return vehicle;
            }
        }

        return null;
    }

    public ParkingSpace getParkingSpace(
            String slotId) {

        for (ParkingSpace space :
                parkingSpaces) {

            if (space.getSlotId()
                    .equalsIgnoreCase(slotId)) {

                return space;
            }
        }

        return null;
    }

    public String getVehicleSlot(
            String vehicleNumber) {

        for (ParkingSpace space :
                parkingSpaces) {

            if (space.isOccupied()
                    && space.getVehicle() != null
                    && space.getVehicle()
                            .getVehicleNumber()
                            .equalsIgnoreCase(vehicleNumber)) {

                return space.getSlotId();
            }
        }

        return "-";
    }

    public int getAvailableSlots() {

        int count = 0;

        for (ParkingSpace space :
                parkingSpaces) {

            if (!space.isOccupied()) {
                count++;
            }
        }

        return count;
    }

    public int getOccupiedSlots() {
        return parkingSpaces.size()
                - getAvailableSlots();
    }

    public int getFloorOccupiedCount(
            String floor) {

        int count = 0;

        for (ParkingSpace space :
                parkingSpaces) {

            if (space.getFloor()
                    .equals(floor)
                    && space.isOccupied()) {

                count++;
            }
        }

        return count;
    }

    public int getVehicleTypeCount(
            String type) {

        int count = 0;

        for (Vehicle vehicle :
                vehicles) {

            if (vehicle.getType()
                    .equalsIgnoreCase(type)) {

                count++;
            }
        }

        return count;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
}
