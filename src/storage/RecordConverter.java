package storage;

import model.InvalidInputException;
import model.Patient;
import model.QueueTicket;
import model.TicketStatus;
import model.Visit;

import java.time.LocalDate;
import java.util.Map;

/**
 * Converts records to and from one line of text.
 * Patient line: id|name|age
 * Ticket line:  number|patientId|visitType|notes|status|issuedDate|servedDate
 * The "|" character is removed from free text so it can never break a line.
 */
public class RecordConverter {
    private static final String SEP = "|";

    public static String patientToLine(Patient p) {
        return p.getId() + SEP + clean(p.getName()) + SEP + p.getAge();
    }

    public static Patient lineToPatient(String line) {
        String[] f = line.split("\\|", -1);
        if (f.length != 3) throw new InvalidInputException("Bad patient line: " + line);
        try {
            return new Patient(Integer.parseInt(f[0].trim()), f[1].trim(), Integer.parseInt(f[2].trim()));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Bad number in patient line: " + line);
        }
    }

    public static String ticketToLine(QueueTicket t) {
        return t.getNumber() + SEP + t.getPatient().getId() + SEP + t.getVisitType() + SEP
                + clean(t.getVisit().getNotes()) + SEP + t.getStatus().name() + SEP
                + t.getIssuedDate() + SEP + (t.getServedDate() == null ? "" : t.getServedDate());
    }

    /** patientsById lets the ticket find its patient; an unknown id means a damaged record. */
    public static QueueTicket lineToTicket(String line, Map<Integer, Patient> patientsById) {
        String[] f = line.split("\\|", -1);
        if (f.length != 7) throw new InvalidInputException("Bad ticket line: " + line);
        try {
            Patient p = patientsById.get(Integer.parseInt(f[1].trim()));
            if (p == null) throw new InvalidInputException("Ticket refers to a missing patient: " + line);
            LocalDate served = f[6].trim().isEmpty() ? null : LocalDate.parse(f[6].trim());
            return new QueueTicket(Integer.parseInt(f[0].trim()), p, Visit.fromTypeName(f[2], f[3]),
                    TicketStatus.valueOf(f[4].trim()), LocalDate.parse(f[5].trim()), served);
        } catch (RuntimeException e) {
            if (e instanceof InvalidInputException) throw e;
            throw new InvalidInputException("Bad ticket line: " + line);
        }
    }

    private static String clean(String s) {
        return s == null ? "" : s.replace("|", " ").replace("\n", " ").replace("\r", " ");
    }
}
