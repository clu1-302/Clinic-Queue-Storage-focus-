package logic;

import model.QueueTicket;
import model.TicketStatus;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Snapshot of one day's activity. Built by ClinicService.generateReport(). */
public class DailyReport {
    private final LocalDate date;
    private final int served;
    private final int waiting;
    private final Map<String, Integer> countPerVisitType = new LinkedHashMap<>();

    public DailyReport(LocalDate date, List<QueueTicket> tickets) {
        this.date = date;
        countPerVisitType.put("Consultation", 0);
        countPerVisitType.put("Dental", 0);
        countPerVisitType.put("Check-up", 0);
        int s = 0, w = 0;
        for (QueueTicket t : tickets) {
            if (t.getStatus() == TicketStatus.SERVED && date.equals(t.getServedDate())) s++;
            if (t.getStatus() == TicketStatus.WAITING) w++;
            if (date.equals(t.getIssuedDate()))
                countPerVisitType.merge(t.getVisitType(), 1, Integer::sum);
        }
        served = s;
        waiting = w;
    }

    public LocalDate getDate() { return date; }
    public int getServed() { return served; }
    public int getWaiting() { return waiting; }
    public Map<String, Integer> getCountPerVisitType() { return countPerVisitType; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DAILY REPORT - " + date + "\n");
        sb.append("Patients served today : ").append(served).append('\n');
        sb.append("Still waiting         : ").append(waiting).append('\n');
        sb.append("Visits by type:\n");
        countPerVisitType.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append('\n'));
        return sb.toString();
    }
}
