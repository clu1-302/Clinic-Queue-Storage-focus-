package logic;

import model.InvalidInputException;

public class Validator {
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name must not be empty.");
        if (!name.matches("[A-Za-z .'-]+"))
            throw new InvalidInputException("Name may contain letters only.");
    }

    public static void validateAge(int age) {
        if (age < 1 || age > 120)
            throw new InvalidInputException("Age must be between 1 and 120.");
    }

    public static void validateVisitType(String type) {
        if (type == null || (!type.equals("Consultation") && !type.equals("Dental") && !type.equals("Check-up")))
            throw new InvalidInputException("Visit type must be Consultation, Dental or Check-up.");
    }
}
