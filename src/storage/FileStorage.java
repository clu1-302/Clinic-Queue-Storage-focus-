package storage;

import logic.ClinicService;
import model.InvalidInputException;
import model.Patient;
import model.QueueTicket;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Saves and loads patients and tickets as text files. Never crashes on a
 * missing or damaged file: bad lines are skipped and a message is returned.
 */
public class FileStorage {
    private final Path patientsFile;
    private final Path ticketsFile;

    public FileStorage() { this("data"); }

    public FileStorage(String folder) {
        patientsFile = Paths.get(folder, "patients.txt");
        ticketsFile = Paths.get(folder, "tickets.txt");
    }

    /** Writes all records. Returns a status message (never throws). */
    public String save(ClinicService service) {
        try {
            Files.createDirectories(patientsFile.getParent());
            List<String> pl = new ArrayList<>();
            for (Patient p : service.getPatients()) pl.add(RecordConverter.patientToLine(p));
            List<String> tl = new ArrayList<>();
            for (QueueTicket t : service.getTickets()) tl.add(RecordConverter.ticketToLine(t));
            Files.write(patientsFile, pl, StandardCharsets.UTF_8);
            Files.write(ticketsFile, tl, StandardCharsets.UTF_8);
            return "Saved " + pl.size() + " patients and " + tl.size() + " tickets.";
        } catch (IOException e) {
            return "Could not save: " + e.getMessage();
        }
    }

    /** Loads into the service. Returns a message describing what happened. */
    public String load(ClinicService service) {
        if (!Files.exists(patientsFile) && !Files.exists(ticketsFile)) {
            return "No saved data found. Starting with an empty clinic.";
        }
        int skipped = 0;
        List<Patient> patients = new ArrayList<>();
        Map<Integer, Patient> byId = new HashMap<>();
        List<QueueTicket> tickets = new ArrayList<>();
        try {
            for (String line : readLines(patientsFile)) {
                try {
                    Patient p = RecordConverter.lineToPatient(line);
                    if (byId.containsKey(p.getId())) throw new InvalidInputException("Duplicate id");
                    patients.add(p);
                    byId.put(p.getId(), p);
                } catch (InvalidInputException e) { skipped++; }
            }
            for (String line : readLines(ticketsFile)) {
                try { tickets.add(RecordConverter.lineToTicket(line, byId)); }
                catch (InvalidInputException e) { skipped++; }
            }
        } catch (IOException e) {
            return "Could not read saved data (" + e.getMessage() + "). Starting empty.";
        }
        service.load(patients, tickets);
        String msg = "Loaded " + patients.size() + " patients and " + tickets.size() + " tickets.";
        return skipped == 0 ? msg : msg + " Skipped " + skipped + " damaged line(s).";
    }

    private List<String> readLines(Path file) throws IOException {
        List<String> out = new ArrayList<>();
        if (!Files.exists(file)) return out;
        for (String l : Files.readAllLines(file, StandardCharsets.UTF_8)) if (!l.trim().isEmpty()) out.add(l);
        return out;
    }
}
