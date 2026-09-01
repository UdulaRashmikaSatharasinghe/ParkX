import java.time.LocalDateTime;

/*Inheritance 
Reuses common code from Vehicle*/

public class Motorcycle extends Vehicle {

    public Motorcycle(
            String vehicleNumber,
            String ownerName) {

        super(
                vehicleNumber,
                ownerName);
    }

    public Motorcycle(
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
        return "Motorcycle";
    }

    @Override
    public double getHourlyRate() {
        return 50.00;
    }
}
