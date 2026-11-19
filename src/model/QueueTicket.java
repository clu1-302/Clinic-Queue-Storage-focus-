package model;

import java.time.LocalDate;

public class QueueTicket {
    private final int number;
    private final Patient patient;
    private final Visit visit; // a Consultation, Dental or Check-up visit
    private final LocalDate issuedDate;
    private LocalDate servedDate; // null until the ticket is served
    private TicketStatus status;

    public QueueTicket(int number, Patient patient, Visit visit) {
        this(number, patient, visit, TicketStatus.WAITING, LocalDate.now(), null);
    }

    /** Full constructor, used when storage restores a saved ticket. */
    public QueueTicket(int number, Patient patient, Visit visit, TicketStatus status,
                       LocalDate issuedDate, LocalDate servedDate) {
        if (patient == null) throw new InvalidInputException("A ticket needs a patient.");
        if (visit == null) throw new InvalidInputException("A ticket needs a visit.");
        this.number = number;
        this.patient = patient;
        this.visit = visit;
        this.status = (status == null) ? TicketStatus.WAITING : status;
        this.issuedDate = (issuedDate == null) ? LocalDate.now() : issuedDate;
        this.servedDate = servedDate;
    }

    public int getNumber() { return number; }
    public Patient getPatient() { return patient; }
    public Visit getVisit() { return visit; }
    public String getVisitType() { return visit.getTypeName(); }
    public int getEstimatedMinutes() { return visit.getEstimatedMinutes(); }
    public LocalDate getDate() { return issuedDate; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public LocalDate getServedDate() { return servedDate; }
    public TicketStatus getStatus() { return status; }
    public boolean isWaiting() { return status == TicketStatus.WAITING; }
    public boolean isServed() { return status == TicketStatus.SERVED; }

    public void markServed() {
        if (status != TicketStatus.WAITING) {
            throw new InvalidInputException("Only a waiting ticket can be served.");
        }
        status = TicketStatus.SERVED;
        servedDate = LocalDate.now();
    }

    public void cancel() {
        if (status != TicketStatus.WAITING) {
            throw new InvalidInputException("Only a waiting ticket can be cancelled.");
        }
        status = TicketStatus.CANCELLED;
    }

    @Override public String toString() {
        return "Q" + number + " - " + patient.getName() + " [" + visit.getTypeName() + ", " + status.getLabel().toUpperCase() + "]";
    }
}
