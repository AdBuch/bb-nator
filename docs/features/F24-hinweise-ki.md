# F24 – Gestufte Hinweise und KI-Vorschläge

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.7 | Idee | B | [F10](F10-aufgaben-assistent.md) |

## Ziel
Hinweise fördern Denken statt Lösungen zu verraten.

## User Stories
- Als Lernende/r rufe ich Hinweise in Stufen ab (Frage → Methode → technischer Hinweis).

## Umfang
- Mentor-geprüfte Hinweisbibliothek, optional KI-Vorschläge, adaptive nächste Aufgabe.

## Nicht im Umfang
- KI vergibt Noten oder reicht Berichte ein

## Akzeptanzkriterien
- Klasse C und D gehen nie an externe KI-Anbieter.
- KI standardmässig aus pro Mandant.

## Datenschutz
KI-Sperre gemäss PRIVACY.md §7.

## Offene Fragen
- Welcher KI-Anbieter, lokal oder extern?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- RELEASES.md v0.7

## Präzisierung: eigenständiges Denken vor Prompt-Automatik (2026-11-23)

**Unterstützungsleiter:** (1) Verständnis-/Rückfrage, (2) Lösungsweg-/Konzeptimpuls, (3) technischer Hinweis/Beispiel **ohne komplette Projektlösung**, (4) gezielte Mentor-Eskalation. Lernende können Problem, bisherigen Versuch und gewünschten Hint-Level erläutern.

- Mentor kuratiert Hinweise je Task/Ziel und entscheidet, wann AI-Vorschläge angeboten werden. Modell, Context Budget, Systemzugriffe und Tool-Berechtigungen sind konfigurierbar; nicht automatisch externe KI nutzen.
- AI-Output wird getestet und hinterfragt, besonders bei API, Architektur, Security und Debugging. Keine autonomen Merge-Requests, Aufgabenbewertungen oder formalen Freigaben.
- Separate **Lernprojekte zum Bauen von MCP-/AI-Agenten** sind F31. F24 ist die Lernhilfe **innerhalb** von BB-NATOR.
- Schutzklasse C/D nie an externe KI. Firmeninterne Quelltexte oder Jira-Inhalte dürfen nicht ohne genehmigte Datenfreigabe hinausgehen.
- **Akzeptanz:** vollständige Lösung wird nicht ungefragt offenbart; fehlender AI-Zugang führt zu konventionellen Hints, Lernaufgabe funktioniert weiter.
