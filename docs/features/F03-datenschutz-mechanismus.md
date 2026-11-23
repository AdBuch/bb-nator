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
