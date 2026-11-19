package storage;

import logic.Validator;
import model.InvalidInputException;
import model.Patient;
import model.QueueTicket;
import model.TicketStatus;
import model.Visit;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RecordConverter {
    private static final char SEP = '|';
    private static final char ESC = '\\';
    private static final int PATIENT_FIELDS = 3;
    private static final int TICKET_FIELDS = 7;

    private RecordConverter() { }

    public static String patientToLine(Patient p) {
        return p.getId() + String.valueOf(SEP) + escape(p.getName()) + SEP + p.getAge();
    }

    public static Patient lineToPatient(String line) {
        List<String> f = split(line);
        if (f.size() != PATIENT_FIELDS) throw new InvalidInputException("Bad patient line: " + line);
        try {
            int id = Integer.parseInt(f.get(0).trim());
            String name = f.get(1).trim();
            int age = Integer.parseInt(f.get(2).trim());
            if (id < 1) throw new InvalidInputException("Patient id must be positive: " + line);
            Validator.validateName(name);
            Validator.validateAge(age);
            return new Patient(id, name, age);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Bad number in patient line: " + line);
        }
    }

    public static String ticketToLine(QueueTicket t) {
        return t.getNumber() + String.valueOf(SEP) + t.getPatient().getId() + SEP
                + escape(t.getVisitType()) + SEP + escape(t.getVisit().getNotes()) + SEP
                + t.getStatus().name() + SEP + t.getIssuedDate() + SEP
                + (t.getServedDate() == null ? "" : t.getServedDate());
    }

    public static QueueTicket lineToTicket(String line, Map<Integer, Patient> patientsById) {
        List<String> f = split(line);
        if (f.size() != TICKET_FIELDS) throw new InvalidInputException("Bad ticket line: " + line);
        try {
            int number = Integer.parseInt(f.get(0).trim());
            int patientId = Integer.parseInt(f.get(1).trim());
            if (number < 1) throw new InvalidInputException("Ticket number must be positive: " + line);
            Patient p = patientsById.get(patientId);
            if (p == null) throw new InvalidInputException("Ticket refers to a missing patient: " + line);

            Visit visit = Visit.fromTypeName(f.get(2), f.get(3));
            TicketStatus status = TicketStatus.valueOf(f.get(4).trim().toUpperCase());
            LocalDate issued = LocalDate.parse(f.get(5).trim());
            String servedText = f.get(6).trim();
            LocalDate served = servedText.isEmpty() ? null : LocalDate.parse(servedText);

            if (status == TicketStatus.SERVED && served == null) served = issued;
            if (status != TicketStatus.SERVED) served = null;

            return new QueueTicket(number, p, visit, status, issued, served);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new InvalidInputException("Bad number or date in ticket line: " + line);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Bad status in ticket line: " + line);
        }
    }

    static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case ESC: sb.append(ESC).append(ESC); break;
                case SEP: sb.append(ESC).append(SEP); break;
                case '\n': sb.append(ESC).append('n'); break;
                case '\r': sb.append(ESC).append('r'); break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }

    static List<String> split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ESC && i + 1 < line.length()) {
                char next = line.charAt(++i);
                switch (next) {
                    case 'n': cur.append('\n'); break;
                    case 'r': cur.append('\r'); break;
                    case ESC: cur.append(ESC); break;
                    case SEP: cur.append(SEP); break;
                    default:  cur.append(ESC).append(next);
                }
            } else if (c == SEP) {
                fields.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        fields.add(cur.toString());
        return fields;
    }
}
