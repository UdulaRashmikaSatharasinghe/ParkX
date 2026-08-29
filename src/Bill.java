import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {

    private static int billNumber = 1;

    public static String generateBill(
            Vehicle vehicle) {

        LocalDateTime exitTime =
                LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy - hh:mm:ss a");

        long totalMinutes =
                Duration.between(
                        vehicle.getEntryTime(),
                        exitTime)
                        .toMinutes();

        if (totalMinutes < 0) {
            totalMinutes = 0;
        }

        long hours =
                totalMinutes / 60;

        long minutes =
                totalMinutes % 60;

        long chargedHours =
                vehicle.getChargedHoursAt(
                        exitTime);

        double total =
                vehicle.calculateFeeAt(
                        exitTime);

        String billId =
                String.format(
                        "PX%04d",
                        billNumber++);

        return
                "========================================\n"
                + "                 PARKX\n"
                + "            PARKING RECEIPT\n"
                + "========================================\n\n"
                + "Receipt ID     : " + billId + "\n"
                + "Vehicle Type   : " + vehicle.getType() + "\n"
                + "Vehicle No     : " + vehicle.getVehicleNumber() + "\n"
                + "Owner Name     : " + vehicle.getOwnerName() + "\n\n"
                + "----------------------------------------\n"
                + "Entry Time     : "
                + vehicle.getEntryTime().format(formatter) + "\n"
                + "Exit Time      : "
                + exitTime.format(formatter) + "\n"
                + "Duration       : "
                + hours + " Hour(s) "
                + minutes + " Minute(s)\n"
                + "Charged Hours  : "
                + chargedHours + "\n"
                + "Rate Per Hour  : Rs. "
                + String.format("%.2f", vehicle.getHourlyRate()) + "\n"
                + "----------------------------------------\n"
                + "TOTAL AMOUNT   : Rs. "
                + String.format("%.2f", total) + "\n\n"
                + "========================================\n"
                + "        Thank you for using ParkX\n"
                + "========================================";
    }

    private Bill() {
    }
}
