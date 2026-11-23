# Features

Ein Dokument pro Feature, damit jedes einzeln besprochen und entschieden werden kann.

## So wird ein Feature besprochen
1. Diskussion als Pull Request, der nur dieses eine Feature-Dokument ändert.
2. Offene Fragen beantworten und als Zeile unter **Entscheide** festhalten (Datum – Entscheid – wer).
3. Status anpassen: `Idee` → `Spezifiziert` → `In Arbeit` → `Umgesetzt`.
4. Ändert ein Entscheid Architektur oder Datenschutz, folgt ein ADR.

Die Reihenfolge und Releases stehen in der [Roadmap](../ROADMAP.md). Grundlagen: [PRODUCT_VISION](../requirements/PRODUCT_VISION.md), [BB_COCKPIT](../requirements/BB_COCKPIT.md), [PRIVACY](../requirements/PRIVACY.md), [ADR-0002](../architecture/ADR-0002-identity-tenancy-data-protection.md).

| ID | Feature | Release | Status |
| --- | --- | --- | --- |
| [F01](F01-identitaet-anmeldung.md) | Identität und Anmeldung (LDAP, lokale Rollen) | v0.2 | Umgesetzt (API); UI offen |
| [F02](F02-mandantenfaehigkeit.md) | Mandantenfähigkeit | v0.2 | Grundlage umgesetzt (PR #8) |
| [F03](F03-datenschutz-mechanismus.md) | Datenschutz-Mechanismus (Schutzklassen, Sichtbarkeit, Zugriffsprotokoll) | v0.2 | Spezifiziert |
| [F04](F04-einwilligungen.md) | Einwilligungsregister | v0.3 | Spezifiziert |
| [F05](F05-aufbewahrung-auskunft.md) | Aufbewahrung, Löschung, Auskunft und Export | v0.2 (Grundlage) – v1.0 | Spezifiziert |
| [F06](F06-bivo-import.md) | BiVo-2021-Zielkatalog (Import) | v0.2 | Spezifiziert |
| [F07](F07-sieben-perspektiven.md) | Sieben Perspektiven und Zielmodell | v0.2 | Spezifiziert |
| [F08](F08-lernplan.md) | Individueller Lernplan und Acht-Semester-Roadmap | v0.2 | Spezifiziert |
| [F09](F09-mentor-cockpit.md) | Mentor-Cockpit (Dashboard) | v0.2 | Spezifiziert (Demo-Vorstufe in PR #6) |
| [F10](F10-aufgaben-assistent.md) | Lernaufgaben-Assistent | v0.2 | Spezifiziert |
| [F11](F11-nachweise-bewertung.md) | Nachweise und Kompetenzbewertung | v0.2 – v0.6 | Spezifiziert |
| [F12](F12-lernendenprofil.md) | Entwicklungsprofil Lernende (Mentorsicht) | v0.2 | Spezifiziert |
| [F13](F13-lernenden-dashboard.md) | Lernenden-Dashboard und Tagesreflexion | v0.3 | Spezifiziert (Lernenden-Seite noch dünn) |
| [F14](F14-lernjournal.md) | Lernjournal | v0.3 | Idee |
| [F15](F15-fokus-lernzeit.md) | Fokus-Timer und Lernzeit | v0.3 | Idee |
| [F16](F16-jira-integration.md) | Jira-EDU-Integration | v0.4 | Spezifiziert |
| [F17](F17-gitlab-nachweise.md) | GitLab-Nachweise | v0.4 | Spezifiziert |
| [F18](F18-zeiterfassung.md) | Zeiterfassung (lesend) | v0.4 | Idee |
| [F19](F19-schule-uek.md) | Berufsfachschule und üK | v0.4 | Idee |
| [F20](F20-gamification.md) | Gamification | v0.5 | Idee |
| [F21](F21-wissenschecks.md) | Wissenschecks und Fragenbank | v0.5 – v0.6 | Idee |
| [F22](F22-checkins-feedback.md) | Check-ins und Mentor-Feedback | v0.6 | Idee |
| [F23](F23-bildungsbericht-dokumente.md) | Bildungsbericht und Dokumente | v0.6 | Spezifiziert |
| [F24](F24-hinweise-ki.md) | Gestufte Hinweise und KI-Vorschläge | v0.7 | Idee |
| [F25](F25-qv-vorbereitung.md) | QV- und IPA-Vorbereitung | v0.8 | Idee |
| [F26](F26-entwicklungs-dashboard.md) | Entwicklungs-Dashboard und Berichte | v1.0 | Idee |
| [F27](F27-demo-updater.md) | Demo-Modus und lokaler Updater | v0.2a | Umgesetzt (PR #6) |
| [F28](F28-betrieb.md) | Betrieb, Backup und Deployment | v1.0 | Idee |
