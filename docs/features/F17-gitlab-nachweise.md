# F17 – GitLab-Nachweise

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.4 | Spezifiziert | B | [F11](F11-nachweise-bewertung.md) |

## Ziel
Merge Requests, Reviews und Pipelines werden als mögliche Nachweise verknüpft.

## User Stories
- BB-08: Als Mentor sehe ich MR- und Pipeline-Verweise als mögliche Nachweise.

## Umfang
- Direkter Spring-Adapter mit Gruppen- und Projekt-Scope, Metadaten statt Quellcode.

## Nicht im Umfang
- Quellcode-Import

## Akzeptanzkriterien
- Ein verknüpfter MR erscheint als Nachweis mit Status 'Eingereicht', nicht 'Verifiziert'.

## Datenschutz
Keine Quellcode-Inhalte in der Datenbank.

## Offene Fragen
- keine

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- INTEGRATION_AND_REUSE_PLAN.md

## Präzisierung: GitLab als potenzielle Evidenz (2026-11-23)

- Direkter Spring-Adapter mit erlaubten Gruppen/Projekten, Leserechten und personenbezogener Zuordnung über explizite externe Benutzer-ID.
- Metadaten: Projekt-ID, MR/Commit-Verweis, Autor/Reviewer, Pipeline-/Reviewstatus, URL, Zeitpunkt und Datenfrische. Keine Diff-/Quellcode-Inhalte oder Credentials standardmässig importieren.
- Merge Request und bestandene Pipeline zeigen technische Aktivität, **nicht automatische fachliche Kompetenz**. Mentor kann Evidenz mit Ziel/Task verbinden und unter F11 prüfen.
- **Akzeptanz:** 401/403/429, unbekannter Autor, Rate Limit, Pagination und veralteter Cache führen zu transparentem Fehlerstatus. Keine fremden oder nicht freigegebenen Projektresultate.
