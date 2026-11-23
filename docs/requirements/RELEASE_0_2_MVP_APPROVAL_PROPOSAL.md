# v0.2 MVP · Fachliche Freigabevorlage

**Status:** Vorschlag zur fachlichen Freigabe, **nicht beschlossen und nicht implementiert**. Stand 2026-11-23.
**Grundlagen:** [ADR-0002](../architecture/ADR-0002-identity-tenancy-data-protection.md), [PRIVACY](PRIVACY.md), [Release-Ziele](RELEASES.md), [Produkt-Backlog](../roadmap/PRODUCT_BACKLOG.md), [BB-Cockpit](BB_COCKPIT.md).

## 0. Geltungsbereich und Abgrenzung

Das v0.2-MVP bildet einen zusammenhängenden, testbaren Lernprozess ab:

**Anmeldung → berechtigte Mentoren-/Lernendenansicht → versioniertes Ziel auswählen → persönliche Aufgabe zuweisen → Nachweis einreichen → Mentor prüft und begründet Kompetenzstand → Fortschritt mit Herkunft anzeigen.**

Ein rein synthetischer Demo-Modus darf früher erprobt werden. **Echte persönliche Ausbildungsdaten dürfen erst nach erfolgreicher Rollen-, Mandanten-, Schutzklassen- und Audit-Prüfung und organisatorischer Freigabe verarbeitet werden.**

| Vorschlag | Bereits festgelegte technische Leitplanke | Diese Vorlage beantragt |
| --- | --- | --- |
| LDAP mit lokalen Rollen | ADR-0002 definiert LDAP-Bind, lokale Rollen, aktive Benutzer, serverseitige Session und CSRF | Umsetzungsumfang für Admin-/Login-UI, Rechte, Negativtests und organisatorische Freigabepunkte |
| Entwicklungsstufen 0–4 | F11 unterscheidet Aufgabe, Evidenz und Kompetenz | Präzise, **interne und noch zu bestätigende** Rubrik und Bewertungsprozess |
| Offizieller BiVo-Katalog | ADR-0002 verlangt lokalen Import statt öffentlichem Quelltext | Schema, Validierung, fachliche Stichprobe und Import-Freigabe |
| Fünf Startvorlagen | F29 Aufgabenbibliothek; F10 Zuweisungsassistent | Konkrete rein synthetische Erstinhalte und Versionierungsregeln |
| MVP-Grenze | RELEASES.md und ROADMAP.md definieren v0.2 | Klaren minimalen End-to-End-Gate mit Nichtzielen |

**Dokumentenrang:** ADR-0002 und PRIVACY.md bleiben gültige, übergeordnete Entscheidungen. Diese Vorlage setzt keine LDAP-Konfiguration, Aufbewahrungsfristen, Einsichtsrechte, Notenregel, Rechtsgrundlage oder BiVo-Textlizenz eigenständig fest.

## 1. F01 · LDAP-Anmeldung und lokale Administration

### Vorgeschlagener Ablauf
1. Eine Person öffnet die Anmeldeseite; die Anwendung zeigt einen generischen Anmeldefehler ohne Konten-Enumeration.
2. Über den vorhandenen `IdentityProvider`-Port wird LDAPS-Bind verifiziert. **Ein erfolgreicher LDAP-Bind allein autorisiert nichts.**
3. Ein vorhandenes lokales Konto wird über einen stabilen, noch festzulegenden LDAP-Schlüssel und den sicheren Mandantenkontext gefunden. Bei gültigem **Erstlogin** entsteht idempotent ein Konto im Status `PENDING` **ohne Rollen**; ein Konto in `DISABLED` bleibt gesperrt. Ausbildungszugriff setzt lokale Freigabe voraus.
4. Der Server erstellt eine neue Session mit `HttpOnly`, `Secure` unter HTTPS und `SameSite=Lax`; Session wird bei Anmeldung erneuert; CSRF-Schutz für zustandsverändernde Requests.
5. Serverseitige Rollen `APPRENTICE`, `MENTOR`, `BB_LEAD`, `ADMIN`, `AUDITOR` und konkrete Zuordnungen regeln die Fachaktionen. Mehrere Rollen sind nur nach geprüftem Bedarf vorgesehen.
6. Das Admin-UI soll `PENDING`/`ACTIVE`/`DISABLED`, Rollen, Mentorenzuordnung, manuelle LDAP-Suche und selektiven Mehrfachimport mit `neu`/`vorhanden`/`Konflikt`-Vorschau sowie einen protokollierten Änderungsverlauf anbieten. Das Anlegen eines **lokalen** Kontos stellt kein neues LDAP-Konto her.
7. Abmelden invalidiert die Session; Deaktivierung entzieht Zugriff auch für noch laufende Sessions nach der zu spezifizierenden Durchsetzung.

### Minimale API-/Datenanforderungen zur Prüfung
- **Vorschlag**: `GET /api/v1/me` mit Benutzer-ID, erlaubten Rollen und Anzeigeinformationen; `POST /api/v1/logout`; `GET/POST/PATCH /api/v1/admin/users` sowie explizites Mapping `user ↔ mentor` nach Freigabe.
- `LocalUser`: ID, Tenant-ID, Directory-Identifier, aktiv, Anzeigename und Rollenbeziehungen. Rollenänderungen haben Actor, Zeitpunkt und Änderungsgrund.
- Kein LDAP-Passwort in DB, API, Logs, Browser-Storage oder Fixtures. LDAP-Konfiguration und Bind-Credentials sind Betreiber-Secrets.
- Bei fehlendem Tenant, unvollständigem Mapping, gesperrtem lokalem Benutzer oder LDAP-Störung: **kein Ausbildungszugriff und kein Fallback auf Demorechte**. Die bewusste Erstanlage eines `PENDING`-Kontos ist davon ausgenommen.

### Verbindliche vorgeschlagene Testfälle
- Erfolgreicher LDAP-Bind + aktiver lokaler Account + richtige Rolle → auf eigene zugelassene Datensätze beschränkt.
- Korrekte LDAP-Credentials ohne lokalen Account → neue `PENDING`-Session nur für den Freigabestatus, keine Ausbildungsberechtigung; deaktivierter Account, falsches Kennwort oder LDAP-Ausfall → kein neuer Login.
- Login-Bruteforce/Drosselung und Enumeration-Schutz werden in einer separaten Security-Review nachgewiesen.
- Ohne Session: Fach-API 401/403, keine personenbezogenen Details; mit fremder Mentorenzuordnung: 403/404 ohne Datenleck.
- Fehlender/falscher CSRF-Token verhindert POST/PATCH/DELETE; Session-Fixation-/Logout-Test.
- Cross-Tenant-ID / fehlender Tenant: 0 Daten, Schreibzugriff abgewiesen; effektiver DB-Benutzer weder Superuser noch BYPASSRLS.
- Nur berechtigte Admins ändern lokale Rollen; Rollenzuweisung und Deaktivierung sind protokolliert.

### Im Gespräch anschliessend ausdrücklich beschlossene F01-Regeln (2026-11-23)
- **v0.2 On-Premises:** Ein Organisationsmandant mit LDAP/LDAPS. Weitere OIDC-Provider und organisationsverwaltete SSO-Konfiguration erst später, aber Identitätsmodell dafür erweiterbar.
- **Erstlogin:** gültiger LDAP-Bind erzeugt bei fehlender lokaler Identität ein rollenloses `PENDING`-Konto; nur Freigabestatus/Logout, keine Ausbildungsdaten.
- **Manuell:** Admin-UI bietet LDAP-Suche, Auswahl, Importvorschau und gezielten Import ohne automatische Rollenvergabe.
- **Attribute:** Anzeigename und E-Mail bei erfolgreichem Login aktualisieren. Rollen, Mitgliedschaft, Mentorenzuordnung und Freigabestatus bleiben lokal; stabile LDAP-ID statt E-Mail-Matching.
- **Verantwortung:** `ADMIN` technische Konfiguration/privilegierte Rechte, `BB_LEAD` fachliche Einladungen, Freigaben und Mentor-/Lernendenrollen; `ADMIN` hat ohne weitere Fachrolle kein allgemeines Ausbildungsdaten-Leserecht.
- **Sessions:** lokale Sperre sofort; Rollenwechsel beim nächsten Request; LDAP-Kontostatus **alle 15 Minuten für aktive Sessions prüfen**; bestätigt gesperrte LDAP-Accounts verlieren alle Sessions. Kein periodischer vollständiger LDAP-Benutzersync.
- **Spätere Cloud:** Organisationserstellung nur nach Plattformadmin-Freigabe; Organisations-SSO ohne Mitgliedschaft erzeugt Beitrittsantrag, keine automatische Rollenvergabe; nicht zugeordnete Benutzer ohne Ausbildungsfunktionen (allenfalls Minigames).
- Einzelheiten, Sicherheitsrisiken, UI-Schritte und Tests sind in [F01](../features/F01-identitaet-anmeldung.md#ausführungsdetails-zum-beschlossenen-ldap-mvp-2026-11-23) nachgeführt. Änderungen am bereits akzeptierten ADR-0002 (der noch «lokaler Benutzer muss existieren» formuliert) bedürfen eines **separaten nachvollziehbaren ADR-Updates**, nicht eines stillen Überschreibens.

### Fachliche Freigabe noch offen
**F01-A** stabiler LDAP-Identifier und Änderungsstrategie; **F01-B** sichere Bestimmung des Mandanten (F02); **F01-C** erste Admin-Kontoanlage, berechtigte Administratoren und Vier-Augen-Kontrolle; **F01-D** erlaubte Rechte pro Rolle, insbesondere BB_LEAD/AUDITOR und Klasse D; **F01-E** konkrete Drosselungs-/Sperrstrategie, Inaktivitäts-/Maximallaufzeit und Verhalten bei LDAP-Ausfall (das **15-Minuten-Statusintervall ist beschlossen**); **F01-F** Betreiber-HTTPS/LDAPS und Secret-Verwaltung sowie verlässliches LDAP-Statusattribut. Diese Antworten werden **nicht** aus dieser Spezifikation abgeleitet.

## 2. F11 · Entwicklungsstufen 0–4 (interne Rubrik, Vorschlag)

Die Skala ist **keine BiVo- oder QV-Note** und kein automatischer Score. Sie bewertet ein definiertes Ziel in einer dokumentierten Situation, nicht die Person insgesamt.

| Stufe | Vorgeschlagene Bedeutung | Beispiel für beobachtbare Evidenz |
| --- | --- | --- |
| **0: Nicht belegt** | Keine ausreichende Beobachtung oder Evidenz vorhanden; sagt **nicht** «nicht fähig» aus. | Noch kein mentor-geprüfter Nachweis zum Ziel |
| **1: Eng begleitet** | Führt Teilhandlungen mit enger, schrittweiser Begleitung aus. | Kann Lösung mit konkreten Hilfen vorführen und die Schritte teilweise erklären |
| **2: Unterstützt** | Bearbeitet bekannte Aufgaben mit gezielter Hilfe; benötigt für wesentliche Entscheidungen Rückfragen. | Abgabe + Test mit dokumentierter Unterstützung und nachvollziehbarer Erklärung |
| **3: Weitgehend selbständig** | Liefert eine angemessene Lösung selbständig und erläutert relevante Entscheidungen, Tests und Grenzen. | Wiederholbarer Nachweis mit eigener Begründung und Review |
| **4: Selbständig und reflektiert** | Beurteilt Qualität, Risiken und Alternativen kritisch und kann die Lösung situationsgerecht anpassen. | Eigenständige Umsetzung mit nachvollziehbaren Qualitätssicherungs- und Trade-off-Entscheidungen |

### Bewertungsworkflow (Vorschlag)
- **Aufgabe:** Entwurf → Zugewiesen → In Arbeit → Review → Erledigt. Der Status alleine ändert keine Kompetenzstufe.
- **Evidenz:** Fehlt → Eingereicht → Geprüft → Verifiziert / Nacharbeit. Dabei gehört ein Nachweis zu einer konkreten Aufgabe und einem konkreten Ziel/Katalogstand.
- **Bewertung:** nur ein entsprechend zugeordneter, berechtigter Mentor vergibt Stufe + Ziel-ID/Version + datierten Nachweis + Beobachtung + Begründung + nächsten Schritt. Eine formelle, getrennte Freigabe für offizielle Berichte bleibt F23.
- **Revision:** frühere Einschätzungen bleiben historisiert; Lernende können ergänzende Erläuterungen oder Widerspruch erfassen; Bearbeitung einer Stufe löscht keine alte Bewertung.
- **Fortschritt:** getrennt `geplant`, `geübt`, `mit Evidenz`, `mentor-verifiziert`. Keine XP-, Zeit-, Commit- oder Jira-Done-Umrechnung in eine Stufe.
- **Datenklasse:** Ausbildungsbewertungen Klasse B nach PRIVACY; fremde Mentoren erhalten keinen Zugriff. Höher klassifizierte Belege bleiben ihrer eigenen Klasse zugeordnet.

### Akzeptanzfälle
1. Jira/Task auf «Done» ohne Mentorentscheidung: **Stufe unverändert**, Ziel nicht verifiziert.
2. Mentor verifiziert ohne Nachweis/Begründung: Validierungsfehler, keine stille Freigabe.
3. Nicht zugeordneter Mentor oder anderer Tenant: weder lesen noch bewerten.
4. Lernende/r sieht eigene Einschätzung, Begründung und Verlauf.
5. Stufe 0 wird im Dashboard als **«keine ausreichende Evidenz»**, niemals als Misserfolg angezeigt.
6. Rückstufung/Änderung erzeugt eine neue nachvollziehbare Revision.

### Fachliche Freigabe noch offen
**F11-A** Bestätigung oder Änderung der fünf Stufen; **F11-B** wer beurteilen darf und ob ein Zweitreview nötig ist; **F11-C** genügende Nachweisqualität / Wiederholungsbeobachtungen; **F11-D** Verwendung in Bildungsberichten und ob Lernende aktiv Stellung nehmen; **F11-E** Präsentation von Gesamtübersichten ohne undurchsichtige Scores.

## 3. F06 · Versionierter offizieller BiVo-2021-Katalog

### Vorgeschlagenes Importmodell
- `CatalogImport`: ID, Herkunft/URL bzw. Datei-Referenz, Herausgeber, offizieller Dokumenttitel, Publikationsstand, Geltungs-/Sprachversion, SHA-256, Importzeit, Operator, Validierungsresultat und Freigabestatus.
- `CatalogVersion`: unveränderliche Versions-ID, Gültigkeit soweit aus Quelle belegbar, Herkunft, Importverweis.
- `CatalogGoal`: stabiler **quellgetreuer** Code, Wortlaut (nur wenn Weiterverwendung freigegeben), Stufe (`Handlungskompetenzbereich`, `Handlungskompetenz`, `Leistungsziel`), Lernort (`Betrieb`, `Berufsfachschule`, `üK` sofern in der Quelle vorhanden), Hierarchie/Eltern-ID, `catalogVersionId`.
- `GoalMapping`: eigenständige tenantbezogene Zuordnung zu individuellen Plänen, Quell-Version und Prüfstatus. Eine Zuordnung **ist keine Kompetenzverifikation**.
- Die **offizielle Katalogbasis bleibt global und read-only**, eigene Firmen-/Mentor-/Lernendenziele sind tenantbezogen (F07).

### Importprozess
1. Admin lädt eine berechtigte veröffentlichte Originaldatei in einer kontrollierten Betreiberumgebung hoch. **Kein Scraping oder erfundene offiziöse Codes.**
2. Dateityp, Format, Encoding, Prüfsumme und Dokument-Metadaten prüfen; bei unsicherer Tabellen-/PDF-Extraktion in manuellen Review statt automatischer Freigabe.
3. Strukturvalidierung: eindeutige Codes **innerhalb des Versions-/Lernort-Kontexts**, Hierarchie, erforderliche Felder, Mehrdeutigkeiten, Duplikate und Verlust von Zielen melden.
4. Importvorschau mit Zählungen, Beispielzielen, Fehlerreport und Freigabe durch zuständige Fachperson.
5. Wiederholter Import gleicher Prüfsumme/Version ist idempotent. Neuer offizieller Stand erzeugt neue Version, **niemals** eine stille Überschreibung historischer Zielverweise.
6. Relevanz/Überleitung zwischen Ständen als separat überprüfte Mapping-Entscheidung; keine automatische Gleichwertigkeit nur aufgrund ähnlicher Bezeichnungen.

### Tests und Akzeptanz
- Synthetische Datei in CI: wiederholter Import identisch, Fehlerfälle für doppelte IDs, fehlende Eltern/unklaren Lernort, kaputte Metadaten und Versionwechsel.
- Öffentliches Repo enthält nur Parser, Strukturschema und **frei erfundene** Testfixtures; kein offizieller Volltext ohne Freigabe.
- Fachliche Stichprobe aus tatsächlich freigegebener Quelle bestätigt Struktur und Ziel-Abdeckung vor Einsatz.
- Individuelle Aufgaben referenzieren unveränderliche Zielversion; Katalogwechsel lässt alte Nachweise intakt.
- Fehlende/mehrdeutige Leistungsziele im UI als **«Import unvollständig»**, nicht als 0 % Fortschritt.

### Fachliche Freigabe noch offen
**F06-A** amtliche Originaldatei und genaues Ausgabeformat; **F06-B** Herausgeber/Publikationsstand und Quellkennungen; **F06-C** Nutzungs-/Weitergaberechte; **F06-D** Freigabeverantwortliche Fachperson; **F06-E** Umgang mit nicht maschinenlesbaren Teilen, Importfehlern und Sprachvarianten. Die konkrete BiVo-Hierarchie wird **erst nach Quelleinsicht** befüllt.

## 4. F29/F10 · Fünf konkrete Startvorlagen

**Status:** vorgeschlagener erster kleiner Beispielsatz; *nicht* als tatsächliche Aufträge, offizielle BiVo-Zuordnungen oder produktive Firmenrichtlinie zu verstehen. Alle Daten, APIs und Repositories für Übungen sind fiktiv/öffentlich freigegeben.

| ID | Vorlagentitel / Zweck | Voraussetzungen | Lieferobjekte | Zentrale Akzeptanzkriterien und erwartete Evidenz |
| --- | --- | --- | --- | --- |
| **T01** | **Java Canvas/Figuren abschliessen:** Methoden, Zustände und verständliche Struktur | Java-Basics | Ausführbares Figuren-/Canvas-Programm und kurze Erklärung | Mehrere Figuren sinnvoll zeichnet/ändert, Eingaben/Fehlerfälle erklärt; Demo und Code-Review |
| **T02** | **Umstieg auf Maven:** reproduzierbarer Build eines bestehenden Java-Projekts | T01 oder gleichwertiges Java-Projekt | `pom.xml`, saubere Standardstruktur, Startanleitung | `mvn clean package` läuft auf frischer Umgebung; keine manuell eingebundenen JARs; Build-Log |
| **T03** | **Erste JUnit-Tests:** Qualitätsidee anhand deterministischer Kernlogik | T02 | Mindestens drei aussagekräftige JUnit-Tests inkl. Rand-/Fehlerfall | Tests prüfen beobachtbares Verhalten, scheitern bei gezielter Fehleränderung; Testprotokoll und kurze Reflexion |
| **T04** | **Git und Code-Review:** Arbeit nachvollziehbar entwickeln | T02/T03 | Git-Repo mit sinnvollen Commits, Branch und kleiner Review-Abgabe | Commit-Historie erklärt; Konflikt oder Änderungswunsch demonstriert; Review-Rückmeldung dokumentiert |
| **T05** | **REST/JSON-API konsumieren:** robusten kleinen Client bauen | T02/T03/T04 oder nachgewiesene Alternative | Java-HTTP-Client gegen öffentliches/synthetisches API, README, Tests mit Mock-Antworten | 2xx, 4xx, 5xx, Timeout, ungültiges JSON/Mapping, Eingabevalidierung und keine Geheimnisse in Git; Review-Nachweis |

### Gemeinsames Vorlagenformat (Vorschlag)
`templateId`, `version`, `title`, `learningIntent`, `context`, `prerequisites`, `deliverables`, `acceptanceCriteria`, `testGuidance`, `hintLevels`, `evidenceExpected`, `suggestedSemester`, `suggestedGoals[]`, `author`, `approvalState`. Keine offiziellen Ziel-IDs ohne verifizierten BiVo-Import.

**Zuweisung:** F10 erstellt pro Lernenden eine eigene, versionierte Aufgabeninstanz; Mentor kann Titel/Kriterien für den Lernkontext anpassen. Aktualisierung der Vorlage ändert bestehende Zuweisungen **nicht** rückwirkend. Freigabepflichtfelder: Zweck, Zielverweise, Abgaben, Kriterien, Bearbeiter, Reviewweg.

**Fachliche Freigabe noch offen:** **F29-A** T01–T05 als erste fünf Vorlagen bestätigen oder ersetzen; **F29-B** Semestervorschläge, Zeitbudgets und Lehrjahr-Voraussetzungen; **F29-C** wer Vorlagen kuratiert, prüft und veröffentlicht; **F29-D** global offene vs. tenantinterne Vorlagen; **F29-E** exakte BiVo-Zuordnungen erst nach Katalogprüfung.

## 5. MVP-Abgrenzung und Reihenfolge (v0.2)

### In Scope: kleinster zusammenhängender Produktionspfad
| Schritt | Funktion | Owner |
| --- | --- | --- |
| 1 | Benutzer/Session/LDAP + lokale Rollen, Mentorenzuordnung, CSRF und Admin-Basis | F01/F02 |
| 2 | Tenant-RLS, Schutzklasse B für Ausbildungsdaten, Besitzer/Sichtbarkeit und erforderliches Access-Logging | F02/F03/F05 (Grundlage) |
| 3 | Freigegebener, versionierter Zielkatalog oder **synthetischer Pilot-Katalog**, sieben Quellperspektiven, Plan mit S1–S8 | F06/F07/F08 |
| 4 | Berufsbildner-Cockpit + Lernendenprofil mit verlinkten Ziel-Lücken und Aufgaben | F09/F12 |
| 5 | Zielgebundene Aufgabe mit Zweck, Kriterien und individueller Zuweisung | F10; optional T01–T05 via F29 |
| 6 | Lernende reichen Artefakt/Link ein, Mentor prüft und dokumentiert separate Evidenz-/Kompetenzentscheidung | F11 |
| 7 | End-to-End-Autorisierung/Datenschutz, Fehlzustände, Wiederholbarkeit, Accessibility, automatisierte Tests | Querschnitt |

**Wichtig:** Ein **synthetischer** Zielkatalog ermöglicht einen früh testbaren UI-/API-Pilot, erfüllt aber **nicht** das Release-Gate «vollständiger offizieller BiVo-Katalog». Ein echtes v0.2-Release darf den offiziellen Import erst als erfüllt markieren, wenn F06 fachlich freigegeben und tatsächlich geprüft ist.

### Nicht Bestandteil des MVP
- Jira/GitLab-Veröffentlichung, MR-Pipeline-Synchronisation und BCS (v0.4).
- Vollständiges Lernjournal, Fokuszeit und Einwilligung für freiwillige Funktionen (v0.3).
- XP/Badges, Wissenschecks und Agentenlabor (v0.5+), KI-Hilfen (v0.7).
- Formale Bildungsbericht-Generierung, Dokumentenablage und offizielle QV-Beurteilung (v0.6–0.8).
- Öffentliche Hosting-/Mandantenadministration, Remote-Update-API, automatische Kompetenznoten.
- Ein «grünes» UI oder erledigtes Ticket **ersetzt nie** eine begründete Mentorentscheidung.

### Geplanter Build-/Freigabeablauf
1. **Auth/Security-Slice:** F01/F02/F03 gemeinsam sicher prüfen; Login-UI, Account-/Mentorverwaltung, Lockout-Strategie (nach Fachentscheid), CSRF, RLS, Rechte-Negativtests.
2. **Katalog-/Ziel-Slice:** Importer mit synthetischen Fixtures; Quellfreigabe ausstehend klar anzeigen; offizielle Inhalte nur nach fachlicher Prüfung.
3. **Plan-Slice:** F07/F08 mit versionierten Zielverweisen, sieben Perspektiven und acht Semestern.
4. **Aufgaben-Slice:** F10/F29, danach F09/F12 als arbeitsfähige Dashboards.
5. **Nachweis-Slice:** F11 mit klar getrennter Evidenz und begründeter Mentorbewertung.
6. **MVP-Abnahme:** fiktiver Mentor und zwei Lernende erfüllen den kompletten Workflow; fremder Mentor/Mandant, CSRF, fehlendes Ziel und manipulierte IDs scheitern; kein personenbezogener Inhalt im öffentlichen Repo.

### Minimaler End-to-End-Abnahmetest
- Mit synthetischen Konten einloggen; Mentor sieht ausschliesslich zugewiesene Lernende.
- Freigegebenes Ziel inklusive Quelle/Version finden; Aufgabe als Entwurf speichern, dann zielgebunden zuweisen.
- Lernende/r sieht ausschliesslich eigene Aufgabe und reicht fiktive Evidenz ein.
- Abschluss ohne Review ändert Kompetenz nicht; Mentor verifiziert **mit Begründung** und Eintrag erscheint mit Autor/Datum.
- Acht-Semester-Plan und sieben Perspektiven zeigen aktuelle Ziele und Nachweise mit korrektem Status.
- Direkte URL auf fremde Person/fremden Mandanten liefert keinen Datenzugriff; nicht authentifizierte Writes, CSRF-freie Writes und fehlerhafte IDs werden abgewiesen.
- Ausfall LDAP, unvollständiger BiVo-Import oder fehlender externer Dienst führt zu ehrlichem Fehler-/Unvollständig-Status, **nicht** zu falschem Fortschritt.
- CI inklusive Maven/Angular/Container-/Security-Tests grün; unabhängiger Review; explizite menschliche Freigabe.

## 6. Produkt- und Organisationsentscheidungen vor Implementierungsfreigabe

| ID | Entscheidung, die eine berechtigte Person treffen muss | Status |
| --- | --- | --- |
| F01-A–F01-F | LDAP-Identität, Tenant-Kontext, Bootstrap-Admin, Rollen-/Klasse-D-Berechtigung, Drosselung, TLS/Secrets | **Offen** |
| F11-A–F11-E | 0–4-Rubrik, Bewertungsrechte, Nachweisstandard, Einspracherecht, Visualisierung | **Vorgeschlagen** |
| F06-A–F06-E | Originalquelle, Version/Format, Textrechte, fachliche Freigabe, Fehler-/Sprachbehandlung | **Offen** |
| F29-A–F29-E | fünf Vorlagen, Zeitbudget, Kurationsrechte, Teilbarkeit, Zielzuordnung | **Vorgeschlagen** |
| F05 / F03 | Aufbewahrungsfristen, Datenschutz-/Organisationsprüfung und Zugriff auf sensible Unterlagen | **Offen** |
| MVP | Kernworkflow und eindeutiges Pilot-Release-Gate | **Vorgeschlagen** |

**Entscheidprotokoll (erst nach Freigabe befüllen):**
`YYYY-MM-DD | Entscheid-ID | Entscheidung | zuständige Rolle/Person | Referenz/Begründung | Auswirkung auf Feature/ADR`.

**Do not:** «vorgeschlagen» stillschweigend zu «beschlossen» oder «umgesetzt» umbenennen. Änderungen an genehmigten ADRs erfordern einen separaten nachvollziehbaren Entscheid.
