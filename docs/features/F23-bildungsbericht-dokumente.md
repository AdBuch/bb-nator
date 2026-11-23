# F23 – Bildungsbericht und Dokumente

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.6 | Spezifiziert | D | [F03](F03-datenschutz-mechanismus.md), [F11](F11-nachweise-bewertung.md), [F22](F22-checkins-feedback.md) |

## Ziel
Der Semester-Bildungsbericht entsteht als Entwurf aus vorhandenen Daten; Dokumente sind geschützt abgelegt.

## User Stories
- BB-09: Als berechtigter BB erstelle und exportiere ich einen Bildungsbericht mit Nachweisverweisen.

## Umfang
- Entwurf, Review und explizite Freigabe; verschlüsselte Ablage für Zeugnisse und Berichte.

## Nicht im Umfang
- Automatisches Einreichen

## Akzeptanzkriterien
- Bericht wird erst nach expliziter Freigabe final.
- Dokumente der Klasse D verschlüsselt.

## Datenschutz
Klasse D, Zugriff protokolliert.

## Offene Fragen
- Wie verhält sich der Bericht zum offiziellen Semesterformular?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- RELEASES.md v0.6
- PRIVACY.md

## Präzisierung: Ausbildungsdokumentation und Zeugnisse (2026-11-23)

- Bildungsberichte, Lehrzeugnisse, Lernnachweise, Zertifikate und Schul-/üK-Unterlagen sind **unterschiedliche Dokumenttypen** mit Herkunft, Version, Autor, Freigabestand, Zugriffsrecht, Aufbewahrungsregel.
- Berichtsgenerator ordnet Ziele, geprüfte Evidenz, Mentor-/Lernendenfeedback und vereinbarte Folgeschritte zu. Er produziert nur einen **bearbeitbaren Entwurf**; formale Prüfung und Freigabe bleiben Menschen.
- Die offiziellen Berichtsvorlagen und zulässige Weitergabe sind gesondert zu prüfen. Keine erfundenen amtlichen Beurteilungsfelder und keine automatische Abgabe.
- **Akzeptanz:** Klasse-D-Dateien werden verschlüsselt gespeichert, jeder Zugriff geloggt, Zugriffstests für fremde Mentoren/andere Mandanten negativ; Export berücksichtigt Freigaben und Datenschutz.
