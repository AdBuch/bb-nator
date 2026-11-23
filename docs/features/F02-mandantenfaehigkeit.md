# F02 – Mandantenfähigkeit

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | Grundlage umgesetzt (PR #8) | A | — |

## Ziel
Mehrere Lehrbetriebe können dieselbe Installation nutzen, ohne Daten des anderen zu sehen. Lokal läuft genau ein Mandant.

## User Stories
- Als Betreiber richte ich einen Mandanten ein, ohne dass andere Mandanten betroffen sind.
- Als Benutzer sehe ich nie Daten eines anderen Mandanten, auch nicht über direkte IDs.

## Umfang
- `tenant_id` auf jeder fachlichen Tabelle, nie null.
- PostgreSQL Row-Level-Security mit `FORCE` auf allen mandantenbezogenen Tabellen.
- Mandant wird pro Transaktion gesetzt (`TenantScope`); ohne Kontext keine Zeilen.
- Globaler, schreibgeschützter BiVo-Katalog; alle anderen Ziele gehören dem Mandanten.

## Nicht im Umfang
- Mandanten-Onboarding, Abrechnung, Mandantenverwaltung im UI (erst bei zweitem echten Mandanten)

## Akzeptanzkriterien
- Fremd-Mandant kann weder lesen noch schreiben (Integrationstest unter `bbnator_app`).
- Fehlender Mandantenkontext liefert keine Daten.
- Architekturtest: neue fachliche Tabellen ohne RLS-Policy schlagen fehl.

## Datenschutz
Mandantentrennung ist die äusserste Datenschutzgrenze. Wirkt nur, wenn die Anwendung nicht als Superuser verbindet (siehe F01).

## Offene Fragen
- Wie wird der Mandant beim Login bestimmt (Konfiguration, LDAP-Basis-DN, Subdomain)?

## Entscheide
<!-- Datum – Entscheid – wer -->
- —

## Quellen
- ADR-0002
- V3__identity_tenancy.sql
