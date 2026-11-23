# BB-NATOR Roadmap

**Stand:** 2026-11-23. Verbindliche Release-Gates stehen in [RELEASES.md](requirements/RELEASES.md); diese Roadmap ordnet die [Features](features/README.md) den Releases zu und zeigt den aktuellen Stand. Wochenangaben sind Ziele, keine Zusagen.

## Aktueller Stand

| Bereich | Stand |
| --- | --- |
| v0.1 Foundation | abgeschlossen, in `main` |
| v0.2a Demo ([F27](features/F27-demo-updater.md)) | in `release/0.2-learning-path` (PR #6) |
| Mandanten-Schema und RLS ([F02](features/F02-mandantenfaehigkeit.md)) | Grundlage in `release/0.2-learning-path` (PR #8) |
| Identität und Login ([F01](features/F01-identitaet-anmeldung.md)) | in Arbeit |

## Releases

| Release | Ziel | Features | Gate (Kurzform) |
| --- | --- | --- | --- |
| **v0.2** Seven Perspectives | Echte Benutzer, Datenschutz-Fundament, Ziele, Pläne, Mentor-Cockpit | [F01](features/F01-identitaet-anmeldung.md), [F02](features/F02-mandantenfaehigkeit.md), [F03](features/F03-datenschutz-mechanismus.md), [F05](features/F05-aufbewahrung-auskunft.md) (Grundlage), [F06](features/F06-bivo-import.md), [F07](features/F07-sieben-perspektiven.md), [F08](features/F08-lernplan.md), [F09](features/F09-mentor-cockpit.md), [F10](features/F10-aufgaben-assistent.md), [F11](features/F11-nachweise-bewertung.md) (Teil), [F12](features/F12-lernendenprofil.md) | Fiktiver Plan zeigt alle sieben Perspektiven; Mentor erstellt und weist zielverknüpfte Aufgaben zu; Rollen- und Mandantengrenzen durch Negativtests belegt |
| **v0.3** Daily Companion | Alltag der Lernenden | [F04](features/F04-einwilligungen.md), [F13](features/F13-lernenden-dashboard.md), [F14](features/F14-lernjournal.md), [F15](features/F15-fokus-lernzeit.md) | Lernende schliessen einen Tag ab; Zeitarten getrennt; Opt-in geprüft |
| **v0.4** Connected Learning | Anbindung an bestehende Werkzeuge | [F16](features/F16-jira-integration.md), [F17](features/F17-gitlab-nachweise.md), [F18](features/F18-zeiterfassung.md), [F19](features/F19-schule-uek.md) | Jira-Ticket mit Bestätigung erstellt; GitLab-Nachweise verknüpft; keine Doppelwahrheit |
| **v0.5** Gamification | Motivation ohne Konkurrenz | [F20](features/F20-gamification.md), [F21](features/F21-wissenschecks.md) (Beginn) | Keine Ranglisten, keine Noten aus XP oder Zeit |
| **v0.6** Mentoring & Dokumentation | Formale Begleitung | [F11](features/F11-nachweise-bewertung.md) (Abschluss), [F21](features/F21-wissenschecks.md), [F22](features/F22-checkins-feedback.md), [F23](features/F23-bildungsbericht-dokumente.md) | Bericht als Entwurf mit Freigabe; Dokumente verschlüsselt |
| **v0.7** Smart Learning | Gezielte Unterstützung | [F24](features/F24-hinweise-ki.md) | Hinweise verraten keine Lösung; KI-Sperre für Klasse C und D |
| **v0.8** QV Readiness | Vorbereitung auf das QV | [F25](features/F25-qv-vorbereitung.md) | Bereitschaft mit Nachweisen, keine erfundene Prognose |
| **v1.0** Production Platform | Betriebsreife | [F05](features/F05-aufbewahrung-auskunft.md) (Abschluss), [F26](features/F26-entwicklungs-dashboard.md), [F28](features/F28-betrieb.md) | Backup/Restore, Export und Löschung end-to-end getestet, Audit, Barrierefreiheit |

## Reihenfolge in v0.2

1. **Identität** ([F01](features/F01-identitaet-anmeldung.md)): Laufzeitrolle `bbnator_app`, Demo-Sperre, LDAP-Login mit lokalen Rollen, Testcontainer-LDAP in CI.
2. **Datenschutz-Mechanismus** ([F03](features/F03-datenschutz-mechanismus.md)): Schutzklasse pro Entität (Architekturtest), Sichtbarkeit, Zugriffsprotokoll.
3. **Mandantentrennung** ([F02](features/F02-mandantenfaehigkeit.md)): RLS auf allen fachlichen Tabellen, Fremd-Mandant-Tests (Grundlage erledigt).
4. **Ziele** ([F06](features/F06-bivo-import.md), [F07](features/F07-sieben-perspektiven.md)): BiVo-Import, Zielquellen und Beziehungen.
5. **Pläne** ([F08](features/F08-lernplan.md)).
6. **Mentor-Arbeitsplatz** ([F10](features/F10-aufgaben-assistent.md), [F11](features/F11-nachweise-bewertung.md), [F12](features/F12-lernendenprofil.md), [F09](features/F09-mentor-cockpit.md)).
7. **Abschluss:** synthetische Seed-Daten, Docker-Smoke mit Negativtests, Release-PR nach `main` (menschliche Freigabe).

## Erster nutzbarer MVP
Ende v0.2 mit F01–F03 und F09–F11: Ein Mentor und mehrere Lernende melden sich an, Aufgaben werden mit Zielen erstellt, zugewiesen und mit Nachweis abgeschlossen, alles mit Rollen- und Mandantenprüfung. Pilot zuerst mit fiktiven Daten, danach mit echten Lernenden erst nach eurer datenschutzrechtlichen Prüfung.

## Offene Entscheide
| Thema | Feature |
| --- | --- |
| Stabiler LDAP-Benutzerschlüssel | [F01](features/F01-identitaet-anmeldung.md) |
| Bestimmung des Mandanten beim Login | [F02](features/F02-mandantenfaehigkeit.md) |
| Aufbewahrungsfristen pro Schutzklasse | [F05](features/F05-aufbewahrung-auskunft.md) |
| Information der Erziehungsberechtigten | [F04](features/F04-einwilligungen.md) |
| Quelldatei und Weiterverbreitung BiVo | [F06](features/F06-bivo-import.md) |
| Definition der Kompetenzstufen 0–4 | [F11](features/F11-nachweise-bewertung.md) |
| Jira-EDU-Projekt und Felder | [F16](features/F16-jira-integration.md) |
| KI-Anbieter | [F24](features/F24-hinweise-ki.md) |
