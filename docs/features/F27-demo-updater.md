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

## Präzisierung: Reifegrad und offene Sicherheit (2026-11-23)

Die lokale Demo und der manuell gestartete Updater sind laut PR #6 auf dem Release-Branch implementiert. Das ist **kein** produktionsreifer, zentraler Deployment-Dienst. Noch offen: echte Anmeldung, sichere Adminrechte, Rollback bei abgebrochenem Build, Wartungsfenster, Update-Status über den Neustart, signierte/versionierte Releases, Schutz vor fremder Exposition.

Der Starter holt bei Start Updates von erlaubtem Git-Remote und baut lokale Container. Der Button ist **nur vom selben Rechner** erreichbar (127.0.0.1, Token im Browser-Speicher). Dies nicht als Remote-Admin-Funktion anpreisen.

**Akzeptanz für späteren Produktionsmodus:** nur autorisierte Administratoren, überprüfte Releases, Backups vor Schemaänderungen, klarer Rollback-/Recovery-Prozess und keine personalisierten Daten im öffentlichen Demo-Modus. Siehe F28.
