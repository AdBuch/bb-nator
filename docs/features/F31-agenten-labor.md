# F31 – Lernprojekt AI-Agenten und MCP-Labor

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.5–v0.7 (Lernpfadinhalt, kein Plattform-Kern) | **Idee** | B für Ergebnisse, A für neutrale Beispiele | [F29](F29-aufgabenbibliothek-lernprojekte.md), [F24](F24-hinweise-ki.md) |

## Abgrenzung
**F31** beschreibt *Lernaufgaben, in denen Lernende eigene AI-Agenten und MCP-Komponenten entwickeln*. **F24** beschreibt *die KI-Unterstützung des BB-NATOR-Portals für Lernende*. Beides nicht vermischen.

## Lernziele / mögliche Übungen
- Funktionsweise von LLM, Prompts und Datenfluss kritisch erklären; Halluzinationen und Modellgrenzen erkennen.
- MCP Client/Server/Tools/Resources/Prompts vergleichen, Datenberechtigungen und Trust Boundaries skizzieren.
- Ein kleines lokal oder in einer kontrollierten Sandbox laufendes MCP-Tool entwerfen, testen und dokumentieren.
- Unterschied zwischen direktem Modellaufruf, tool-basiertem Agenten und deterministischem Workflow nachweisen.
- Tests für Fehlerfälle, Rate Limits, Timeouts, erlaubte Tools und fehlende Credentials; über MR reviewen.
- Reflektieren, welchen Teil ein Agent erledigt, wie Ergebnisse verifiziert werden und wann menschliche Freigabe nötig ist.

## Beispiel-Projektpfade
1. Analyse/MCP-Grundlagen → kleines Tool/Client-Server-PoC.
2. Spring-AI als *Recherche-/Integrationsoption*, nicht als Pflichtframework; verglichen mit einfachem Java und lokalem Agent.
3. Dokumentations-/Ticketkontext mit ausschließlich synthetischen Beispielen, ohne unternehmensinterne Repos/Inhalte.
4. Go oder Java als Sprache je nach Voraussetzungen, Infrastruktur und Projektbudget; nicht autonom komplexe produktive Workflows ausführen.

## Akzeptanzkriterien
- Mentor kann ein Projekt als Version aus F29 zuweisen und nacheinander Wissen, Implementierung und Review prüfen.
- Mindestens ein positiver und zwei negative Sicherheits-/Fehlertests und eine manuelle Erklärung der Architektur.
- Kein externer KI-Aufruf mit Klasse C/D, Firmeninterna, Quellcode oder persönlichen Ausbildungsdaten ohne eine genehmigte Policy.
- Kein AI-Agent darf ohne explizite Freigabe Releases mergen oder produktive Systeme bedienen.

## Offene Entscheide
Für welche Lehrjahre und Zeitbudgets eine Agentenübung vorgesehen ist, welche lokalen Modelle und Sandbox-Rechte verwendet werden, welches Projekt zuerst kommt.

## Quellen
Frühere Ideen für MCP, Spring-AI und AI-Agenten-Projekte; TECHNOLOGY.md, PRODUCT_VISION.md, PRIVACY.md.
