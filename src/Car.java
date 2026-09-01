import java.time.LocalDateTime;

/*Inheritance 
 Reuses common code from Vehicle*/

public class Car extends Vehicle {

    public Car(
            String vehicleNumber,
            String ownerName) {

        super(
                vehicleNumber,
                ownerName);
    }

    public Car(
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
        return "Car";
    }

    @Override
    public double getHourlyRate() {
        return 100.00;
    }
}
