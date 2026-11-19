package ui;

import javax.swing.JOptionPane;
import java.awt.Component;

/** Confirmation and message dialogs. */
public final class Dialogs {
    private Dialogs() {}

    public static boolean confirm(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public static boolean confirmRemove(Component parent, String what) {
        return confirm(parent, "Remove " + what + "? This cannot be undone.", "Confirm removal");
    }

    public static boolean confirmExit(Component parent) {
        return confirm(parent, "Exit the Clinic Queue System? Data will be saved.", "Confirm exit");
    }

    public static void info(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
