# F01 – Identität und Anmeldung (LDAP, lokale Rollen)

| Release | Status | Schutzklassen | Abhängig von |
| --- | --- | --- | --- |
| v0.2 | In Arbeit | A | [F02](F02-mandantenfaehigkeit.md) |

## Ziel
Benutzer melden sich mit ihrem Firmenkonto an. BB-NATOR kennt nur Benutzer, die lokal angelegt und aktiv sind, und vergibt Rollen selbst.

## User Stories
- Als Lernende/r melde ich mich mit meinem Firmenkonto an und sehe nur, was mir zusteht.
- Als Admin lege ich Benutzer an, deaktiviere sie und vergebe Rollen (APPRENTICE, MENTOR, BB_LEAD, ADMIN, AUDITOR).
- Als BB-Lead ordne ich Lernende ihren Mentoren zu.

## Umfang
- LDAP-Bind (LDAPS) prüft nur das Passwort; BB-NATOR speichert keine Passwörter.
- Lokaler Benutzer muss existieren und aktiv sein; ein erfolgreicher Bind allein gewährt nichts.
- Rollen und Mentor-Zuordnung aus der BB-NATOR-Datenbank.
- Serverseitige Session mit `HttpOnly`-/`SameSite=Lax`-Cookie, CSRF-Schutz aktiv.
- Anmeldung als Port `IdentityProvider`, konfigurierbar pro Mandant; LDAP ist der erste Adapter.
- Backend verbindet sich als Datenbankrolle `bbnator_app` (ohne Superuser, ohne BYPASSRLS).

## Nicht im Umfang
- Self-Service-Registrierung
- OIDC/SSO (späterer Adapter)
- Passwort-Reset (Sache des Verzeichnisses)

## Akzeptanzkriterien
- Login mit gültigem Konto, aber ohne lokalen Benutzer wird abgelehnt.
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
- —

## Quellen
- ADR-0002
- PRIVACY.md
- BB_COCKPIT.md §6
