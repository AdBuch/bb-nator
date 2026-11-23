# F04 – Einwilligungsregister

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.3 | Spezifiziert | A | [F03](F03-datenschutz-mechanismus.md) |

## Ziel
Freiwillige Funktionen laufen nur mit dokumentierter, jederzeit widerrufbarer Einwilligung.

## User Stories
- Als Lernende/r schalte ich Fokus-Timer, Stimmungs-Check, Gamification oder KI-Verarbeitung einzeln ein und aus.

## Umfang
- Eintrag pro Lernendem und Zweck, versioniert mit dem angezeigten Text, mit Zeitstempel.
- Widerruf stoppt die Verarbeitung und löscht die unter diesem Zweck erfassten Daten.
- Mentoren sehen nicht, wer abgelehnt hat.

## Nicht im Umfang
- Einwilligung für Klasse B (Grundlage ist das Lehrverhältnis)

## Akzeptanzkriterien
- Ohne Einwilligung bleibt die Funktion aus.
- Widerruf löscht die Daten des Zwecks nachweislich.

## Datenschutz
Bei Minderjährigen ist zu klären, ob Erziehungsberechtigte informiert werden.

## Offene Fragen
- Information der Erziehungsberechtigten bei Minderjährigen?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- PRIVACY.md §3

## Präzisierung: getrennte freiwillige Zwecke (2026-11-23)

Einwilligung separat für Lernjournal-/Mood-Optionen, Fokuszeit, Spiel/XP und AI-Verarbeitung. Die Lernenden sollen frei entscheiden und die eigene aktuelle Einstellung samt dokumentiertem Zwecktext sehen; Ausschalten darf **keine** Benachteiligung in Ausbildung oder Beurteilung erzeugen.

**Akzeptanz:** Widerruf stoppt neue Verarbeitung unmittelbar; bestehende Daten werden entsprechend nachweisbarer Richtlinie gelöscht. Zustimmung ist nicht automatisch durch Anmelden, Aufgabenabgabe oder Lehrvertrag gegeben. Bei Minderjährigen organisatorische/gesetzliche Anforderungen vor Aktivierung klären, nicht im Code vermuten.
