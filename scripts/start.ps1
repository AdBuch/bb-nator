$ErrorActionPreference = "Stop"
Set-Location (Resolve-Path (Join-Path $PSScriptRoot ".."))
if (-not (Test-Path ".env")) { throw "Copy .env.example to .env and set POSTGRES_PASSWORD first." }
if ((git branch --show-current) -ne "main") { throw "Starter must be run on main." }
if (git status --porcelain) { throw "Working tree is not clean. Cannot update automatically." }
$remote = git remote get-url origin
if ($remote -notin @("https://github.com/AdBuch/bb-nator", "https://github.com/AdBuch/bb-nator.git", "git@github.com:AdBuch/bb-nator.git")) {
    throw "Unexpected Git remote."
}
git fetch --prune origin main
if ($LASTEXITCODE -eq 0) {
    git merge --ff-only origin/main
    if ($LASTEXITCODE -ne 0) { throw "Fast-forward refused: resolve Git history manually." }
} else {
    Write-Warning "Git fetch failed. Using installed version."
}
python scripts/ensure_env.py
if ($LASTEXITCODE -ne 0) { throw "Could not prepare .env." }
docker compose up --build -d
if ($LASTEXITCODE -ne 0) { throw "Docker Compose failed." }
Write-Host "Open http://127.0.0.1:8088. Admin token in .update-token. Keep terminal open."
python scripts/updater.py
