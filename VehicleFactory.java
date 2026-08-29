import java.time.LocalDateTime;

public class VehicleFactory {

    public static Vehicle createVehicle(
            String type,
            String vehicleNumber,
            String ownerName) {

        return createVehicle(
                type,
                vehicleNumber,
                ownerName,
                LocalDateTime.now());
    }

    public static Vehicle createVehicle(
            String type,
            String vehicleNumber,
            String ownerName,
            LocalDateTime entryTime) {

        if (type == null) {
            return null;
        }

        switch (type) {

            case "Car":
                return new Car(
                        vehicleNumber,
                        ownerName,
                        entryTime);

            case "Motorcycle":
                return new Motorcycle(
                        vehicleNumber,
                        ownerName,
                        entryTime);

            case "Van":
                return new Van(
                        vehicleNumber,
                        ownerName,
                        entryTime);

            case "Tricycle":
                return new Tricycle(
                        vehicleNumber,
                        ownerName,
                        entryTime);

            default:
                return null;
        }
    }

    private VehicleFactory() {
    }
}
