SHELL := /bin/bash
.PHONY: up down logs smoke backend-test frontend-test frontend-build

up:
	docker compose up --build -d

down:
	docker compose down

logs:
	docker compose logs -f --tail=100

smoke:
	bash scripts/smoke.sh

backend-test:
	cd backend && mvn -B clean verify

frontend-test:
	cd frontend && npm run test:ci

frontend-build:
	cd frontend && npm run build
