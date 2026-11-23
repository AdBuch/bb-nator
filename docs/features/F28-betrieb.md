# F28 – Betrieb, Backup und Deployment

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v1.0 | Idee | A | — |

## Ziel
Sicherer lokaler Betrieb mit Backup, Wiederherstellung, Monitoring und TLS.

## User Stories
- Als Betreiber stelle ich ein Backup wieder her und prüfe es.

## Umfang
- Backup und Restore getestet, TLS, Monitoring, Release-Signierung, Rollback für den Updater.

## Nicht im Umfang
- —

## Akzeptanzkriterien
- Wiederherstellung ist dokumentiert und geprüft.

## Datenschutz
Backups folgen den Löschfristen (F05).

## Offene Fragen
- Wer betreibt die Installation, und wo?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- RELEASES.md v1.0
- UPDATES.md

## Präzisierung: sichere Betriebsübergabe (2026-11-23)

- **Deployment-Umgebungen:** lokale Demo (synthetisch), interne Pilotumgebung (LDAP/RBAC/Tenant/Datenschutz), gehärtete Produktion. Demo-Profil mit unauthentifizierten Schreibzugriffen niemals publik machen.
- **Update-Sicherheit:** festgelegte, geprüfte Release-Version statt unkontrolliertem neuesten Branch-Commit, Backup vor Flyway-Schemamigration, kontrollierte Neustarts, Gesundheits-/Smoke-Checks und dokumentierter Rollback.
- **Betriebsfähigkeit:** versionierte Konfiguration, Secret-Rotation, Logging ohne Personendaten, Monitoring/Alarmierung, Migrations-/Restore-Proben, dokumentierte Notfallprozesse.
- **Datenschutz:** Zweckbindung, Löschfristen, Zugriffsprotokolle, Klasse-D-Dokumentverschlüsselung und Datenschutzprüfung vor realem Pilot.
- **Akzeptanz:** Wiederherstellung nach fehlgeschlagenem Update mit dokumentierter Datenintegrität; kein localhost-only Updatebutton als Internet-Admin ausgegeben.
