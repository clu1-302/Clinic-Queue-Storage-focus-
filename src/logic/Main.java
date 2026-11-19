package logic;

import model.QueueTicket;

/** Starts the logic package on its own: java -cp out logic.Main */
public class Main {
    public static void main(String[] args) {
        ClinicService service = new ClinicService();
        service.addPatient("Juan Dela Cruz", 19);
        service.addPatient("Maria Santos", 20);
        QueueTicket a = service.issueTicket(1, "Consultation");
        QueueTicket b = service.issueTicket(2, "Dental");

        System.out.println("Search 'mar': " + service.searchPatients("mar"));
        System.out.println("Wait for " + b + ": " + service.estimatedWaitMinutes(b) + " min");
        System.out.println("Called: " + service.callNext());
        System.out.println("Wait for " + b + ": " + service.estimatedWaitMinutes(b) + " min");
        System.out.println();
        System.out.print(service.generateReport());

        service.editPatient(2, "Maria S. Santos", 21);
        service.removePatient(1);
        System.out.println("After edit/remove: " + service.searchPatients(""));

        try {
            service.addPatient("R2D2", 5);
        } catch (RuntimeException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            service.addPatient("Bob", 200);
        } catch (RuntimeException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            service.issueTicket(1, "Dental"); // patient 1 was removed
        } catch (RuntimeException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
