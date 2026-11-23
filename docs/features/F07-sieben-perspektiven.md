# F07 – Sieben Perspektiven und Zielmodell

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Spezifiziert | B | [F06](F06-bivo-import.md) |

## Ziel
Ziele aus BiVo, Betrieb, Berufsbildner, Lernendenwünschen, Lehrpfad, üK und Schule fliessen in einen gemeinsamen Plan.

## User Stories
- Als Mentor erfasse ich Betriebs- und Visionsziele.
- Als Lernende/r schlage ich eigene Ziele vor.
- Als Mentor verknüpfe ich Ziele verschiedener Quellen bewusst (keine automatische Gleichsetzung).

## Umfang
- `GoalSource`, `Goal`, `GoalVersion`, `GoalRelation` (n:m, geprüft, mit Herkunft).
- Lernendenwünsche sind von Lernenden vorschlag- und änderbar.

## Nicht im Umfang
- Automatische Zuordnung über ähnliche Namen

## Akzeptanzkriterien
- Ein fiktiver Lernplan zeigt alle sieben Perspektiven (Release-Gate v0.2).

## Datenschutz
Lernendenwünsche gehören den Lernenden (Klasse B, Sichtbarkeit nach F03).

## Offene Fragen
- Semantik von `GoalRelation`: gleichwertig, unterstützt, setzt voraus?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- PRODUCT_VISION.md
- BB_COCKPIT.md §2

## Präzisierung: Zielbeziehungen und Mitwirkung (2026-11-23)

Jede Perspektive bekommt eigene Quelle, Berechtigung und Verantwortlichkeit. Mindestens die Relationen **unterstützt**, **setzt voraus**, **wurde zugeordnet** und **möglicherweise gleichwertig** müssen semantisch getrennt sein. Die letzte Relation erfordert manuelle Bestätigung und Herkunftsverweis; dieselbe Bezeichnung ist kein Nachweis für Gleichwertigkeit.

- Mentor kann den betrieblichen und pädagogischen Zielbestand pflegen; Lernende können Wünsche/Vertiefungen vorschlagen, priorisieren und im Gespräch bestätigen lassen.
- Externe Schul-/üK-Angaben sind als gemeldet/nachgewiesen gekennzeichnet und können offiziellen BiVo-Nachweisen nicht stillschweigend gleichgesetzt werden.
- Jede Zielverknüpfung hat Bearbeiter, Datum, Quelle und Version; Änderungen bleiben historisiert.
- **Akzeptanz:** Ein Demo-Lernprojekt kann gleichzeitig einem BiVo-Ziel, Firmenziel und persönlichem Wunsch zugeordnet sein, ohne dass ein Ziel automatisch als verifiziert gilt.
