# Test Plan - Clinic Queue System (Team Kumar)

| # | Feature | Input / Action | Expected result | Result |
|---|---------|----------------|-----------------|--------|
| 1 | Register patient | Name "Juan Dela Cruz", age 19 | Patient #1 added; green status message | Pass |
| 2 | Validation: blank name | Name "  ", age 19 | InvalidInputException "Name must not be empty."; red status; Register button stays disabled when field is empty | Pass |
| 3 | Validation: name with digits | Name "R2D2" | InvalidInputException "Name may contain letters only." | Pass |
| 4 | Validation: age range | Age 0 and age 121 | InvalidInputException "Age must be between 1 and 120." | Pass |
| 5 | Validation: age not a number | Age "abc" in the window | Red status "Age must be a whole number." | Pass (UI) |
| 6 | Issue next queue number | Issue Dental then Consultation for patient 1 | Tickets Q1 then Q2, both WAITING | Pass |
| 7 | Patient not found | Issue ticket for patient id 99 | RecordNotFoundException "No patient with id 99" | Pass |
| 8 | Estimated waiting time (polymorphism) | Q1 Dental (30 min) waiting ahead of Q2 | Q2 wait = 30 min; after Q1 is called, Q2 wait = 0 | Pass |
| 9 | Call next | Press Call next with 2 waiting | Oldest ticket becomes SERVED; Q1 first | Pass |
| 10 | Call next with empty queue | Call next when nobody waits | Returns nothing; red status "Nobody is waiting." | Pass |
| 11 | Search patients | Keyword "MAR" | Finds "Maria Santos" (case-insensitive) | Pass |
| 12 | Edit missing patient | Edit id 42 | RecordNotFoundException | Pass |
| 13 | Remove patient | Remove patient 1, confirm Yes | Patient and their tickets removed; choosing No keeps them | Pass (logic) / check in UI |
| 14 | Daily report | After serving 1 and 1 waiting | Served 1, waiting 1, count per visit type correct | Pass |
| 15 | Save and load | Add records, close, reopen | Same patients and tickets (including status) appear | Pass |
| 16 | Damaged file | Put "garbage" line in tickets.txt, start | No crash; message "Skipped 2 damaged line(s)" | Pass |
| 17 | Missing file | Delete the data folder, start | No crash; "No saved data found" message | Pass |
| 18 | Exit confirmation | File > Exit or close window | Yes/No dialog; data saved on Yes | Check in UI |

"Pass" rows 1-4, 6-12, 14-17 were run against the code. Rows 5, 13 (dialog) and 18 need a person at the window.
