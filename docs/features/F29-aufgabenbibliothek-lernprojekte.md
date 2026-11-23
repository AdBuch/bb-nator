# F29 – Aufgabenbibliothek und Lernprojekt-Vorlagen

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.3 (erste Vorlagen), Ausbau v0.4–0.7 | **Idee / zu entscheiden** | A (Vorlagen), B (persönliche Zuweisung) | [F08](F08-lernplan.md), [F10](F10-aufgaben-assistent.md) |

## Ziel
Wiederverwendbare, verständlich formulierte Aufgaben und Projekte nach Voraussetzungen, Lehrjahr/Semester, Ausbildungszielen und Fachthema bereitstellen. **F10** ist der Assistent zum Erstellen/Zuweisen; **F29** ist die kuratierte, versionierte *Inhaltsbibliothek*, kein zweites Ticketsystem.

## Lernpfade als mögliche Vorlagen
1. **Einstieg / S1:** Algorithmen, Java-Grundlagen, OOP, einfache grafische Figuren/Canvas; Abgabe und Reflexion.
2. **Aufbau S1–S2:** bestehende Aufgabe in Maven überführen, erste JUnit-Tests, Git/Commits, REST/HTTP/JSON/API und einfache externe API-Projekte.
3. **S2 AI-Grundlagen:** Modelle/Prompts kritisch prüfen, einfache API-Antworten validieren, Datenschutz und Fehlerfälle.
4. **S3 isoliertes Projekt:** kleines End-to-End-Feature, Go-/HTTP-/Mail-Test-PoC, sichere Integrationsversuche und code-reviewbare Merge Requests.
5. **S4 Testing:** Unit/Integration/E2E, Testdaten, Mocking, Regressionen, Fehleranalyse und aussagekräftige Assertions.
6. **S5 Prompting und Business-Readiness:** Kontextaufbereitung, Quellenprüfung, Tests trotz AI-Unterstützung, Qualitätsgates.
7. **S6 produktiver Alltag:** Java/Spring, Angular, Datenbank, CI/CD, Container, Deployment, Incident-Analyse, Reviews.
8. **S7 Verantwortung:** Planung, Security, Datenklassifizierung, eigenständige technische Entscheidungen, Mentoring und Review.
9. **S8 IPA:** Probeaufgabe, Dokumentation, Testkonzept, Präsentation und Fachgespräch.

Diese Sequenz ist ein **interner, individualisierbarer Lernpfad**, keine behauptete offizielle Zuteilung von BiVo-Zielen zu Semestern. Themen dürfen je nach Vorwissen und Schul-/üK-Termin früher oder später stattfinden.

## Vorlagenmodell
`TaskTemplate`: ID, Version, Titel, Ausbildungsabsicht, Kontext, Voraussetzungen, Fachthema, Schwierigkeit, geschätzte Lernzeit/Story Points (optional), Abgaben, Akzeptanzkriterien, Testhinweise, gestufte Hilfe, erwarteter Nachweis, Zielvorschläge, Quelle/Autor, Freigabestatus. Änderungen an Vorlagen ändern bestehende Zuweisungen nicht rückwirkend. Persönliche Aufgaben sind Klasse B; neutrale globale Templates ohne Firmendaten Klasse A.

## Beispiele aus früheren Lernaufgaben (nur Muster)
- Canvas/Figuren fertigstellen → Maven-Umstieg → JUnit-Konzept, danach Git und REST.
- API-Konsument mit Java und einem öffentlichen Test-API-Dienst, danach optional AI-API **ohne echte persönliche Daten oder Credentials im Repository**.
- Test-Mailserver / Mail-Handler als **synthetischer** Go-Experimentierauftrag; keine echten Mailinhalte.
- Agenten-Proof-of-Concept mit MCP Client/Server/Tools/Resources/Prompts (siehe [F31](F31-agenten-labor.md)).
- Integrationstests, REST, Docker und CI mit synthetischen Daten, nicht in Live-Bank-/Versicherungssystemen.

## Akzeptanzkriterien
- Mentor sucht/filtert nach Semester, Voraussetzung, Fachthema und verknüpften Lernzielen.
- Mentor kann Vorlage bearbeiten, vorschauen und über F10 einer Person/Kohorte zuweisen; individuelle Fortschritte bleiben getrennt.
- Lernende können eigene Projektideen vorschlagen; Mentor stimmt Lernziel-Eignung ab.
- Jede Vorlage enthält überprüfbare Abgabe, Qualitäts-/Testkriterien und Reflexionsfrage.
- Niemals reale Firmensysteme, private Repos, Secrets oder proprietäre Übungsinhalte ins öffentliche Repo kopieren.

## Offene Entscheide
Erste 5–10 Vorlagen, interne vs. öffentlich geteilte Bibliothek, Versionierung, kuratorische Freigabe und optionale Jira-EDU-Template-Synchronisation.

## Quellen
Frühere Lehrlingsbetreuung-Ideen (Canvas/Maven/JUnit/API/Go/MCP) und `PRODUCT_VISION.md`, `BB_COCKPIT.md`.

## Vorschlag: fünf MVP-Startvorlagen (2026-11-23)

In der [v0.2 MVP-Freigabevorlage §4](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md#4-f29f10--fünf-konkrete-startvorlagen) sind als **noch freizugebende** erste Aufgaben vorgesehen: **T01 Java Canvas/Figuren**, **T02 Maven-Umstieg**, **T03 JUnit-Tests**, **T04 Git & Code Review**, **T05 REST/JSON-API-Client**. Jede Vorlage erhält Ziel, Voraussetzungen, Lieferobjekte, aussagekräftige Abnahmekriterien, Test-/Reflexionshinweise und eine Version. Keine offiziellen BiVo-IDs ohne freigegebenen Katalog; keine Firmeninterna. Die umfangreichere Bibliothek bleibt unabhängig davon ab v0.3 geplant.
