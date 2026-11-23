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
