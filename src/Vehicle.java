import java.time.Duration;
import java.time.LocalDateTime;

/*Abstraction
*Defines common vehicle behavior
without creating a generic vehicle */

public abstract class Vehicle {
	

/*Encapsulation
Protects data from direct modification*/

    private String vehicleNumber;
    private String ownerName;
    private LocalDateTime entryTime;

    public Vehicle(
            String vehicleNumber,
            String ownerName) {

        this(
                vehicleNumber,
                ownerName,
                LocalDateTime.now());
    }

    public Vehicle(
            String vehicleNumber,
            String ownerName,
            LocalDateTime entryTime) {

        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.entryTime = entryTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public abstract String getType();

    public abstract double getHourlyRate();

    public long getParkedMinutes() {
        return getParkedMinutesAt(LocalDateTime.now());
    }

    public long getParkedMinutesAt(
            LocalDateTime endTime) {

        long minutes =
                Duration.between(
                        entryTime,
                        endTime)
                        .toMinutes();

        if (minutes < 0) {
            return 0;
        }

        return minutes;
    }

    public long getChargedHours() {
        return getChargedHoursAt(LocalDateTime.now());
    }

    public long getChargedHoursAt(
            LocalDateTime endTime) {

        long minutes =
                getParkedMinutesAt(endTime);

        if (minutes <= 0) {
            return 1;
        }

        return (long)
                Math.ceil(
                        minutes / 60.0);
    }

    public double calculateFee() {
        return calculateFeeAt(LocalDateTime.now());
    }

    public double calculateFeeAt(
            LocalDateTime endTime) {

        return getChargedHoursAt(endTime)
                * getHourlyRate();
    }
}
