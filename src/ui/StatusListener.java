package ui;

/** Lets a panel report a colored message to the main window's status line. */
public interface StatusListener {
    void show(String message, boolean ok);
}
