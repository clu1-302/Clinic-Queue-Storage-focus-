package model;

/**
 * A kind of clinic visit. Each subclass gives its own estimated minutes,
 * description and type name, so the program can keep every visit in a
 * List<Visit> and call these methods without knowing the exact subclass
 * (polymorphism).
 */
public abstract class Visit {
    private String notes; // optional, e.g. "toothache", "fever since Monday"

    protected Visit(String notes) {
        setNotes(notes);
    }

    /** How long this kind of visit usually takes, in minutes. */
    public abstract int getEstimatedMinutes();

    /** A short sentence saying what happens during this visit. */
    public abstract String getDescription();

    /** The name shown on screen and saved to the file: "Consultation", "Dental" or "Check-up". */
    public abstract String getTypeName();

    public String getNotes() { return notes; }

    public void setNotes(String notes) {
        this.notes = (notes == null) ? "" : notes.trim();
    }

    /** All the type names, in order. Handy for a JComboBox in the ui package. */
    public static String[] getTypeNames() {
        return new String[] { "Consultation", "Dental", "Check-up" };
    }

    /**
     * Builds the right subclass from its type name. Used when the user picks a
     * visit type and when storage reads a line from the file.
     */
    public static Visit fromTypeName(String typeName, String notes) {
        if (typeName == null) {
            throw new InvalidInputException("Please choose a visit type.");
        }
        switch (typeName.trim().toLowerCase()) {
            case "consultation": return new ConsultationVisit(notes);
            case "dental":       return new DentalVisit(notes);
            case "check-up":
            case "checkup":      return new CheckUpVisit(notes);
            default:
                throw new InvalidInputException("Unknown visit type: " + typeName);
        }
    }

    public static Visit fromTypeName(String typeName) {
        return fromTypeName(typeName, "");
    }

    @Override public String toString() {
        String base = getTypeName() + " (~" + getEstimatedMinutes() + " min)";
        return notes.isEmpty() ? base : base + " - " + notes;
    }
}
