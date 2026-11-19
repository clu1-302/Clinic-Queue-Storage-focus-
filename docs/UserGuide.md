# Clinic Queue System - User Guide

**Start:** compile with `javac -d out $(find src -name '*.java')`, then run `java -cp out Main` from the project folder.
Saved data is kept in the `data` folder and loads automatically.

## Queue tab (front desk)
1. **Register patient:** type the name (letters only) and age (1-120). The Register button turns on when both are filled in.
2. **Issue ticket:** pick the patient, choose Consultation (15 min), Dental (30 min) or Check-up (10 min), add optional notes, press *Issue ticket*. The status line shows the queue number and the estimated wait.
3. **Call next:** press *Call next* to call the oldest waiting patient. They are marked served.
The table lists everyone still waiting with their estimated wait in minutes.

## Patients tab
- Type part of a name and press *Search* (empty search shows everyone).
- Click a row to load it, change the name or age, then *Save changes*.
- *Remove patient* asks for confirmation and also removes that patient's tickets.

## Report tab
Shows today's patients served, patients still waiting, and visits per type. Press *Refresh report* to update.

## Menus
- **File:** Save, Exit (asks to confirm; data is also saved automatically after every change).
- **View:** jump to Queue, Patients or Report.
- **Help:** About.

## Status line
Green = the action worked. Red = something was wrong (the message says what to fix).
