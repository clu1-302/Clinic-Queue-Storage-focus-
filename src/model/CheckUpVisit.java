package model;

/** A routine check-up: vital signs, height, weight, and a quick physical exam. */
public class CheckUpVisit extends Visit {
    private static final int ESTIMATED_MINUTES = 10;

    public CheckUpVisit() { this(""); }
    public CheckUpVisit(String notes) { super(notes); }

    @Override public int getEstimatedMinutes() { return ESTIMATED_MINUTES; }
    @Override public String getDescription() { return "Routine check-up: blood pressure, temperature, height and weight."; }
    @Override public String getTypeName() { return "Check-up"; }
}
