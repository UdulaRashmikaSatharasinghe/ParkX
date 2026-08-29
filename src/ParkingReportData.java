import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParkingReportData {
    private final LocalDateTime generatedAt;
    private final LocalDate periodFrom;
    private final LocalDate periodTo;
    private final List<ParkingHistoryRecord> allHistory;
    private final List<ParkingHistoryRecord> periodHistory;
    private final int currentlyParked;
    private final int totalSpaces;
    private final Map<String, Integer> vehicleCounts;
    private final Map<LocalDate, Double> dailyRevenue;
    private final Map<Integer, Double> monthlyRevenue;
    private final Map<Integer, Double> annualRevenue;
    private final Map<Integer, Integer> entryHours;
    private final int uniqueVehicles;
    private final int vehiclesToday;
    private final int vehiclesThisMonth;
    private final int vehiclesThisYear;

    public ParkingReportData(LocalDateTime generatedAt, LocalDate periodFrom,
            LocalDate periodTo, List<ParkingHistoryRecord> allHistory,
            List<ParkingHistoryRecord> periodHistory, int currentlyParked,
            int totalSpaces, Map<String, Integer> vehicleCounts,
            Map<LocalDate, Double> dailyRevenue,
            Map<Integer, Double> monthlyRevenue,
            Map<Integer, Double> annualRevenue, Map<Integer, Integer> entryHours,
            int uniqueVehicles, int vehiclesToday, int vehiclesThisMonth,
            int vehiclesThisYear) {
        this.generatedAt = generatedAt;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.allHistory = Collections.unmodifiableList(allHistory);
        this.periodHistory = Collections.unmodifiableList(periodHistory);
        this.currentlyParked = currentlyParked;
        this.totalSpaces = totalSpaces;
        this.vehicleCounts = Collections.unmodifiableMap(vehicleCounts);
        this.dailyRevenue = Collections.unmodifiableMap(dailyRevenue);
        this.monthlyRevenue = Collections.unmodifiableMap(monthlyRevenue);
        this.annualRevenue = Collections.unmodifiableMap(annualRevenue);
        this.entryHours = Collections.unmodifiableMap(entryHours);
        this.uniqueVehicles = uniqueVehicles;
        this.vehiclesToday = vehiclesToday;
        this.vehiclesThisMonth = vehiclesThisMonth;
        this.vehiclesThisYear = vehiclesThisYear;
    }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public LocalDate getPeriodFrom() { return periodFrom; }
    public LocalDate getPeriodTo() { return periodTo; }
    public List<ParkingHistoryRecord> getAllHistory() { return allHistory; }
    public List<ParkingHistoryRecord> getPeriodHistory() { return periodHistory; }
    public int getCurrentlyParked() { return currentlyParked; }
    public int getTotalSpaces() { return totalSpaces; }
    public Map<String, Integer> getVehicleCounts() { return vehicleCounts; }
    public Map<LocalDate, Double> getDailyRevenue() { return dailyRevenue; }
    public Map<Integer, Double> getMonthlyRevenue() { return monthlyRevenue; }
    public Map<Integer, Double> getAnnualRevenue() { return annualRevenue; }
    public Map<Integer, Integer> getEntryHours() { return entryHours; }
    public int getUniqueVehicles() { return uniqueVehicles; }
    public int getVehiclesToday() { return vehiclesToday; }
    public int getVehiclesThisMonth() { return vehiclesThisMonth; }
    public int getVehiclesThisYear() { return vehiclesThisYear; }
}
