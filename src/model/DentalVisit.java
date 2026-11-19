package model;

/** A visit to the school dentist: check, cleaning or extraction. */
public class DentalVisit extends Visit {
    private static final int ESTIMATED_MINUTES = 30;

    public DentalVisit() { this(""); }
    public DentalVisit(String notes) { super(notes); }

    @Override public int getEstimatedMinutes() { return ESTIMATED_MINUTES; }
    @Override public String getDescription() { return "Dental visit: oral check, cleaning or simple extraction."; }
    @Override public String getTypeName() { return "Dental"; }
}
