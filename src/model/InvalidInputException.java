package model;

/**
 * Thrown when a value typed by the user (or read from a file) is not valid,
 * for example a blank name, a negative age, or an unknown visit type.
 * It is unchecked, so callers do not need "throws"; the logic/ui code catches it to show a message.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
