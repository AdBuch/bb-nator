# F09 – Mentor-Cockpit (Dashboard)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Spezifiziert (Demo-Vorstufe in PR #6) | B | [F01](F01-identitaet-anmeldung.md), [F03](F03-datenschutz-mechanismus.md), [F10](F10-aufgaben-assistent.md) |

## Ziel
Der Mentor sieht in unter einer Minute, wer heute Unterstützung braucht, was offen ist und welche Ziele fehlen.

## User Stories
- BB-01: Als Mentor sehe ich meine Lernenden mit Aufgaben, Meilensteinen und fehlenden verifizierten Zielen; jede Zahl führt zu den Einträgen.
- BB-06: Als Mentor finde ich nicht abgedeckte Pflichtziele und nutze sie für neue Aufgaben.

## Umfang
- Lernendenkarten, Aufmerksamkeits-Warteschlange mit Grund und nächstem Schritt, Abdeckungs-Heatmap, Aufgaben-Board, Anstehendes.
- Jede Ansicht zeigt den Stand der letzten Synchronisation.

## Nicht im Umfang
- Ranglisten
- Risiko-Scores aus Aktivitätsdaten

## Akzeptanzkriterien
- Häufigste Aktionen in höchstens drei Interaktionen.
- Tastaturbedienbar, Diagramme mit Tabellenalternative.

## Datenschutz
Nur zugeordnete Lernende sichtbar.

## Offene Fragen
- keine

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- BB_COCKPIT.md §3A
- UX_WIREFRAMES.md

## Präzisierung: operativer Berufsbildner-Arbeitsplatz (2026-11-23)

Die Startseite ist **aktionsorientiert**. Oben: Kohorte/Lehrjahr/Semester, Suche, sofortiger Button **Neue Aufgabe**. Dazu sechs getrennte Panels: (1) Lernendenkarten mit aktuellem Fokus, (2) fällige Reviews und Hilfegesuche, (3) BiVo-Abdeckung **geplant / geübt / belegt / verifiziert**, (4) Aufgabenboard, (5) Check-ins/üK/Schule/Meilensteine, (6) Datenqualität/Sync-Status.

- In einer **Aufmerksamkeitsliste** steht immer ein konkreter Grund und eine nächste menschliche Aktion. Nicht aus XP, Commit-Zahlen oder Zeit automatisch eine negative Prognose ableiten.
- Aktion an jedem Lernenden: Profil, gezielte Aufgabe zuweisen, Feedback, Gespräch planen. Jede aggregierte Zahl ist anklickbar und führt zu den zugrundeliegenden Nachweisen.
- Ziele ohne Nachweis oder ohne geplante Aufgabe sind getrennt sichtbar; externe Systeme zeigen letzten Sync-Zeitpunkt und Verbindungsfehler.
- Bei fehlenden externen Verbindungen zeigt die UI 'nicht verfügbar' statt null oder 0 %.
- **Akzeptanz:** Mentor entdeckt einen überfälligen Review in unter 30 s und kann eine Lernaufgabe aus einer dokumentierten Kompetenzlücke in höchstens drei UI-Aktionen vorbereiten.
