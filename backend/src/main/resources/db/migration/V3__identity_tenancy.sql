-- v0.2 step 1+3: tenants, local users, roles and mentor assignments.
-- Row-level security is FORCEd on every tenant-scoped table: a missing
-- app.tenant_id setting yields zero rows (fail-closed), even for the owner role.
-- No personal data is stored in this migration; the tenant table holds only technical metadata.

CREATE TABLE tenant (
  id         TEXT PRIMARY KEY CHECK (id ~ '^[a-z0-9][a-z0-9-]{1,62}$'),
  name       VARCHAR(120) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE app_user (
  id           UUID PRIMARY KEY,
  tenant_id    TEXT NOT NULL REFERENCES tenant(id),
  ldap_subject VARCHAR(255) NOT NULL,
  display_name VARCHAR(120) NOT NULL,
  active       BOOLEAN NOT NULL DEFAULT TRUE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (tenant_id, ldap_subject)
);

CREATE TABLE role_assignment (
  id        UUID PRIMARY KEY,
  tenant_id TEXT NOT NULL REFERENCES tenant(id),
  user_id   UUID NOT NULL REFERENCES app_user(id),
  role      VARCHAR(24) NOT NULL CHECK (role IN ('APPRENTICE','MENTOR','BB_LEAD','ADMIN','AUDITOR')),
  UNIQUE (tenant_id, user_id, role)
);

CREATE TABLE mentor_assignment (
  id           UUID PRIMARY KEY,
  tenant_id    TEXT NOT NULL REFERENCES tenant(id),
  mentor_id    UUID NOT NULL REFERENCES app_user(id),
  apprentice_id UUID NOT NULL REFERENCES app_user(id),
  CHECK (mentor_id <> apprentice_id),
  UNIQUE (tenant_id, mentor_id, apprentice_id)
);

CREATE INDEX app_user_tenant ON app_user(tenant_id);
CREATE INDEX role_assignment_tenant_user ON role_assignment(tenant_id, user_id);
CREATE INDEX mentor_assignment_tenant_apprentice ON mentor_assignment(tenant_id, apprentice_id);

-- Tenant isolation: one identical policy per scoped table.
ALTER TABLE app_user ENABLE ROW LEVEL SECURITY;
ALTER TABLE app_user FORCE ROW LEVEL SECURITY;
CREATE POLICY app_user_tenant ON app_user
  USING (tenant_id = current_setting('app.tenant_id', true))
  WITH CHECK (tenant_id = current_setting('app.tenant_id', true));

ALTER TABLE role_assignment ENABLE ROW LEVEL SECURITY;
ALTER TABLE role_assignment FORCE ROW LEVEL SECURITY;
CREATE POLICY role_assignment_tenant ON role_assignment
  USING (tenant_id = current_setting('app.tenant_id', true))
  WITH CHECK (tenant_id = current_setting('app.tenant_id', true));

ALTER TABLE mentor_assignment ENABLE ROW LEVEL SECURITY;
ALTER TABLE mentor_assignment FORCE ROW LEVEL SECURITY;
CREATE POLICY mentor_assignment_tenant ON mentor_assignment
  USING (tenant_id = current_setting('app.tenant_id', true))
  WITH CHECK (tenant_id = current_setting('app.tenant_id', true));
