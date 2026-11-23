# F06 – BiVo-2021-Zielkatalog (Import)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Spezifiziert | A | [F02](F02-mandantenfaehigkeit.md) |

## Ziel
Die offiziellen Handlungskompetenzen und Leistungsziele stehen versioniert und nachvollziehbar zur Verfügung.

## User Stories
- Als Admin importiere ich den Bildungsplan aus der offiziellen Datei und sehe Quelle, Version und Prüfsumme.
- Als Mentor finde ich Ziele nach Kompetenzbereich und Semester.

## Umfang
- Import beim Einrichten aus der veröffentlichten Quelldatei, mit Quelle, Version, Abrufdatum und Prüfsumme.
- Globaler, schreibgeschützter Katalog; neue Versionen ersetzen alte nicht, sondern werden daneben geführt.
- Im öffentlichen Repo nur Importer und synthetische Testdaten.

## Nicht im Umfang
- Bearbeitung offizieller Ziele

## Akzeptanzkriterien
- Erneuter Import derselben Datei ist idempotent.
- Ziel-IDs bleiben über Versionen nachvollziehbar.

## Datenschutz
Keine Personendaten.

## Offene Fragen
- Welche Datei und welches Format liefert der Bund (PDF, XLSX, XML)?
- Ist Weiterverbreitung des Textes erlaubt?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- ADR-0002 §4
- PRODUCT_VISION.md

## Präzisierung: Vollständigkeit und Nachvollziehbarkeit (2026-11-23)

- Ein Importpaket muss die **offizielle Herkunft**, Herausgeber, Dokumenttitel, Publikationsstand, Dateiprüfsumme, Abrufdatum, Geltungsbereich (Betrieb/Schule/üK) und erkennbare Ziel-IDs enthalten. PDF- oder Tabellenexport nur mit überprüfbarer Struktur, sonst manuelle Freigabe.
- Unterscheide Handlungskompetenzbereich, Handlungskompetenz, Leistungsziel und Lernort. Keine Vermengung von betrieblichem Nachweis mit schulischer Modulabsolvierung.
- Prüfe Importer anhand einer kleinen nachvollziehbaren Stichprobe aus der offiziell freigegebenen Quelle und zeige fehlende/mehrdeutige Ziele im Fehlerreport.
- Ein Zielkatalog ist versioniert und read-only; frühere Zuweisungen behalten ihren ursprünglichen Zielstand. Überleitungen zwischen Versionen sind eigene überprüfte Zuordnungen.
- **Offen:** Quellen und Textweitergaberechte juristisch/organisatorisch klären, bevor offizielle Volltexte im öffentlichen Repo erscheinen. Vorher nur synthetische Testdaten.

## Vorschlag zur fachlichen MVP-Freigabe (2026-11-23)

[Versionierter BiVo-Katalog, Importschema, Validierungsworkflow und Akzeptanztests](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md#3-f06--versionierter-offizieller-bivo-2021-katalog) sind als **Freigabevorschlag** präzisiert. Keine offiziellen Codes oder Volltexte werden ohne geprüfte Quelle und Weitergaberechte ins öffentliche Repository aufgenommen. Die konkrete Quelle, Version, Freigabeverantwortung und Behandlung von Quellfehlern bleiben offen.
