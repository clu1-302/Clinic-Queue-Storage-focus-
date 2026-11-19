import logic.ClinicService;
import storage.FileStorage;
import ui.MainWindow;

import javax.swing.SwingUtilities;

/**
 * Starts the Clinic Queue System: creates the service, loads saved data
 * and opens the main window. All checking happens in the other packages.
 */
public class Main {
    public static void main(String[] args) {
        ClinicService service = new ClinicService();
        FileStorage storage = new FileStorage("data");
        String message = storage.load(service);
        SwingUtilities.invokeLater(() -> new MainWindow(service, storage, message).setVisible(true));
    }
}
