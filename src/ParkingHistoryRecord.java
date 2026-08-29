import java.time.LocalDateTime;

public class ParkingHistoryRecord {

    private final int id;
    private final String vehicleNumber;
    private final String ownerName;
    private final String vehicleType;
    private final String slotId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final long chargedHours;
    private final double hourlyRate;
    private final double totalFee;

    public ParkingHistoryRecord(int id, String vehicleNumber, String ownerName,
            String vehicleType, String slotId, LocalDateTime entryTime,
            LocalDateTime exitTime, long chargedHours, double hourlyRate,
            double totalFee) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.slotId = slotId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.chargedHours = chargedHours;
        this.hourlyRate = hourlyRate;
        this.totalFee = totalFee;
    }

    public int getId() { return id; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getOwnerName() { return ownerName; }
    public String getVehicleType() { return vehicleType; }
    public String getSlotId() { return slotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public long getChargedHours() { return chargedHours; }
    public double getHourlyRate() { return hourlyRate; }
    public double getTotalFee() { return totalFee; }
}
