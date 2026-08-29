import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class ParkingReportPdfExporter {
    private static final int W = 1240;
    private static final int H = 1754;
    private static final Color NAVY = new Color(8, 20, 38);
    private static final Color CARD = new Color(18, 38, 64);
    private static final Color CARD_2 = new Color(23, 47, 77);
    private static final Color TEXT = new Color(241, 245, 249);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color AMBER = new Color(245, 180, 55);
    private static final Color PURPLE = new Color(168, 85, 247);
    private static final Color RED = new Color(239, 68, 68);
    private static final Color[] SERIES = {BLUE, PURPLE, GREEN, AMBER, RED};
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public void export(ParkingReportData data, Path target) throws IOException {
        List<BufferedImage> pages = new ArrayList<>();
        pages.add(overviewPage(data));
        pages.add(trendsPage(data));
        pages.add(performancePage(data));
        addHistoryPages(data, pages);
        writePdf(pages, target);
    }

    private BufferedImage overviewPage(ParkingReportData d) {
        Canvas c = page(d, "EXECUTIVE SUMMARY", 1);
        LocalDate today = d.getGeneratedAt().toLocalDate();
        double totalRevenue = sum(d.getAllHistory());

        section(c, "Parking Overview", "Live facility and all-time parking indicators", 95);
        metric(c, 55, 155, 265, 125, "TOTAL VEHICLES", integer(d.getUniqueVehicles()), "Unique registered vehicles", BLUE);
        metric(c, 340, 155, 265, 125, "PARKED TODAY", integer(d.getVehiclesToday()), "Entries today", GREEN);
        metric(c, 625, 155, 265, 125, "THIS MONTH", integer(d.getVehiclesThisMonth()), "Parking entries", PURPLE);
        metric(c, 910, 155, 275, 125, "THIS YEAR", integer(d.getVehiclesThisYear()), "Parking entries", AMBER);
        metric(c, 55, 300, 265, 125, "TOTAL SESSIONS", integer(d.getAllHistory().size() + d.getCurrentlyParked()), "Completed + active", BLUE);
        metric(c, 340, 300, 265, 125, "CURRENTLY PARKED", integer(d.getCurrentlyParked()), "Live occupancy", RED);
        metric(c, 625, 300, 265, 125, "COMPLETED", integer(d.getAllHistory().size()), "All recorded history", GREEN);
        metric(c, 910, 300, 275, 125, "TOTAL REVENUE", money(totalRevenue), "All completed sessions", AMBER);

        section(c, "Vehicle Statistics", "Distribution for the selected reporting period", 465);
        panel(c, 55, 525, 550, 415);
        drawDonut(c, d.getVehicleCounts(), 95, 570, 300, 300);
        int typeTotal = d.getVehicleCounts().values().stream().mapToInt(Integer::intValue).sum();
        int ly = 575;
        int i = 0;
        for (Map.Entry<String, Integer> e : d.getVehicleCounts().entrySet()) {
            if (e.getValue() == 0 && "Other".equals(e.getKey())) continue;
            c.g.setColor(SERIES[i % SERIES.length]); c.g.fillRoundRect(420, ly + 4, 16, 16, 5, 5);
            c.text(e.getKey(), 450, ly + 18, 18, Font.BOLD, TEXT);
            double pct = typeTotal == 0 ? 0 : e.getValue() * 100d / typeTotal;
            c.text(String.format("%,d  •  %.1f%%", e.getValue(), pct), 450, ly + 45, 15, Font.PLAIN, MUTED);
            ly += 70; i++;
        }
        String popular = maxKey(d.getVehicleCounts(), "No data");
        c.text("MOST POPULAR", 95, 905, 13, Font.BOLD, MUTED);
        c.text(popular, 240, 905, 16, Font.BOLD, BLUE);

        panel(c, 625, 525, 560, 415);
        c.text("Revenue Overview", 665, 570, 25, Font.BOLD, TEXT);
        c.text("Automatically calculated from billing history", 665, 600, 15, Font.PLAIN, MUTED);
        double todayIncome = revenueOn(d.getAllHistory(), today);
        double monthIncome = d.getMonthlyRevenue().getOrDefault(today.getMonthValue(), 0d);
        double yearIncome = d.getAnnualRevenue().getOrDefault(today.getYear(), 0d);
        revenueRow(c, "Today's Parking Income", todayIncome, 665, 655, GREEN);
        revenueRow(c, "Current Month Income", monthIncome, 665, 720, BLUE);
        revenueRow(c, "Current Year Income", yearIncome, 665, 785, PURPLE);
        revenueRow(c, "Total Parking Income", totalRevenue, 665, 850, AMBER);

        section(c, "Reporting Snapshot", "Selected-period totals for quick management review", 980);
        double periodRevenue = sum(d.getPeriodHistory());
        metric(c, 55, 1040, 360, 125, "PERIOD SESSIONS", integer(d.getPeriodHistory().size()), "Completed in selected range", BLUE);
        metric(c, 440, 1040, 360, 125, "PERIOD REVENUE", money(periodRevenue), "Collected in selected range", GREEN);
        metric(c, 825, 1040, 360, 125, "AVG. SESSION VALUE", money(avg(periodRevenue, d.getPeriodHistory().size())), "Revenue per completed session", PURPLE);
        note(c, 55, 1210, 1130, 165, "REPORT NOTES",
                "Revenue recognizes completed parking sessions on their exit date. Vehicle distribution and performance analytics use the selected reporting period. Current occupancy is a live snapshot captured when this report was generated.");
        footer(c, 1);
        return c.finish();
    }

    private BufferedImage trendsPage(ParkingReportData d) {
        Canvas c = page(d, "REVENUE TRENDS", 2);
        section(c, "Daily Income", "Revenue by day within the selected period", 95);
        panel(c, 55, 155, 1130, 390);
        LocalDate dailyStart = d.getPeriodFrom().isAfter(d.getPeriodTo().minusDays(30))
                ? d.getPeriodFrom() : d.getPeriodTo().minusDays(30);
        List<String> dailyLabels = new ArrayList<>(); List<Double> dailyValues = new ArrayList<>();
        for (LocalDate day = dailyStart; !day.isAfter(d.getPeriodTo()); day = day.plusDays(1)) {
            dailyLabels.add(day.format(DateTimeFormatter.ofPattern("dd MMM")));
            dailyValues.add(d.getDailyRevenue().getOrDefault(day, 0d));
        }
        barChart(c, dailyLabels, dailyValues, 95, 200, 1050, 295, BLUE);

        section(c, "Monthly Income", "January to December • " + d.getGeneratedAt().getYear(), 590);
        panel(c, 55, 650, 1130, 390);
        List<String> months = new ArrayList<>(); List<Double> monthValues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(java.time.Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            monthValues.add(d.getMonthlyRevenue().getOrDefault(month, 0d));
        }
        lineChart(c, months, monthValues, 95, 695, 1050, 295, PURPLE);

        section(c, "Annual Income", "Long-term revenue performance by year", 1085);
        panel(c, 55, 1145, 1130, 390);
        List<Integer> years = new ArrayList<>(d.getAnnualRevenue().keySet());
        years.sort(Integer::compareTo);
        barChart(c, years.stream().map(String::valueOf).collect(Collectors.toList()),
                years.stream().map(d.getAnnualRevenue()::get).collect(Collectors.toList()),
                95, 1190, 1050, 295, GREEN);
        footer(c, 2);
        return c.finish();
    }

    private BufferedImage performancePage(ParkingReportData d) {
        Canvas c = page(d, "PARKING PERFORMANCE", 3);
        List<ParkingHistoryRecord> records = d.getPeriodHistory();
        Map<LocalDate, Long> sessionsByDay = records.stream().collect(Collectors.groupingBy(
                r -> r.getEntryTime().toLocalDate(), LinkedHashMap::new, Collectors.counting()));
        String busiest = sessionsByDay.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().format(DATE) + " (" + e.getValue() + ")").orElse("No data");
        Map.Entry<LocalDate, Double> highest = d.getDailyRevenue().entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);
        String highestDay = highest == null ? "No data" : highest.getKey().format(DATE) + " • " + money(highest.getValue());
        double avgHours = records.stream().mapToLong(r -> Math.max(0,
                Duration.between(r.getEntryTime(), r.getExitTime()).toMinutes())).average().orElse(0) / 60d;
        int peakHour = d.getEntryHours().entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(-1);
        String peak = peakHour < 0 ? "No data" : String.format("%02d:00 – %02d:00", peakHour, (peakHour + 1) % 24);
        double utilization = d.getTotalSpaces() == 0 ? 0 : d.getCurrentlyParked() * 100d / d.getTotalSpaces();

        section(c, "Operational Highlights", "Performance indicators from the selected period and live facility state", 95);
        metric(c, 55, 155, 360, 135, "BUSIEST PARKING DAY", busiest, "Entries recorded on the day", BLUE);
        metric(c, 440, 155, 360, 135, "HIGHEST REVENUE DAY", highestDay, "Completed-session revenue", GREEN);
        metric(c, 825, 155, 360, 135, "MOST POPULAR VEHICLE", maxKey(d.getVehicleCounts(), "No data"), "Selected-period distribution", PURPLE);
        metric(c, 55, 315, 360, 135, "AVERAGE DURATION", String.format("%.1f hours", avgHours), "Entry to exit", AMBER);
        metric(c, 440, 315, 360, 135, "AVG. REVENUE / SESSION", money(avg(sum(records), records.size())), "Selected-period average", GREEN);
        metric(c, 825, 315, 360, 135, "PEAK PARKING PERIOD", peak, "Most frequent entry hour", RED);

        section(c, "Parking Space Utilization", "Live occupancy at report generation time", 500);
        panel(c, 55, 560, 1130, 255);
        c.text(String.format("%.1f%%", utilization), 95, 635, 48, Font.BOLD, utilization > 85 ? RED : BLUE);
        c.text(d.getCurrentlyParked() + " occupied of " + d.getTotalSpaces() + " total spaces", 95, 680, 18, Font.PLAIN, MUTED);
        c.g.setColor(new Color(40, 63, 91)); c.g.fillRoundRect(95, 720, 1050, 28, 20, 20);
        c.g.setColor(utilization > 85 ? RED : BLUE);
        c.g.fillRoundRect(95, 720, (int) (1050 * Math.min(100, utilization) / 100), 28, 20, 20);
        c.text(utilization < 60 ? "Healthy capacity available" : utilization < 85 ? "Moderate facility usage" : "High occupancy — monitor capacity",
                95, 785, 16, Font.BOLD, TEXT);

        section(c, "Peak Entry Periods", "Parking arrivals grouped by hour", 860);
        panel(c, 55, 920, 1130, 430);
        List<String> labels = new ArrayList<>(); List<Double> values = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            labels.add(String.format("%02d", hour));
            values.add(d.getEntryHours().getOrDefault(hour, 0).doubleValue());
        }
        barChart(c, labels, values, 95, 965, 1050, 325, AMBER);
        note(c, 55, 1395, 1130, 115, "INTERPRETATION",
                "Utilization is a point-in-time occupancy percentage. Peak period is based on session entry times; completed sessions and currently active entries in the reporting period are included where applicable.");
        footer(c, 3);
        return c.finish();
    }

    private void addHistoryPages(ParkingReportData d, List<BufferedImage> pages) {
        List<ParkingHistoryRecord> rows = new ArrayList<>(d.getPeriodHistory());
        rows.sort(Comparator.comparing(ParkingHistoryRecord::getExitTime).reversed());
        int perPage = 22;
        int count = Math.max(1, (rows.size() + perPage - 1) / perPage);
        for (int p = 0; p < count; p++) {
            int pageNo = pages.size() + 1;
            Canvas c = page(d, "PARKING HISTORY", pageNo);
            section(c, "Completed Parking Records", "Detailed billing history for the selected reporting period", 95);
            int y = 170;
            String[] heads = {"Receipt", "Vehicle", "Owner", "Type", "Slot", "Entry", "Exit", "Hours", "Amount (LKR)"};
            int[] xs = {55, 130, 240, 375, 475, 535, 710, 885, 955};
            int[] ws = {75, 110, 135, 100, 60, 175, 175, 70, 230};
            c.g.setColor(BLUE); c.g.fillRoundRect(55, y, 1130, 48, 12, 12);
            for (int i = 0; i < heads.length; i++) c.text(heads[i], xs[i] + 10, y + 30, 13, Font.BOLD, Color.WHITE);
            int start = p * perPage;
            int end = Math.min(rows.size(), start + perPage);
            y += 58;
            for (int i = start; i < end; i++) {
                ParkingHistoryRecord r = rows.get(i);
                c.g.setColor((i - start) % 2 == 0 ? CARD : CARD_2);
                c.g.fillRoundRect(55, y, 1130, 55, 8, 8);
                String[] cells = {"PX-" + r.getId(), r.getVehicleNumber(), r.getOwnerName(), r.getVehicleType(), r.getSlotId(),
                        r.getEntryTime().format(DateTimeFormatter.ofPattern("dd MMM yy HH:mm")),
                        r.getExitTime().format(DateTimeFormatter.ofPattern("dd MMM yy HH:mm")),
                        String.valueOf(r.getChargedHours()), String.format("%,.2f", r.getTotalFee())};
                for (int j = 0; j < cells.length; j++) {
                    boolean amount = j == cells.length - 1;
                    c.fitText(cells[j], xs[j] + 10, y + 34, ws[j] - 20, 13,
                            amount ? Font.BOLD : Font.PLAIN, amount ? GREEN : TEXT);
                }
                y += 62;
            }
            if (rows.isEmpty()) c.center("No completed parking records in this reporting period.", 620, 420, 20, Font.PLAIN, MUTED);
            c.text("Records " + (rows.isEmpty() ? "0" : (start + 1) + "–" + end) + " of " + rows.size(), 55, 1590, 14, Font.PLAIN, MUTED);
            c.text("Period total: " + money(sum(rows)), 1185, 1590, 16, Font.BOLD, GREEN, true);
            footer(c, pageNo);
            pages.add(c.finish());
        }
    }

    private Canvas page(ParkingReportData d, String label, int pageNo) {
        Canvas c = new Canvas();
        c.g.setColor(NAVY); c.g.fillRect(0, 0, W, H);
        c.g.setColor(new Color(6, 16, 31)); c.g.fillRect(0, 0, W, 78);
        c.text("ParkX", 55, 48, 30, Font.BOLD, AMBER);
        c.text("Parking Management System", 175, 47, 16, Font.PLAIN, MUTED);
        c.text("PARKING HISTORY & ANALYTICS REPORT", 1185, 32, 13, Font.BOLD, TEXT, true);
        c.text(label, 1185, 55, 12, Font.PLAIN, MUTED, true);
        c.text("Generated: " + d.getGeneratedAt().format(DATE_TIME), 55, 1690, 12, Font.PLAIN, MUTED);
        c.text("Reporting period: " + d.getPeriodFrom().format(DATE) + " – " + d.getPeriodTo().format(DATE), 1185, 1690, 12, Font.PLAIN, MUTED, true);
        return c;
    }

    private void section(Canvas c, String title, String subtitle, int y) {
        c.text(title, 55, y + 25, 26, Font.BOLD, TEXT);
        c.text(subtitle, 55, y + 51, 14, Font.PLAIN, MUTED);
    }

    private void metric(Canvas c, int x, int y, int w, int h, String title, String value, String desc, Color accent) {
        panel(c, x, y, w, h); c.g.setColor(accent); c.g.fillRoundRect(x, y, 7, h, 8, 8);
        c.g.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45)); c.g.fillOval(x + w - 58, y + 18, 32, 32);
        c.text(title, x + 24, y + 28, 12, Font.BOLD, MUTED);
        c.fitText(value, x + 24, y + 70, w - 48, 25, Font.BOLD, accent);
        c.fitText(desc, x + 24, y + 102, w - 48, 13, Font.PLAIN, MUTED);
    }

    private void revenueRow(Canvas c, String label, double value, int x, int y, Color color) {
        c.g.setColor(CARD_2); c.g.fillRoundRect(x, y - 28, 480, 52, 12, 12);
        c.g.setColor(color); c.g.fillOval(x + 15, y - 11, 12, 12);
        c.text(label, x + 40, y, 15, Font.PLAIN, TEXT);
        c.text(money(value), x + 460, y, 16, Font.BOLD, color, true);
    }

    private void panel(Canvas c, int x, int y, int w, int h) {
        c.g.setColor(CARD); c.g.fillRoundRect(x, y, w, h, 22, 22);
        c.g.setColor(new Color(40, 63, 91)); c.g.setStroke(new BasicStroke(1)); c.g.drawRoundRect(x, y, w, h, 22, 22);
    }

    private void note(Canvas c, int x, int y, int w, int h, String title, String text) {
        panel(c, x, y, w, h); c.text(title, x + 30, y + 35, 13, Font.BOLD, BLUE);
        c.wrap(text, x + 30, y + 68, w - 60, 19, 14, MUTED);
    }

    private void drawDonut(Canvas c, Map<String, Integer> values, int x, int y, int w, int h) {
        int total = values.values().stream().mapToInt(Integer::intValue).sum();
        c.g.setStroke(new BasicStroke(55, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        if (total == 0) {
            c.g.setColor(new Color(40, 63, 91)); c.g.drawOval(x + 45, y + 45, w - 90, h - 90);
        } else {
            double start = 90; int i = 0;
            for (int value : values.values()) {
                if (value <= 0) { i++; continue; }
                double arc = -360d * value / total; c.g.setColor(SERIES[i++ % SERIES.length]);
                c.g.draw(new Arc2D.Double(x + 45, y + 45, w - 90, h - 90, start, arc, Arc2D.OPEN)); start += arc;
            }
        }
        c.center(integer(total), x + w / 2, y + h / 2 + 4, 32, Font.BOLD, TEXT);
        c.center("VEHICLES", x + w / 2, y + h / 2 + 29, 11, Font.BOLD, MUTED);
    }

    private void barChart(Canvas c, List<String> labels, List<Double> values, int x, int y, int w, int h, Color color) {
        axes(c, x, y, w, h); if (values.isEmpty()) { noChartData(c, x, y, w, h); return; }
        double max = Math.max(1, values.stream().mapToDouble(Double::doubleValue).max().orElse(1));
        int step = Math.max(1, (int) Math.ceil(values.size() / 12d));
        double cell = (double) (w - 70) / values.size();
        for (int i = 0; i < values.size(); i++) {
            int bh = (int) ((h - 65) * values.get(i) / max); int bx = (int) (x + 55 + i * cell);
            c.g.setColor(color); c.g.fillRoundRect(bx, y + h - 38 - bh, Math.max(5, (int) cell - 6), bh, 7, 7);
            if (i % step == 0) c.center(labels.get(i), bx + Math.max(5, (int) cell - 6) / 2, y + h - 13, 10, Font.PLAIN, MUTED);
        }
        c.text(moneyShort(max), x + 5, y + 18, 11, Font.PLAIN, MUTED);
        c.text("Rs. 0", x + 5, y + h - 38, 11, Font.PLAIN, MUTED);
    }

    private void lineChart(Canvas c, List<String> labels, List<Double> values, int x, int y, int w, int h, Color color) {
        axes(c, x, y, w, h); if (values.isEmpty()) { noChartData(c, x, y, w, h); return; }
        double max = Math.max(1, values.stream().mapToDouble(Double::doubleValue).max().orElse(1));
        int oldX = -1, oldY = -1;
        for (int i = 0; i < values.size(); i++) {
            int px = x + 55 + (values.size() == 1 ? (w - 80) / 2 : i * (w - 80) / (values.size() - 1));
            int py = y + h - 38 - (int) ((h - 65) * values.get(i) / max);
            if (oldX >= 0) { c.g.setColor(color); c.g.setStroke(new BasicStroke(4)); c.g.drawLine(oldX, oldY, px, py); }
            c.g.setColor(TEXT); c.g.fillOval(px - 5, py - 5, 10, 10); c.center(labels.get(i), px, y + h - 13, 10, Font.PLAIN, MUTED);
            oldX = px; oldY = py;
        }
        c.text(moneyShort(max), x + 5, y + 18, 11, Font.PLAIN, MUTED);
    }

    private void axes(Canvas c, int x, int y, int w, int h) {
        c.g.setColor(new Color(40, 63, 91)); c.g.setStroke(new BasicStroke(1));
        for (int i = 0; i <= 4; i++) { int gy = y + 20 + i * (h - 58) / 4; c.g.drawLine(x + 55, gy, x + w, gy); }
    }
    private void noChartData(Canvas c, int x, int y, int w, int h) { c.center("No revenue data for this period", x + w / 2, y + h / 2, 17, Font.PLAIN, MUTED); }
    private void footer(Canvas c, int page) { c.text("ParkX • Confidential Management Report", 55, 1725, 11, Font.BOLD, MUTED); c.text("Page " + page, 1185, 1725, 11, Font.BOLD, MUTED, true); }

    private void writePdf(List<BufferedImage> pages, Path target) throws IOException {
        List<byte[]> jpgs = new ArrayList<>();
        for (BufferedImage page : pages) { ByteArrayOutputStream image = new ByteArrayOutputStream(); ImageIO.write(page, "jpg", image); jpgs.add(image.toByteArray()); }
        int pageCount = pages.size(); int objectCount = 2 + pageCount * 3;
        byte[][] objects = new byte[objectCount + 1][];
        objects[1] = bytes("<< /Type /Catalog /Pages 2 0 R >>");
        StringBuilder kids = new StringBuilder("[");
        for (int i = 0; i < pageCount; i++) kids.append(3 + i * 3).append(" 0 R ");
        objects[2] = bytes("<< /Type /Pages /Kids " + kids + "] /Count " + pageCount + " >>");
        for (int i = 0; i < pageCount; i++) {
            int pageObj = 3 + i * 3, imageObj = pageObj + 1, contentObj = pageObj + 2;
            objects[pageObj] = bytes("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /XObject << /Im" + i + " " + imageObj + " 0 R >> >> /Contents " + contentObj + " 0 R >>");
            byte[] jpg = jpgs.get(i);
            objects[imageObj] = stream("<< /Type /XObject /Subtype /Image /Width " + W + " /Height " + H + " /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length " + jpg.length + " >>", jpg);
            byte[] commands = bytes("q 595 0 0 842 0 0 cm /Im" + i + " Do Q");
            objects[contentObj] = stream("<< /Length " + commands.length + " >>", commands);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(); out.write(bytes("%PDF-1.4\n%\u00e2\u00e3\u00cf\u00d3\n"));
        long[] offsets = new long[objectCount + 1];
        for (int i = 1; i <= objectCount; i++) { offsets[i] = out.size(); out.write(bytes(i + " 0 obj\n")); out.write(objects[i]); out.write(bytes("\nendobj\n")); }
        long xref = out.size(); out.write(bytes("xref\n0 " + (objectCount + 1) + "\n0000000000 65535 f \n"));
        for (int i = 1; i <= objectCount; i++) out.write(bytes(String.format("%010d 00000 n \n", offsets[i])));
        out.write(bytes("trailer\n<< /Size " + (objectCount + 1) + " /Root 1 0 R >>\nstartxref\n" + xref + "\n%%EOF"));
        Files.write(target, out.toByteArray());
    }

    private byte[] stream(String dictionary, byte[] data) throws IOException { ByteArrayOutputStream out = new ByteArrayOutputStream(); out.write(bytes(dictionary + "\nstream\n")); out.write(data); out.write(bytes("\nendstream")); return out.toByteArray(); }
    private byte[] bytes(String text) { return text.getBytes(StandardCharsets.ISO_8859_1); }
    private double revenueOn(List<ParkingHistoryRecord> rows, LocalDate date) { return rows.stream().filter(r -> r.getExitTime().toLocalDate().equals(date)).mapToDouble(ParkingHistoryRecord::getTotalFee).sum(); }
    private double sum(List<ParkingHistoryRecord> rows) { return rows.stream().mapToDouble(ParkingHistoryRecord::getTotalFee).sum(); }
    private double avg(double total, int count) { return count == 0 ? 0 : total / count; }
    private String maxKey(Map<String, Integer> map, String fallback) { return map.entrySet().stream().filter(e -> e.getValue() > 0).max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(fallback); }
    private String integer(long value) { return String.format("%,d", value); }
    private String money(double value) { return String.format("Rs. %,.2f", value); }
    private String moneyShort(double value) { return value >= 1_000_000 ? String.format("Rs. %.1fM", value / 1_000_000) : value >= 1000 ? String.format("Rs. %.1fK", value / 1000) : String.format("Rs. %.0f", value); }

    private static class Canvas {
        final BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        Canvas() { g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); }
        void text(String value, int x, int y, int size, int style, Color color) { text(value, x, y, size, style, color, false); }
        void text(String value, int x, int y, int size, int style, Color color, boolean right) { g.setFont(new Font("SansSerif", style, size)); g.setColor(color); int px = right ? x - g.getFontMetrics().stringWidth(value) : x; g.drawString(value, px, y); }
        void center(String value, int x, int y, int size, int style, Color color) { g.setFont(new Font("SansSerif", style, size)); g.setColor(color); g.drawString(value, x - g.getFontMetrics().stringWidth(value) / 2, y); }
        void fitText(String value, int x, int y, int width, int size, int style, Color color) { int s = size; do { g.setFont(new Font("SansSerif", style, s--)); } while (s > 9 && g.getFontMetrics().stringWidth(value) > width); g.setColor(color); g.drawString(value, x, y); }
        void wrap(String value, int x, int y, int width, int lineHeight, int size, Color color) { g.setFont(new Font("SansSerif", Font.PLAIN, size)); g.setColor(color); FontMetrics fm = g.getFontMetrics(); String line = ""; for (String word : value.split(" ")) { String test = line.isEmpty() ? word : line + " " + word; if (fm.stringWidth(test) > width) { g.drawString(line, x, y); y += lineHeight; line = word; } else line = test; } if (!line.isEmpty()) g.drawString(line, x, y); }
        BufferedImage finish() { g.dispose(); return image; }
    }
}
