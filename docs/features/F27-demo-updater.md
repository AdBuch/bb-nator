# F27 – Demo-Modus und lokaler Updater

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2a | Umgesetzt (PR #6) | — | — |

## Ziel
Eine lauffähige Vorführung mit fiktiven Daten und ein Update-Knopf für die lokale Installation.

## User Stories
- Als Interessierte/r starte ich die Demo lokal und probiere Aufgaben aus.

## Umfang
- Vier fiktive Lernende, einfache Aufgaben mit Status und Nachweis, Starter- und Update-Skript auf 127.0.0.1.

## Nicht im Umfang
- Echte Daten
- Betrieb auf erreichbaren Servern

## Akzeptanzkriterien
- Demo startet nur lokal (Sperre folgt in F01).
- CI-Smoke-Test mit `EXPECT_DEMO=1`.

## Datenschutz
Demo-Profil öffnet Schreibzugriffe ohne Login; nie öffentlich betreiben.

## Offene Fragen
- keine

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- RELEASE_0_2A.md
- UPDATES.md
