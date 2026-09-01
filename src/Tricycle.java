import java.time.LocalDateTime;

/*Inheritance 
Reuses common code from Vehicle*/

public class Tricycle extends Vehicle {

    public Tricycle(
            String vehicleNumber,
            String ownerName) {

        super(
                vehicleNumber,
                ownerName);
    }

    public Tricycle(
            String vehicleNumber,
            String ownerName,
            LocalDateTime entryTime) {

        super(
                vehicleNumber,
                ownerName,
                entryTime);
    }

    @Override
    public String getType() {
        return "Tricycle";
    }

    @Override
    public double getHourlyRate() {
        return 40.00;
    }
}
