# F10 – Lernaufgaben-Assistent

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Spezifiziert | B | [F07](F07-sieben-perspektiven.md) |

## Ziel
Mentoren erstellen Lernaufgaben, die auf Ziele ausgerichtet sind, statt nur Tickets zu verteilen.

## User Stories
- BB-02: Als Mentor erstelle ich eine Aufgabe mit Titel, Zweck, Akzeptanzkriterien, Zuweisung und mindestens einem Ziel; ich speichere einen Entwurf und weise zu.

## Umfang
- Sechs Schritte: Absicht, Zielausrichtung, Details, Unterstützungsplan, Zuweisung, Vorschau.
- Vorlagen (Java/OOP, REST, Angular, Git, Docker, Security, IPA …), bearbeitbar.
- Kohortenaufgaben erzeugen individuelle Zuweisungen.
- Status: Entwurf → Bereit → Zugewiesen → In Arbeit ↔ Blockiert → Review → Erledigt.

## Nicht im Umfang
- Veröffentlichung nach Jira (F16)

## Akzeptanzkriterien
- Entwurf speicherbar ohne Zuweisung.
- Aufgabe ohne Zielverknüpfung kann nicht zugewiesen werden.

## Datenschutz
Aufgaben sind Klasse B.

## Offene Fragen
- Welche Vorlagen zuerst?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- BB_COCKPIT.md §3C

## Präzisierung: vollständige Aufgabenerteilung (2026-11-23)

Pflichtdaten beim Zuweisen: **warum** die Aufgabe existiert, Lernziel(e) mit Quellen/Versionen, verständlicher Kontext, Abgaben, Akzeptanzkriterien, verantwortliche Person und ein geplanter Reviewweg. Optional: Schätzung/Story Points, Voraussetzungen, Schwierigkeitsgrad, Hilfestufen, Teststrategie, Termin und Freigabedatum.

**Workflow:** Entwurf → Bereit → Zugewiesen → In Arbeit ↔ Blockiert → Review → Erledigt (oder Abgebrochen). Fachlicher Nachweisstatus wird **separat** verwaltet; mehrere Lernende erhalten jeweils individuelle Fortschritts-/Kompetenzeinträge. Vorlagen liefert F29, der Assistent bleibt Autorisierungspunkt.

**Eingabehilfe:** offene BiVo-Lücken, vorhandene Skills, Lehrpfad, freie Wahl der Lernenden und aktuelle Schul-/üK-Phasen dürfen Hinweise liefern, aber keine automatische Aufgabe oder Bewertung erzwingen.

**Akzeptanzfälle:** Entwurf ohne Ziel darf gespeichert, aber nicht freigegeben werden; Kohorten-Zuweisung hat persönliche Task-IDs; Lernende sehen nur eigene Zuweisung; doppelte Veröffentlichung erzeugt keinen zweiten Jira-Vorgang (F16); Fehlberechtigungen und Validierungsfehler werden getestet.

**Konkrete Startvorlagen:** Java/Canvas, Maven/JUnit, Git, REST/API/JSON, Go-Experimente, Integrationstests, Angular, Docker/CI, Security, AI-/MCP-PoC, IPA, jeweils ohne interne Firmendaten.

## Vorgeschlagener v0.2-MVP-Schnitt (2026-11-23)

Der [MVP-Kernworkflow](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md#5-mvp-abgrenzung-und-reihenfolge-v02) umfasst Aufgabe als Entwurf speichern, versioniertes Lernziel zuordnen, einer berechtigten Person zuweisen, persönliche Evidenz einreichen und einen **separaten** begründeten Mentorentscheid dokumentieren. Die fünf **vorgeschlagenen** T01–T05-Vorlagen liegen fachlich bei F29, nicht als zweites Aufgabenmodell bei F10. Der finale Abnahmeumfang bleibt zur Freigabe offen.
