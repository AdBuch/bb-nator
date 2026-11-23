# F16 – Jira-EDU-Integration

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.4 | Spezifiziert | B | [F10](F10-aufgaben-assistent.md) |

## Ziel
Mentoren erstellen oder verknüpfen Jira-EDU-Tickets aus dem Assistenten; Jira bleibt führend für Ticketstatus.

## User Stories
- BB-07: Als Mentor erstelle oder verknüpfe ich ein Jira-EDU-Ticket mit Vorschau und Bestätigung.

## Umfang
- Direkter Spring-Adapter, Idempotenzschlüssel, Ticket-Key und URL speichern, Statusabgleich ohne stilles Überschreiben.

## Nicht im Umfang
- Proxy über andere interne Werkzeuge
- Ticketbearbeitung in BB-NATOR

## Akzeptanzkriterien
- Ticket wird genau einmal erstellt, auch bei Wiederholung.
- Erledigtes Ticket verifiziert keine Kompetenz.

## Datenschutz
Nur Verweise und Metadaten; Tokens mit minimalem Scope.

## Offene Fragen
- Projekt-Key, Vorgangstypen und Felder von Jira EDU?
- Bleiben interne Aufgaben nach der Integration bestehen?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- INTEGRATION_AND_REUSE_PLAN.md
- BB_COCKPIT.md §3C

## Präzisierung: Aufgaben und EDU-Workflow (2026-11-23)

- **Direkter** Java/Spring-Jira-Adapter, kein Tinyops-Hop. Filter auf erlaubte EDU-Projekte, verantwortliche Personen, Vorgangstypen und Ausbildungszeitraum; JQL und Feld-Mapping konfigurierbar.
- Ein BB-NATOR-Lerntask bleibt pädagogische Einheit mit Zielbeziehungen; Jira führt Issue-Status, Beschreibung und Transitions. Es gibt sowohl **bestehendes Ticket verknüpfen** als auch **Ticket nach Vorschau erstellen**.
- Menschliche Bestätigung vor jeder Schreibaktion; idempotenter Create, Konflikthinweise statt blindem Überschreiben, begrenzte Pagination, Retry/Backoff, Sync-Zeitstempel.
- Lernendenidentität wird über verifizierte externe IDs verknüpft, niemals allein anhand gleicher Namen. Nur erforderliche Metadaten speichern; keine Geheimnisse in Logs.
- **Akzeptanz:** Bei API-Ausfall bleiben Lernaufgaben intern sichtbar und mit 'letzter Stand / Sync fehlgeschlagen' markiert; wiederholter Create erzeugt nur ein Jira-Ticket; Done verifiziert keine Kompetenz.
