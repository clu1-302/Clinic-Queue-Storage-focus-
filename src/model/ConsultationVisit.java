package model;

/** A general consultation with the school doctor or nurse. */
public class ConsultationVisit extends Visit {
    private static final int ESTIMATED_MINUTES = 15;

    public ConsultationVisit() { this(""); }
    public ConsultationVisit(String notes) { super(notes); }

    @Override public int getEstimatedMinutes() { return ESTIMATED_MINUTES; }
    @Override public String getDescription() { return "General consultation for symptoms such as fever, cough or headache."; }
    @Override public String getTypeName() { return "Consultation"; }
}
