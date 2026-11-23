# F01 – Identität und Anmeldung (LDAP, lokale Rollen)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | In Arbeit | A | [F02](F02-mandantenfaehigkeit.md) |

## Ziel
Benutzer melden sich mit ihrem Firmenkonto an. BB-NATOR erstellt nach erfolgreicher LDAP-Erstanmeldung ein lokales Konto im Status `PENDING`, ohne fachliche Rollen oder Ausbildungszugriff. Berechtigungen werden ausschliesslich lokal nach Freigabe vergeben.

## User Stories
- Als Lernende/r melde ich mich mit meinem Firmenkonto an und sehe nur, was mir zusteht.
- Als Admin lege ich Benutzer an, deaktiviere sie und vergebe Rollen (APPRENTICE, MENTOR, BB_LEAD, ADMIN, AUDITOR).
- Als BB-Lead ordne ich Lernende ihren Mentoren zu.

## Umfang
- LDAP-Bind (LDAPS) prüft nur das Passwort; BB-NATOR speichert keine Passwörter.
- Erfolgreicher erster LDAP-Bind führt zu lokalem Konto `PENDING` ohne Rollen; ein erfolgreicher Bind allein gewährt keinen fachlichen Zugriff.
- Rollen und Mentor-Zuordnung aus der BB-NATOR-Datenbank.
- Serverseitige Session mit `HttpOnly`-/`SameSite=Lax`-Cookie, CSRF-Schutz aktiv.
- Anmeldung als Port `IdentityProvider`, konfigurierbar pro Mandant; LDAP ist der erste Adapter.
- Backend verbindet sich als Datenbankrolle `bbnator_app` (ohne Superuser, ohne BYPASSRLS).

## Nicht im Umfang
- Self-Service-Registrierung
- OIDC/SSO (späterer Adapter)
- Passwort-Reset (Sache des Verzeichnisses)

## Akzeptanzkriterien
- Gültiger erster LDAP-Login legt genau ein lokales `PENDING`-Konto ohne fachlichen Zugriff an (idempotent, kein Doppelaccount); fehlende Freigabe verhindert den Ausbildungszugriff.
- Deaktivierter Benutzer wird abgelehnt.
- Ohne Session liefern Fach-Endpunkte 401/403.
- Schreibende Requests ohne CSRF-Token werden abgelehnt.
- LDAP-Integrationstest mit Testcontainer in der CI.

## Datenschutz
Login-Name und Anzeigename sind Klasse A. Fehlgeschlagene Logins werden ohne Passwort protokolliert.

## Offene Fragen
- Welches LDAP-Attribut ist der stabile Benutzerschlüssel (`uid`, `sAMAccountName`, `entryUUID`)?
- Wer darf Admin sein, und wie wird der erste Admin angelegt?

## Entscheide
<!-- Datum – Entscheid – wer -->
- 2026-11-23 – **Vom Product Owner im Gespräch festgelegt:** v0.2 wird zuerst **On-Premises** für eine Organisation mit **LDAP/LDAPS** betrieben; Cloud-/OIDC-Anbieter sind ein späteres Release und werden nur architektonisch vorbereitet.
- 2026-11-23 – **Festgelegt:** Ein gültiger LDAP-Erstlogin legt ein lokales Konto **ohne fachliche Rolle**, Status `PENDING`, an. Erst Freigabe ermöglicht Ausbildungszugriff. Alternative: gezielter manueller LDAP-Such-/Mehrfachimport mit Vorschau; auch der Import erteilt **keine** Rollen.
- 2026-11-23 – **Festgelegt:** Beim erfolgreichen Login werden Anzeigename und E-Mail aus LDAP aktualisiert; **Rollen, Zuordnungen, Accountstatus und Freigaben werden dadurch nicht überschrieben**. LDAP-Identität wird über einen stabilen, noch zu wählenden Identifier verknüpft, **nicht allein über E-Mail/Anzeigename**.
- 2026-11-23 – **Festgelegt:** Geteilte Verantwortung: `BB_LEAD` darf Lernende/Mentoren einladen, ihre Beitritte freigeben, fachliche Rollen vergeben und Mentoren zuordnen. `ADMIN` verwaltet technische Einstellungen, LDAP, privilegierte Rollen. Organisationsadmin erhält **nicht automatisch** Mentoreneinsichtsrechte. Die präzise Berechtigungsmatrix/Bootstrap-Admin bleiben zu prüfen.
- 2026-11-23 – **Festgelegt:** Keine allgemeine periodische Vollsynchronisation; aktive Sessions werden **alle 15 Minuten auf LDAP-Kontostatus geprüft**. Bestätigte LDAP-Sperre entzieht Sessions und verhindert neue Logins; lokale Deaktivierung sperrt **sofort**. Rollenänderungen müssen spätestens beim nächsten Request gelten.
- 2026-11-23 – **Für späteren Cloud-Betrieb festgelegt, nicht Teil des v0.2-Implementierungsumfangs:** Organisationsanträge benötigen Plattformadmin-Freigabe; gültiges Organisations-OIDC ohne Mitgliedschaft erzeugt einen Beitrittsantrag mit Admin-Freigabe. Nicht zugeordnete Konten haben keine Ausbildungsfunktionen, allenfalls eigenständige Minigames.

## Quellen
- ADR-0002
- PRIVACY.md
- BB_COCKPIT.md §6

## Vorschlag zur fachlichen MVP-Freigabe (2026-11-23)

Die Anmeldung über LDAP und lokal verwaltete Rollen bleibt als Architekturentscheidung in ADR-0002 verankert. **Noch nicht genehmigt** sind die konkrete LDAP-Schlüsselwahl, Admin-Bootstrap, Rechte-Matrix, Rate-Limiting, Mandantenermittlung und Betreiberkonfiguration.

Die vorgeschlagenen UI-/API-Abläufe, Session-/CSRF-Sicherheitsfälle und Testgates stehen in [v0.2 MVP-Freigabevorlage §1](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md#1-f01--ldap-anmeldung-und-lokale-administration). Status dieses Abschnitts: **Freigabevorschlag**, keine Festlegung betrieblicher Identitäten oder Berechtigungen.

## Ausführungsdetails zum beschlossenen LDAP-MVP (2026-11-23)

### Statusmodell / fehlende Berechtigung
- `PENDING`: LDAP-Identität bestätigt; nur eigene Identitäts-/Freigabestatusansicht, Logout; keine Ausbildungs-APIs, keine Daten anderer Personen.
- `ACTIVE`: lokal von `ADMIN` oder berechtigtem `BB_LEAD` mit zulässiger fachlicher Rolle und Mitgliedschaft freigegeben; jeder Request prüft Mandant, Rolle, Zuordnung und Record-Sichtbarkeit.
- `DISABLED`: lokal deaktiviert; alle laufenden Sessions werden sofort invalidiert; kein Login, auch nicht mit gültigem LDAP-Passwort.
- Eine bestätigte LDAP-Deaktivierung **entzieht Sessionzugang**, ohne lokale Rollen oder Historie zu löschen. Ob dafür auch ein dauerhafter lokaler Statuswechsel nötig ist, bleibt offen. LDAP-Ausfall ist ausdrücklich **nicht** gleichbedeutend mit bestätigter Deaktivierung.

### LDAP-Import im Admin-UI
- Geschützte LDAP-Suche (Name, Account, optional Gruppe) mit begrenzten Treffern und sicherem Filter-Escaping; keine LDAP-Credentials im Browser.
- Einzeln/mehrfach auswählen; Vorschau `neu` / `bereits vorhanden` / `Konflikt`; Import bestätigt neue `PENDING`-Konten oder aktualisiert nur erlaubte Identitätsattribute.
- Erneuter Import derselben stabilen LDAP-Identität ist idempotent; Umbenennung erzeugt kein weiteres Konto; fehlerhafte oder mehrdeutige Identifier werden zur manuellen Prüfung ausgesteuert.
- Freigabe-Warteschlange im Admin-UI mit Benutzer, Herkunft, Status, zulässigen fachlichen Rollen, Mentorenzuordnung, Actor, Änderungsgrund und nachvollziehbarer Historie.
- `BB_LEAD` darf fachliche Freigaben durchführen, aber weder `ADMIN` vergeben noch LDAP-Konfiguration ändern. Wer den **ersten** `ADMIN` anlegt und privilegierte Rechte weitergeben darf, bleibt gesondert festzulegen.

### Aktive Sessions und 15-Minuten-Statusprüfung
- Nach erfolgreicher Anmeldung serverseitige Session mit `HttpOnly`, unter HTTPS `Secure`, `SameSite=Lax`, CSRF-Schutz und Session-ID-Rotation. Kein Passwortspeichern.
- **Beschlossenes Intervall:** Status aktiver Sessions alle **15 Minuten** per LDAP prüfen (kein vollständiger Verzeichnisabgleich und keine Rollenübernahme). Nach bestätigt gesperrtem/gelöschtem LDAP-Account alle Sessions invalidieren, neue Anmeldung verweigern.
- **Lokaler** Wechsel zu `DISABLED` wirkt ohne LDAP-Abfrage sofort, auch in anderen Browser-Sessions. Fachliche Rollen werden bei jedem Request aktuell geprüft oder ihr Cache wird synchron invalidiert.
- Wiederanmeldung nach LDAP-Störung nur bei erfolgreichem LDAP-Bind. Für bereits aktive Sessions ist die **Fehler-/Timeout-Policy** bei LDAP-Nichterreichbarkeit (kurzes Schonfenster versus sofortige Sperrung), maximale Inaktivität/absolute Laufzeit und das belastbare Konto-Statusattribut **noch fachlich und technisch zu entscheiden**. Die im Gespräch genannten 30 Minuten Inaktivität und 8 Stunden Gesamtdauer waren **Empfehlungen, keine Beschlüsse**.
- Statusprüfung benötigt ein freigegebenes, technisch zuverlässiges LDAP-Status-/Account-Lookup-Verfahren. Nicht jeder LDAP-Server stellt dieselben Statusattribute bereit; Konfiguration und Tests sind vor Betrieb notwendig.

### Negativtests / Abnahme
1. Erstlogin → `PENDING` ohne Rollen; eigene Statusansicht erlaubt, Ausbildungsendpunkte verweigert; zweiter Login erzeugt keinen Doppelaccount.
2. Mehrfachimport mit gemischten neuen/existierenden Benutzern; Vorschau, Idempotenz und keine stillen Rollenänderungen.
3. Anzeige-/Mail-Änderung aus LDAP aktualisiert bei Login, stabile lokale ID/Rollen/Mentorenzuordnung bleiben bestehen.
4. `BB_LEAD` erteilt fachliche Rollen, kann aber weder Adminrechte noch SSO/LDAP-Settings verwalten.
5. Lokale Sperrung invalidiert sofort alle Sessions; Rollenentzug ist beim nächsten Request wirksam.
6. Nach spätestens 15 Minuten **erfolgreicher LDAP-Statusprüfung** führt bestätigte LDAP-Sperre zu Session-Entzug; unerreichbares LDAP wird als eigenständiger, nach Policy zu behandelnder Zustand getestet.
7. Fremdtenant, fremde Mentorenzuordnung, direkte Objekt-IDs, fehlende/ungültige Session und CSRF-freie Writes bleiben gesperrt.
