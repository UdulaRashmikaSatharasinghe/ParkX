import java.time.LocalDateTime;

/*Inheritance 
Reuses common code from Vehicle*/

public class Van extends Vehicle {

    public Van(
            String vehicleNumber,
            String ownerName) {

        super(
                vehicleNumber,
                ownerName);
    }

    public Van(
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
        return "Van";
    }

    @Override
    public double getHourlyRate() {
        return 150.00;
    }
}
