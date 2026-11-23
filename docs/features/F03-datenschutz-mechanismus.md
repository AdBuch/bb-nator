# F03 – Datenschutz-Mechanismus (Schutzklassen, Sichtbarkeit, Zugriffsprotokoll)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Spezifiziert | A–D | [F01](F01-identitaet-anmeldung.md), [F02](F02-mandantenfaehigkeit.md) |

## Ziel
Jeder Datensatz trägt eine Schutzklasse; Sichtbarkeit, Aufbewahrung und Protokollierung leiten sich daraus ab. Lernende sind in der Regel Eigentümer der Daten über sich.

## User Stories
- Als Lernende/r entscheide ich, welche meiner freiwilligen Einträge mein Mentor sieht.
- Als Lernende/r sehe ich, wer meine sensiblen Daten geöffnet hat.
- Als Entwickler kann ich keine neue Entität ohne Schutzklasse einführen.

## Umfang
- Schutzklassen A Technisch, B Ausbildung, C Freiwillig, D Sensibel; Deklaration pro Entität, durch Architekturtest erzwungen.
- Eigentümer und Sichtbarkeit `PRIVATE` / `SHARED_WITH_MENTORS` / `FORMAL` pro personenbezogenem Datensatz.
- Prüfung im Use Case, nie im Frontend.
- Zugriffsprotokoll für Klasse D und geteilte Klasse C: wer, wann, welcher Datensatz, welche Aktion, nie der Inhalt.

## Nicht im Umfang
- Einwilligungsregister (F04)
- Löschjobs und Export (F05)

## Akzeptanzkriterien
- Mentor ohne Zuordnung kann Datensätze eines Lernenden nicht lesen.
- Privater Eintrag ist für alle ausser dem Eigentümer unsichtbar.
- Lesen eines Klasse-D-Datensatzes erzeugt einen Protokolleintrag, den die/der Lernende sieht.

## Datenschutz
Kern des Datenschutzkonzepts.

## Offene Fragen
- Darf ein BB-Lead Klasse D aller Lernenden sehen oder nur der eigenen Kohorte?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- PRIVACY.md §1, §2, §6

## Präzisierung: verbindliche Sicherheitsgates (2026-11-23)

- Jede personenbezogene/mandanteneigene Fachentität trägt Klasse A/B/C/D und einen nachvollziehbaren Besitzer, Mandantenkontext und Sichtbarkeit. Globale offizielle Quellenkataloge und technische Migrationsmetadaten bleiben die dokumentierten Ausnahmen.
- **Backend kontrolliert** Zugriff pro Rolle, eigene Person, Mentorenzuordnung, Schutzklasse und Mandant in jedem Use Case. Frontendfilter sind niemals Sicherheitsgrenze.
- Für Klasse D sowie geteilte Klasse C werden Lese-/Schreibzugriffe mit Actor, Record-ID, Zeitpunkt und Zweck/Operation protokolliert, **niemals** Dokument-/Journalinhalt oder Tokens.
- Verpflichtende Negativtests: fremder Mentor, fremder Mandant, ungültige Session, fehlender CSRF-Schutz, direkte Objekt-ID, fehlende Klassendeklaration.
- **MVP-Gate:** Nachweis der Datenbank-Laufzeitrolle ohne Superuser/BYPASSRLS (Umsetzung aus PR #10). Ohne erfolgreich geprüfte Auth/RLS/Access-Log-Kette keine echten Lernendendaten.
