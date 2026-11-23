# F11 – Nachweise und Kompetenzbewertung

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 – v0.6 | Spezifiziert | B | [F10](F10-aufgaben-assistent.md) |

## Ziel
Erledigte Aufgaben liefern mögliche Nachweise; erst der Mentor bewertet die Kompetenz.

## User Stories
- BB-05: Als Mentor prüfe ich Nachweise und erfasse eine Kompetenzstufe mit Begründung.

## Umfang
- Nachweisstatus: Fehlt / Eingereicht / Geprüft / Verifiziert / Nacharbeit.
- Kompetenzstufe 0–4 mit Bewerter, Datum, Nachweis, Zielstufe und Sicherheit.
- Zielstatus: nicht geplant / geplant / geübt / belegt / verifiziert.

## Nicht im Umfang
- Automatische Verifikation aus Aufgabenstatus

## Akzeptanzkriterien
- Aufgabe 'Erledigt' setzt kein Ziel auf 'verifiziert'.

## Datenschutz
Bewertungen: Mentor ist Autor, Lernende/r ist betroffene Person mit vollem Leserecht.

## Offene Fragen
- Definition jeder Stufe 0–4 und wer welche Stufe setzen darf.

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- BB_COCKPIT.md §4

## Präzisierung: von Tätigkeit zu Kompetenz (2026-11-23)

Ein Ticketstatus, bestandene Pipeline, Fokuszeit oder Quizpunktzahl ist **noch kein** Kompetenznachweis. Zwischen Ziel, Aufgabe, eingereichtem Artefakt, mentor-geprüftem Nachweis und einer pädagogischen Bewertung bestehen getrennte Zustände.

**Interne Entwicklungsrubrik 0–4 (Vorschlag, vor Einsatz formell zu beschliessen):**
0 = noch keine beobachtbaren Nachweise; 1 = unter enger Anleitung; 2 = mit gezielter Unterstützung; 3 = weitgehend selbständig und kann Entscheidungen erklären; 4 = selbständig, sicher und kann Qualität/Alternativen kritisch beurteilen. Diese Skala ist **keine offizielle QV-Note**.

- Jede Einschätzung enthält Ziel, beobachtete Aufgabe, Artefakt/Demo, Bewerter, Datum, Begründung, eventuell nächstes Förderziel; Lernende können begründet widersprechen oder eine Ergänzung einreichen.
- Automatische Fremdsystemverweise kommen zunächst als **potenzielle** Evidenz, nicht als Abschlussentscheidung.
- Statistiken zeigen immer **verifizierte Ziele / anwendbare Pflichtziele**, separat geplante und geübte Ziele; nicht ein undurchsichtiger Gesamtscore.
- **Akzeptanz:** Ein Jira-Ticket wechselt auf Done, der Lernzielstatus bleibt 'nicht verifiziert', bis berechtigter Mentor bewusst prüft. Berechtigungsgrenzen und Verifizierung ohne Begründung werden negativ getestet.

## Vorschlag zur fachlichen MVP-Freigabe (2026-11-23)

Die vorgeschlagene [interne 0–4-Rubrik mit beobachtbaren Kriterien und Evidenzworkflow](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md#2-f11--entwicklungsstufen-04-interne-rubrik-vorschlag) unterscheidet **0 = nicht ausreichend belegt** von fehlender Fähigkeit. Stufen sind ausdrücklich weder amtliche QV-Noten noch automatisch aus Tasks, Zeit oder XP ableitbar. Bewertungsrechte, Belegstandard und Freigabe sind **noch zu entscheiden**.
