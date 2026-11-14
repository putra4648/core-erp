# CORE ERP

Warehouse and procurement ERP — Quarkus 3 backend, Vaadin 25 server-side UI, PostgreSQL.

A modular monolith: 16 business modules, each owning its `model` / `repository` / `service` /
`views` packages. Services are the unit of reuse — a module reaches into another module through
its service layer, not its tables.

Rough shape today: 50 repositories, 41 services, 32 views across 19 routes.

## Documentation

| Document                                           | Covers                                          |
| -------------------------------------------------- | ----------------------------------------------- |
| [docs/setup.md](docs/setup.md)                     | Prerequisites, environment, dev mode, builds    |
| [docs/architecture.md](docs/architecture.md)       | Module layout, layering rules, views and routes |
| [docs/database.md](docs/database.md)               | Schema management, soft delete, manual DDL      |
| [docs/conventions.md](docs/conventions.md)         | Entities, money fields, Excel export            |
| [docs/testing.md](docs/testing.md)                 | Test classes, styles, what needs a database     |
| [docs/troubleshooting.md](docs/troubleshooting.md) | Startup and auth errors, and their fixes        |

---

## Stack

| Component     | Version                 | Notes                               |
| ------------- | ----------------------- | ----------------------------------- |
| Java          | 25                      | `maven.compiler.release=25`         |
| Quarkus       | 3.33.3                  | platform BOM pins the rest          |
| Vaadin        | 25.2.7                  | server-side Flow, drawer navigation |
| Hibernate ORM | via Quarkus             | Panache repositories                |
| PostgreSQL    | 18                      | `postgres:18` via dev services      |
| Apache POI    | via `quarkus-poi` 2.2.1 | Excel export                        |
| Auth          | Auth0 + Quarkus OIDC    | web-app flow, logout via Auth0      |

---

## Quick start

```bash
# 1. Create .env — see docs/setup.md for the three keys
# 2. Start Docker Desktop — required, there is no fallback
./mvnw quarkus:dev        # UI on http://localhost:8080
```

`./mvnw` needs `JAVA_HOME` on JDK 25. On Windows run `mvnw.cmd` from `cmd.exe`.

Full instructions: [docs/setup.md](docs/setup.md).

---

## Known issues

- **Pending manual DDL.** `SalesOrder.customer` and `Transaction.customer` were retyped to
  `Customer`; Hibernate will not repoint the existing foreign keys. Run the `ALTER TABLE`
  statements in [docs/database.md](docs/database.md#pending-manual-ddl).
- **No migration tool.** Schema is managed by Hibernate `generation=update`, which skips
  destructive changes. Add Flyway before a second environment holds real data.
- **Rate limiting is per instance.** `RateLimitFilter` keeps token buckets in memory, so the
  effective limit is `erp.rate-limit.requests-per-window` times the number of nodes. Fine for the
  current single-node deployment; a shared bucket store is the upgrade path, and only
  `RateLimitService#check` changes. There is still no retry policy on service boundaries —
  `quarkus-smallrye-fault-tolerance` is the entry point for that.