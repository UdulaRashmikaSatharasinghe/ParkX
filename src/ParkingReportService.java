import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParkingReportService {
    private final ParkingHistoryDAO historyDAO = new ParkingHistoryDAO();

    public ParkingReportData build(LocalDate from, LocalDate to) throws Exception {
        List<ParkingHistoryRecord> all = historyDAO.findAll();
        List<ParkingHistoryRecord> period = new ArrayList<>();
        Map<String, Integer> types = new LinkedHashMap<>();
        types.put("Car", 0);
        types.put("Motorcycle", 0);
        types.put("Van", 0);
        types.put("Tricycle", 0);
        Map<LocalDate, Double> daily = new LinkedHashMap<>();
        Map<Integer, Double> monthly = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) monthly.put(month, 0d);
        Map<Integer, Double> annual = new LinkedHashMap<>();
        Map<Integer, Integer> hours = new LinkedHashMap<>();
        Set<String> vehicles = new LinkedHashSet<>();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int vehiclesToday = 0, vehiclesThisMonth = 0, vehiclesThisYear = 0;

        for (ParkingHistoryRecord record : all) {
            LocalDate exit = record.getExitTime().toLocalDate();
            LocalDate entryDate = record.getEntryTime().toLocalDate();
            if (entryDate.equals(today)) vehiclesToday++;
            if (entryDate.getYear() == today.getYear()
                    && entryDate.getMonthValue() == today.getMonthValue()) vehiclesThisMonth++;
            if (entryDate.getYear() == today.getYear()) vehiclesThisYear++;
            vehicles.add(record.getVehicleNumber().toUpperCase());
            annual.merge(exit.getYear(), record.getTotalFee(), Double::sum);
            if (exit.getYear() == currentYear)
                monthly.merge(exit.getMonthValue(), record.getTotalFee(), Double::sum);
            if (!exit.isBefore(from) && !exit.isAfter(to)) {
                period.add(record);
                daily.merge(exit, record.getTotalFee(), Double::sum);
                types.merge(normalizeType(record.getVehicleType()), 1, Integer::sum);
                hours.merge(record.getEntryTime().getHour(), 1, Integer::sum);
            }
        }

        int active = 0;
        int spaces = 0;
        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT vehicle_number, vehicle_type, entry_time FROM active_vehicles");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    active++;
                    vehicles.add(rs.getString("vehicle_number").toUpperCase());
                    LocalDateTime entry = rs.getTimestamp("entry_time").toLocalDateTime();
                    LocalDate entryDate = entry.toLocalDate();
                    if (entryDate.equals(today)) vehiclesToday++;
                    if (entryDate.getYear() == today.getYear()
                            && entryDate.getMonthValue() == today.getMonthValue()) vehiclesThisMonth++;
                    if (entryDate.getYear() == today.getYear()) vehiclesThisYear++;
                    if (!entry.toLocalDate().isBefore(from)
                            && !entry.toLocalDate().isAfter(to)) {
                        types.merge(normalizeType(rs.getString("vehicle_type")), 1, Integer::sum);
                        hours.merge(entry.getHour(), 1, Integer::sum);
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM parking_spaces");
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) spaces = rs.getInt(1);
            }
        }
        return new ParkingReportData(LocalDateTime.now(), from, to, all, period,
                active, spaces, types, daily, monthly, annual, hours, vehicles.size(),
                vehiclesToday, vehiclesThisMonth, vehiclesThisYear);
    }

    private String normalizeType(String type) {
        if (type == null) return "Other";
        for (String known : new String[] {"Car", "Motorcycle", "Van", "Tricycle"})
            if (known.equalsIgnoreCase(type.trim())) return known;
        return "Other";
    }
}
