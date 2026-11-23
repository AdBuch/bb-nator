# F30 – Ausbildungskalender, Meilensteine und Kapazitätsplanung

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.4 (Kandidat, nicht beschlossen) | **Idee** | B; D nur mit Freigabe | [F08](F08-lernplan.md), [F09](F09-mentor-cockpit.md), [F19](F19-schule-uek.md), [F22](F22-checkins-feedback.md) |

## Ziel
Mentor und Lernende können Lernaufgaben, Meilensteine, Schul-/üK-Phasen und geplante Feedback-Gespräche übersichtlich koordinieren, **ohne eine zweite HR-/Projektplanung zu bauen**.

## Mögliche User Stories
- Als Mentor erkenne ich, ob ein geplantes Lernprojekt mit üK/Schulblock oder Semester-Review kollidiert.
- Als Lernende/r sehe ich meine nächsten Lernmeilensteine, ohne sensible Ferien- oder Krankheitsinformationen Dritter zu erhalten.
- Als Mentor plane ich einen Check-in und sehe, ob Aufgaben mit den vorgesehenen Lernzielen verbunden sind.

## Umfangsvorschlag
Kalender-Leseansicht mit intern verwalteten Meilensteinen; optional autorisierte externe Termininformationen aus F19 und begrenzten Abwesenheitsindikatoren aus F18. Keine Überwachung, keine öffentlichen Personenkalender, keine automatischen Buchungen.

## Akzeptanzkriterien
- Jede Anzeige hat Quelle, Aktualisierungszeitpunkt und klare Rechte.
- Unbekannte externe Daten werden nicht als frei/verfügbar interpretiert.
- Kalender ist per Tastatur und Listenalternative nutzbar.

## Offene Entscheide
Ob wirklich ein eigenes Kalenderfeature nötig ist oder F08/F09/F19/F22 zusammengesetzte Views genügen; erlaubte externe Datenfelder, Ziel-Release.

## Quellen
BB_COCKPIT.md (Meilensteine, Schul-/üK-Konflikte, Check-ins); abgeleitete UX-Idee, nicht produktseitig genehmigt.
