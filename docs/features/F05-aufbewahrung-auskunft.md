# F05 – Aufbewahrung, Löschung, Auskunft und Export

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 (Grundlage) – v1.0 | Spezifiziert | A–D | [F03](F03-datenschutz-mechanismus.md) |

## Ziel
Daten werden nicht länger als nötig gehalten; Lernende sehen und exportieren, was über sie gespeichert ist.

## User Stories
- Als Lernende/r sehe ich alle über mich gespeicherten Daten und exportiere sie.
- Als Lernende/r beantrage ich die Korrektur eines falschen Eintrags.
- Als Admin lege ich Fristen pro Schutzklasse fest.

## Umfang
- Frist pro Klasse und Mandant, automatischer Löschjob, Backups mit gleicher Rotation.
- Am Ende der Lehre: Export an die Lernenden, Löschung nach Frist.
- Korrektur als Antrag mit Historie, nie stilles Überschreiben.

## Nicht im Umfang
- —

## Akzeptanzkriterien
- Abgelaufene Datensätze sind nach dem Lauf des Jobs entfernt.
- Export enthält alle Datensätze des Lernenden (v1.0 end-to-end getestet).

## Datenschutz
Formale Berichte behalten die Frist, die der Betrieb festlegt.

## Offene Fragen
- Fristen pro Schutzklasse (Standardwerte)?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- PRIVACY.md §4, §5
