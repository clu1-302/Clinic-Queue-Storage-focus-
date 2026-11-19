# Clinic Queue System

A Java Swing desktop app for a school clinic. The front desk registers patients, issues queue numbers, calls the next patient, and views a daily report. Data is saved to text files and loads automatically.

## Features

- **Patients:** register, search (case-insensitive), edit, and remove (also removes their tickets).
- **Queue tickets:** issue a ticket for a Consultation (15 min), Dental (30 min), or Check-up (10 min) visit, with optional notes.
- **Call next:** serves the oldest waiting ticket, first come, first served.
- **Estimated wait:** the sum of the estimated minutes of every waiting ticket ahead.
- **Daily report:** patients served today, patients still waiting, and visits per type.
- **Validation:** names are letters only, age is 1-120, and visit types are checked. Errors show as a red status line.
- **Auto-save:** every change is saved, and File > Save and Exit also save.

## Requirements

- JDK 8 or newer

## Build and run

From the project folder:

```
javac -d out $(find src -name '*.java')
java -cp out Main
```

On Windows PowerShell:

```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out Main
```

To try the logic package alone (no window): `java -cp out logic.Main`

## Project structure

```
src/
  Main.java                 starts the app
  model/                    data classes
    Patient, QueueTicket, TicketStatus
    Visit (abstract) -> ConsultationVisit, DentalVisit, CheckUpVisit
    InvalidInputException, RecordNotFoundException
  logic/                    rules, no UI
    ClinicService, Validator, DailyReport, Main
  storage/                  saving and loading
    FileStorage, RecordConverter
  ui/                       Swing screens
    MainWindow, QueuePanel, PatientsPanel, ReportPanel
    Dialogs, DesignConstants, StatusListener
docs/
  UserGuide.md, TestPlan.md, ClassDiagram.md
```

## Data storage

Files are kept in the `data/` folder (ignored by git):

| File | Line format |
|------|-------------|
| `patients.txt` | `id\|name\|age` |
| `tickets.txt` | `number\|patientId\|visitType\|notes\|status\|issuedDate\|servedDate` |

- Notes and names are escaped (`\|`, `\\`, `\n`, `\r`), so text is saved exactly as typed.
- Saves are written to a `.tmp` file first and then moved into place, so a crash cannot leave a half-written file.
- The previous version of each file is kept as `.bak`.
- Damaged lines are skipped, not fatal. The original files are kept as `*.damaged`, and the status line reports how many lines were skipped.
- Unreadable files are kept as `*.unreadable`, and the app starts empty instead of crashing.
- Blank lines, `#` comment lines, and a Notepad BOM are ignored when loading.
- Tickets are sorted by number on load, so the queue order is always preserved.

## Design notes

- **Polymorphism:** each `Visit` subclass gives its own estimated minutes, description, and type name, so the queue treats every visit the same way.
- **Layers:** `ui` calls `logic`, `logic` uses `model`, and `storage` reads and writes the `logic` data. Errors are unchecked exceptions that the screens catch and show as messages.

## Documentation

- [User Guide](docs/UserGuide.md)
- [Test Plan](docs/TestPlan.md)
- [Class Diagram](docs/ClassDiagram.md)

## Authors

<p align="center"><b><code>TEAM KUMAR</code></b></p>

<p align="center">
  <b><i>Harsh Kumar</i></b><br>
  <b><i>Rafael Julien V. Lam</i></b><br>
  <b><i>Tristan Vhan L. Benadera</i></b><br>
  <b><i>Deniel Jake Manlod</i></b><br>
  <b><i>Carlo Dominic Q. Lu</i></b>
</p>
