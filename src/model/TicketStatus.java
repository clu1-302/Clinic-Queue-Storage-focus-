package model;

/** Where a queue ticket is in its life: waiting, already served, or cancelled (no-show). */
public enum TicketStatus {
    WAITING("Waiting"),
    SERVED("Served"),
    CANCELLED("Cancelled");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    @Override public String toString() { return label; }
}
