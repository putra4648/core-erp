# Troubleshooting

**`Connection to localhost:5432 refused` / `Failed to start quarkus` in tests**
The Docker daemon is not running, so Quarkus dev services could not start PostgreSQL. The log
shows `ComposeDevServicesProcessor ... WARN` right before the failure. Start Docker Desktop and
re-run. Every `@QuarkusTest` fails without it; plain-JUnit tests still pass.

**Startup fails with `relation "..." does not exist`**
The dev services container did not start. Check `docker ps`, or start PostgreSQL manually to
match `application.properties` — `jdbc:postgresql://localhost:5432/erp`, user and password
`erp` / `erp`.

**Startup fails with `column "deleted" of relation "..." contains null values`**
A table predates the soft delete columns and already has rows. Either drop the dev volume and let
Hibernate recreate the schema, or add the columns manually:

```sql
ALTER TABLE <table> ADD COLUMN deleted boolean NOT NULL DEFAULT false;
ALTER TABLE <table> ADD COLUMN deleted_at timestamptz;
ALTER TABLE <table> ADD COLUMN deleted_by varchar(255);
```

**Startup fails on a foreign key after an entity was retyped**
Hibernate does not repoint an existing FK. See the manual DDL in
[database.md](database.md#pending-manual-ddl).

**Redirect loop or 401 on every page**
`AUTH0_ISSUER` is wrong. It must include the scheme and a trailing slash:
`https://<tenant>.eu.auth0.com/`. Also confirm the callback and logout URLs are registered in the
Auth0 application.

**`IllegalStateException: Cannot resolve entity type`**
A repository implements `SoftDeleteRepository` without an explicit type argument. Write
`implements SoftDeleteRepository<Customer>`, not a raw or wildcarded form.

**Vaadin serves a stale UI after a view change**
Dev mode hot-reloads view classes but not `MainLayout` or `Application`. Restart `quarkus:dev`.

**`429 Too many requests` during normal use**
The rate limiter rejected the request. Raise `erp.rate-limit.requests-per-window` /
`erp.rate-limit.burst-capacity` in `application.properties`, or set
`erp.rate-limit.enabled=false` to switch it off. The response carries `Retry-After`.

**Vaadin push channel drops right after enabling rate limiting**
`/connect/*` fell out of `erp.rate-limit.excludedPaths`. The Vaadin push channel is long-lived and
must not be metered; keep it, and `/VAADIN/*` too, in the exclusion list.

**Maven says "Nothing to compile - all classes are up to date" after a source edit**
Incremental compilation did not pick the change up. Force it with `./mvnw clean test-compile`.
