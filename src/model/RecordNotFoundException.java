package model;

/**
 * Thrown when a patient or queue ticket that the user asked for does not exist,
 * for example "find ticket Q99" when there is no ticket 99.
 * The logic package throws it; the screens catch it and show a message.
 */
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
