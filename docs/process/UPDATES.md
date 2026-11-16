# BB-NATOR local starter and update button

**Scope:** Single-machine, local-only, synthetic demo. The updater is a host-side process, not a containerized admin service. It is **not suitable for production** without an authenticated deployment controller, audit, rollback, release signing and explicit maintenance policy.

## First start
Clone repo and stay on `main`. Copy `.env.example` to `.env`; replace the database password.

Linux / WSL:
```bash
bash scripts/start.sh
```

Windows PowerShell (Docker Desktop, Git, Python 3):
```powershell
.\scripts\start.ps1
```

The starter checks `origin/main`, fast-forwards the clean local checkout, runs `docker compose up --build -d` and performs the smoke check (Bash starter). Offline Git fetch falls back to the installed checkout with a warning. Existing PostgreSQL volumes are preserved.

## Admin UI
Visit http://127.0.0.1:8088; select **Admin / Updates**. The host creates `.update-token` (chmod 0600 on POSIX), excluded from Git. Copy the token into the admin form, click **Updates prüfen** and, when a new commit exists, **Neue Version installieren**.

The button talks only to `http://127.0.0.1:8766` on the same machine. It authenticates every request with an ephemeral browser-memory token and restricts CORS origins to local UI origins. The fixed update sequence is fetch → fast-forward origin/main → Docker Compose rebuild. No shell commands or Git branch names are accepted from the browser.

## Safety
- **Never** publish ports 8766 or 8088 publicly, forward them over SSH as shared resources, or put a tunnel/public reverse proxy in front of the demo.
- The updater runs with **the local operator's Docker permissions**. Do not run as root; never expose Docker socket to the application containers.
- The update daemon only operates on `main` in a clean checkout with the expected origin. It has no general-purpose deployment API.
- Before any real personal data: implement LDAP sign-in, RBAC, consent and auditing per ADR-0002 and PRIVACY.md.
- An interrupted update may require local inspection; no automatic rollback yet. Git checkout and PostgreSQL volumes are not reset.
- Opening the admin UI from another machine will not work: the update service deliberately binds to local loopback.
- Do not enable `demo` Spring profile on network-accessible deployments. The demo endpoints are unauthenticated by design and contain only synthetic data.

## Test
```bash
python3 -m unittest discover -s scripts -p 'test_*.py'
bash scripts/smoke.sh
```
