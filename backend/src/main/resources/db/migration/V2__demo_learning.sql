-- Fictional showcase records only. No real apprentices, grades, credentials or company information.
CREATE TABLE demo_learner (
 id UUID PRIMARY KEY,
 tenant_id TEXT NOT NULL,
 display_name VARCHAR(80) NOT NULL,
 semester SMALLINT NOT NULL CHECK (semester BETWEEN 1 AND 8)
);
CREATE TABLE demo_task (
 id UUID PRIMARY KEY,
 tenant_id TEXT NOT NULL,
 learner_id UUID NOT NULL REFERENCES demo_learner(id),
 title VARCHAR(160) NOT NULL,
 learning_goal VARCHAR(160) NOT NULL,
 status VARCHAR(24) NOT NULL DEFAULT 'ASSIGNED'
   CHECK (status IN ('ASSIGNED','IN_PROGRESS','IN_REVIEW','DONE')),
 evidence VARCHAR(24) NOT NULL DEFAULT 'MISSING'
   CHECK (evidence IN ('MISSING','SUBMITTED','VERIFIED'))
);
CREATE INDEX demo_task_tenant_learner ON demo_task(tenant_id,learner_id);

-- Tenant isolation for the showcase tables: even owner connections obey RLS.
ALTER TABLE demo_learner ENABLE ROW LEVEL SECURITY;
ALTER TABLE demo_learner FORCE ROW LEVEL SECURITY;
CREATE POLICY demo_learner_tenant ON demo_learner
  USING (tenant_id = current_setting('app.tenant_id',true))
  WITH CHECK (tenant_id = current_setting('app.tenant_id',true));
ALTER TABLE demo_task ENABLE ROW LEVEL SECURITY;
ALTER TABLE demo_task FORCE ROW LEVEL SECURITY;
CREATE POLICY demo_task_tenant ON demo_task
  USING (tenant_id = current_setting('app.tenant_id',true))
  WITH CHECK (tenant_id = current_setting('app.tenant_id',true));

-- Seed through a temporary migration-local tenant context.
SELECT set_config('app.tenant_id','demo',true);
INSERT INTO demo_learner(id,tenant_id,display_name,semester) VALUES
 ('00000000-0000-4000-8000-000000000001','demo','Demo Alex',2),
 ('00000000-0000-4000-8000-000000000002','demo','Demo Kim',2),
 ('00000000-0000-4000-8000-000000000003','demo','Demo Robin',6),
 ('00000000-0000-4000-8000-000000000004','demo','Demo Sam',6);
INSERT INTO demo_task(id,tenant_id,learner_id,title,learning_goal) VALUES
 ('10000000-0000-4000-8000-000000000001','demo','00000000-0000-4000-8000-000000000001','Java-Methoden und JUnit','Testbare Methoden entwickeln'),
 ('10000000-0000-4000-8000-000000000002','demo','00000000-0000-4000-8000-000000000003','REST API Review','REST-Schnittstellen beurteilen');
