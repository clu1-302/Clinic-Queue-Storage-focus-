package ui;

import logic.ClinicService;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Patients tab: search, edit, remove. */
public class PatientsPanel extends JPanel {
    private final ClinicService service;
    private final StatusListener status;
    private final Runnable onChange;

    private final JTextField searchField = new JTextField(20);
    private final JTextField nameField = new JTextField(15);
    private final JTextField ageField = new JTextField(5);
    private final JButton saveButton = QueuePanel.styledButton("Save changes");
    private final JButton removeButton = QueuePanel.styledButton("Remove patient");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"ID", "Name", "Age"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public PatientsPanel(ClinicService service, StatusListener status, Runnable onChange) {
        this.service = service;
        this.status = status;
        this.onChange = onChange;
        setLayout(new BorderLayout(DesignConstants.PAD, DesignConstants.PAD));
        setBackground(DesignConstants.BACKGROUND);
        setBorder(DesignConstants.padded());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.setBorder(DesignConstants.box("Search"));
        top.add(new JLabel("Name contains:"));
        top.add(searchField);
        JButton searchButton = QueuePanel.styledButton("Search");
        top.add(searchButton);
        add(top, BorderLayout.NORTH);

        table.setFont(DesignConstants.LABEL_FONT);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel edit = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        edit.setOpaque(false);
        edit.setBorder(DesignConstants.box("Edit or remove selected patient"));
        edit.add(new JLabel("Name:"));
        edit.add(nameField);
        edit.add(new JLabel("Age:"));
        edit.add(ageField);
        edit.add(saveButton);
        edit.add(removeButton);
        add(edit, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> refresh());
        searchField.addActionListener(e -> refresh());
        saveButton.addActionListener(e -> save());
        removeButton.addActionListener(e -> remove());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = table.getSelectedRow();
            if (r >= 0) {
                nameField.setText((String) tableModel.getValueAt(r, 1));
                ageField.setText(String.valueOf(tableModel.getValueAt(r, 2)));
            }
            updateButtons();
        });
        refresh();
    }

    private void updateButtons() {
        boolean selected = table.getSelectedRow() >= 0;
        saveButton.setEnabled(selected && !nameField.getText().trim().isEmpty() && !ageField.getText().trim().isEmpty());
        removeButton.setEnabled(selected);
    }

    private int selectedId() { return (Integer) tableModel.getValueAt(table.getSelectedRow(), 0); }

    private void save() {
        try {
            service.editPatient(selectedId(), nameField.getText(), Integer.parseInt(ageField.getText().trim()));
            onChange.run();
            status.show("Patient updated.", true);
        } catch (NumberFormatException e) {
            status.show("Age must be a whole number.", false);
        } catch (RuntimeException e) {
            status.show(e.getMessage(), false);
        }
    }

    private void remove() {
        int id = selectedId();
        String name = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
        if (!Dialogs.confirmRemove(this, name + " and their tickets")) {
            status.show("Removal cancelled.", true);
            return;
        }
        try {
            service.removePatient(id);
            onChange.run();
            status.show("Removed " + name + ".", true);
        } catch (RuntimeException e) {
            status.show(e.getMessage(), false);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        try {
            for (Patient p : service.searchPatients(searchField.getText())) {
                tableModel.addRow(new Object[] {p.getId(), p.getName(), p.getAge()});
            }
        } catch (RuntimeException e) {
            status.show(e.getMessage(), false);
        }
        nameField.setText("");
        ageField.setText("");
        updateButtons();
    }
}
