# F18 – Zeiterfassung (lesend)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.4 | Idee | D | [F03](F03-datenschutz-mechanismus.md) |

## Ziel
Projekt- und Zeitkontext aus der Firmen-Zeiterfassung lesend einbinden.

## User Stories
- Als Mentor sehe ich zusammengefasste Zeitsalden meiner Lernenden, wenn freigegeben.

## Umfang
- Nur lesend über eine dedizierte, geprüfte Schnittstelle; Vertrag und Mock zuerst.

## Nicht im Umfang
- Schreibende Buchungen
- Live-Zugriff vor Prüfung der Schnittstelle

## Akzeptanzkriterien
- Ohne Freigabe kein Live-Adapter, nur Mock.

## Datenschutz
Absenzen und Salden sind Klasse D.

## Offene Fragen
- Welche Zeiterfassung und welche Freigabe gelten?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- INTEGRATION_AND_REUSE_PLAN.md

## Präzisierung: BCS-Agent-Grenze und Lernzeit (2026-11-23)

- **BCS/Projectron** bleibt die kanonische Zeiterfassung; BB-NATOR nutzt allenfalls einen dedizierten, vorher auditierten **BCS-Agent-API-Vertrag** für berechtigte *lesende* Lernenden-/Wochen-/Monatsübersichten. Kein Tinyops als Zwischenserver.
- Prüfen: reale Middleware, Mandanten- und Rollenfilter, zugelassene Personen, Datensparsamkeit, Netzwerk-Topologie, Test-/Fehlerverhalten. Dass Tinyops einen BCS-Client besitzt, belegt noch keine sichere Mehrbenutzer-API.
- **Begriffe strikt trennen:** gebuchte Arbeitszeit, selbstberichtete Lernzeit und freiwillige Fokus-Sessions; keine Addition als ein Produktivitätsscore.
- Ferien, Saldi und Absenzen dürfen nur mit expliziter Freigabe und zweckgebunden erscheinen (Schutzklasse D); keine automatisch ausgelösten Buchungen.
- **Akzeptanz:** ohne genehmigte Anbindung nur synthetischer Mock. Fehler 'nicht verfügbar', nicht 0 Stunden. Keine automatische Bewertung aus Zeitdaten.
