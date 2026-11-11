# CORE ERP

Warehouse and procurement ERP — Quarkus 3 backend, Vaadin 25 server-side UI, PostgreSQL.

A modular monolith: 16 business modules, each owning its `model` / `repository` / `service` /
`views` packages. Services are the unit of reuse — a module reaches into another module through
its service layer, not its tables. The one systematic exception is the dashboard, which queries
repositories directly for its counters (see [Known issues](#known-issues)).

Rough shape today: 50 repositories, 41 services, 32 views across 19 routes.

---

## Stack

| Component | Version | Notes |
|---|---|---|
| Java | 25 | `maven.compiler.release=25` |
| Quarkus | 3.33.3 | platform BOM pins the rest |
| Vaadin | 25.2.7 | server-side Flow, drawer navigation |
| Hibernate ORM | via Quarkus | Panache repositories |
| PostgreSQL | 18 | `postgres:18` via dev services |
| Apache POI | via `quarkus-poi` 2.2.1 | Excel export |
| Auth | Auth0 + Quarkus OIDC | web-app flow, logout via Auth0 |

---

## Running

### Prerequisites

- JDK 25 (GraalVM 25 if you want the native build)
- Docker with the Compose plugin — Quarkus auto-detects `compose-devservices.yml` and starts
  PostgreSQL for you in dev and test. On Windows the Docker daemon must be running before
  `quarkus:dev`, or startup fails with a connection error.
- An Auth0 tenant, or any OIDC provider you configure yourself

### Environment

Create `.env` in the project root (gitignored):

```properties
AUTH0_ISSUER=https://<your-tenant>.eu.auth0.com/
AUTH0_CLIENT_ID=<client-id>
AUTH0_CLIENT_SECRET=<client-secret>
```

The issuer URL must include the scheme and end with `/`. The Auth0 application must be type
**Regular Web Application**, with allowed callback and logout URLs pointing at
`http://localhost:8080`.

### Dev mode

```bash
./mvnw quarkus:dev
```

Quarkus starts a PostgreSQL 18 container from `compose-devservices.yml` on port 5432 and
creates the schema on first boot. The UI is at `http://localhost:8080`.

Dev mode is the intended inner loop: Vaadin hot-reloads view classes without a restart.

### Tests

```bash
./mvnw test
```

### Production build

```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Native build

```bash
./mvnw clean package -Dnative
./target/*-runner
```

CI runs both `clean verify` and the native build on every push to `main`
(`.github/workflows/simple-build-test.yml`).

---

## Database schema

Schema management is `quarkus.hibernate-orm.database.generation=update` — Hibernate diffs the
entity model against the live database and applies changes at startup. There is no migration
tool.

That is fine while the model is still moving, but it has two consequences worth knowing:

- **Destructive changes are not applied.** Dropping or retyping a column needs a manual
  `ALTER TABLE`. Hibernate will not do it.
- **Adding a `NOT NULL` column to a populated table fails** on PostgreSQL unless the entity
  supplies a default. `BaseEntity.deleted` carries `columnDefinition = "boolean default false"`
  for exactly this reason.

Before production, or as soon as more than one environment holds real data, add Flyway and
switch `generation` to `validate`.

---

## Architecture

### Module layout

```
src/main/java/id/putra/core_erp/
├── config/           Quarkus + Vaadin bootstrap (Application, ExecutorConfiguration)
├── entity/           BaseEntity — audit columns and soft delete contract
├── enums/            ApprovalStatus, ApprovalType, BudgetType, OrderStatus, ReceivingStatus, TypeEnum
├── exceptions/       DownloadException, ModuleException, ReportException
├── repository/       SoftDeleteRepository — the delete contract
├── service/          Cross-cutting services + excel/ rendering primitives
├── ui/               MainLayout, LoginView, ReportsView, ViewWithDialog, theme/
└── modules/          16 business modules
```

| Module | Packages | Contains |
|---|---|---|
| `approval` | model, repository, service | Workflow, steps, requests, history |
| `budget` | model, repository, service, views | Budget per department and fiscal year |
| `customer` | model, repository, service, views | Customer master |
| `dashboard` | views | Landing dashboard — no own model |
| `department` | model, **repo**, service, views | Department master (note: `repo`, not `repository`) |
| `inbound` | model, repository, service, views | Receiving, putaway, QC, labelling, returns |
| `inventory` | model, repository, service, views | Stock items per lot, with expiry and reservation |
| `outbound` | model, repository, service, views | Sales orders, picking, packing, shipping, carriers |
| `parameter` | model, repository, service | Parameter lookups — no views, tab disabled in Settings |
| `procurement` | model, repository, service, views | Requisition, quotation, purchase order, item, transfer |
| `product` | model, repository, service, views | Product master |
| `report` | excel | Report content and column definitions |
| `setting` | views | Tabbed settings shell hosting department and approval config |
| `supplier` | model, repository, service, views | Supplier master |
| `transaction` | model, repository | Stock movement ledger — no service layer |
| `warehouse` | model, repository, service, views | Warehouse, zone, rack hierarchy |

Packages are not uniform across modules. Most follow `model` / `repository` / `service` /
`views`, but `department` names its package `repo`, `transaction` has no service, and
`dashboard`, `report` and `setting` are view- or content-only. Match the module you are working
in rather than forcing a shape onto it.

### Layering rules

What the code actually does, and what to follow when adding to it:

- **Services own transactions and business rules.** `@ApplicationScoped @Transactional`. This
  is the unit other modules depend on.
- **Repositories hold queries only.** No business rules, no orchestration.
- **Views call services, with two exceptions.** `DashboardView` and `RequisitionDashboardView`
  inject repositories directly to read their counters. Every other view goes through a service.
  Follow the service path for new work; the two dashboards are debt, not the pattern.
- **Cross-module access goes through the other module's service.** `RequisitionService` depends
  on `ApprovalService`, not on the approval package's repositories.
- **Services never build UI.** They return entities or data; the view formats.

`RequisitionService` is the reference implementation: it validates budget availability,
delegates workflow advancement to `ApprovalService`, and only touches its own repositories.

### Views and routes

Views are Vaadin Flow components registered with `@Route`. Two base classes exist:

- `ViewWithDialog` — for CRUD screens. Implements `addHeader`, `addToolbar`, `refreshData`,
  `configureGrid`, plus a dialog for create/edit. Most module views extend this.
- Plain `VerticalLayout` — for screens that are not a CRUD grid, such as `DashboardView`,
  `SettingsView` and the requisition screens.

Every routed view except `LoginView` declares `layout = MainLayout.class` and carries
`@Authenticated`. Navigation is the drawer built in `MainLayout`, so a new screen only shows up
there if you add a `SideNavItem` for it too.

---

## Conventions

### Entities

Every entity extends `BaseEntity`:

```java
@Entity
@Table
public class Customer extends BaseEntity {
    @NotBlank(message = "Customer name is required")
    public String name;
}
```

`BaseEntity` supplies `id` (from `PanacheEntity`), `createAt` / `createBy` / `updateAt` /
`updateBy` audit columns, and the soft delete fields.

Fields are **public**. Quarkus Panache works with public fields, and Vaadin binders read them
directly — there is no getter/setter layer in this codebase. Do not add one for consistency's
sake.

### Soft delete

Deletes are logical. `BaseEntity` carries:

```java
@SQLRestriction("deleted = false")
public abstract class BaseEntity extends PanacheEntity {
    public boolean deleted = false;
    public Instant deletedAt;
    public String deletedBy;
}
```

`@SQLRestriction` makes Hibernate inject `deleted = false` into every generated SELECT,
including `findById`, `findAll`, `count` and association joins. **Reads need no filtering
code.** A row disappears the moment the flag flips.

Writes go through `SoftDeleteRepository`:

```java
public interface CustomerRepository extends SoftDeleteRepository<Customer> { }

// in the service
customerRepository.softDeleteById(id);
```

Three things to know:

1. **`@SQLRestriction` does not cover native SQL.** `RequisitionRepo` hand-writes native
   queries, so each WHERE clause carries an explicit `r.deleted = false`. Native queries also
   need the `#` prefix on `PanacheRepository.find(...)` — without it, Panache parses the
   string as HQL and rejects `select r.*`.
2. **The restriction does not block hard deletes.** `deleteById()` still emits SQL `DELETE`.
   Always call `softDeleteById`.
3. **Soft delete does not cascade automatically.** If an entity owns children, override
   `cascadeToChildren` in its repository:

```java
@ApplicationScoped
public class ZoneRepository implements SoftDeleteRepository<Zone> {
    @Inject RackRepository rackRepository;

    @Override
    public void cascadeToChildren(Zone zone) {
        rackRepository.find("zone.id", zone.id).list().forEach(rackRepository::softDelete);
    }
}
```

Recursion happens naturally — `Warehouse → Zone → Rack` works because `WarehouseRepository`
calls `zoneRepository.softDelete`, which calls its own `cascadeToChildren`.

`restore(id)` and `findAllIncludingDeleted()` go through native SQL, because a soft-deleted row
is invisible to JPA.

**Scope exception:** `ApprovalRequest` and `ApprovalHistory` do **not** extend `BaseEntity` and
still hard-delete. They are the approval audit trail — an append-only record that must not be
mutable. `ApprovalWorkflow` and `ApprovalStep` are user-editable configuration and *do* extend
`BaseEntity`.

### Excel export

Export is split into three layers so a report can change without touching styling, and styling
without touching reports:

```
service/excel/            UI-agnostic rendering primitives
├── ExcelColumn           column model: header, width, value type, alignment
├── ExcelReport           title + subtitle + columns + rows
├── ExcelStyles           every CellStyle, built once per Workbook
└── ExcelSheetBuilder     renders an ExcelReport onto a sheet

modules/report/excel/     report content
├── ReportType            enum: UI label, sheet name, which sheet class renders it
├── InventoryReportSheet
├── InboundReportSheet
├── OutboundReportSheet
└── AuditReportSheet
```

`ReportsView` owns selection and download only. Adding a report is one new sheet class plus one
`ReportType` constant — no view change.

The produced workbook follows a fixed formal layout:

| Row | Content |
|---|---|
| 0 | Company name — merged, centred, 14pt navy |
| 1 | Report title — merged, centred, 16pt bold, uppercased |
| 2 | Period / generated / record count — merged, centred, 10pt italic grey |
| 3 | Spacer |
| 4 | Header row — white on dark blue, bordered, frozen, autofiltered |
| 5..n | Data — zebra striped, bordered, typed values |
| n+1 | TOTAL — bold, grey fill, medium top border |
| n+2 | Footer — merged, 8pt italic grey |

Decisions worth knowing before editing:

- **Column widths are explicit**, never `autoSizeColumn`. Auto-sizing depends on the fonts
  installed in the JVM, so the same report comes out different widths on a developer machine
  and a headless server.
- **Dates are written as real Excel dates**, not formatted strings, so the recipient can sort,
  filter and group by them.
- **Null values become blank cells**, never `0` or `"-"`. A fabricated zero flows into the
  recipient's SUM and pivot and reads as real data.
- **The total row skips the leading `INTEGER` column.** "No." is a row counter, not a measure.
- **An empty report emits a "no data" notice and no TOTAL row**, for the same reason.
- `XSSFWorkbook` holds the whole document in memory. Past roughly 50,000 rows, switch to
  `SXSSFWorkbook` — at the cost of the merged title block.

Style guarantees are covered by `ExcelSheetBuilderTest`, which is plain JUnit: it renders a
workbook and asserts cell types, merges, widths and totals without booting Quarkus or needing
PostgreSQL.

### Testing

Three test classes, two styles:

| Class | Style | Needs DB |
|---|---|---|
| `ExcelSheetBuilderTest` | plain JUnit | no |
| `SoftDeleteTest` | `@QuarkusTest` + `@TestTransaction` | yes (dev services) |
| `RequisitionNativeQuerySoftDeleteTest` | `@QuarkusTest` + `@TestTransaction` | yes |

`@TestTransaction` rolls back after each test, so tests do not pollute the dev database and do
not depend on each other.

Anything touching CDI or the database needs `@QuarkusTest`; everything else should be plain
JUnit so it stays fast.

---

## Known issues

- **`SalesOrder.customer` and `Transaction.customer` map to `Department`.** Both carry
  `@JoinColumn(name = "customer_id")` but the field type is `Department`, so the generated FK
  points at the `Department` table. Visible in the Hibernate DDL log at startup. Needs a
  decision: retype to `Customer`, or rename the field to reflect that it really is a department.
- **Dashboards bypass the service layer.** `DashboardView` injects seven repositories and
  `RequisitionDashboardView` injects one, reading counters and grids directly. Every other view
  goes through a service. Moving these counts into `DashboardService` would restore the pattern.
- **`DepartmentView` is dead code.** It duplicates `setting/views/components/DepartmentComponent`,
  which is what `SettingsView` actually renders. Nothing navigates to `DepartmentView`, and its
  `@Route("department")` has no `layout`, so it would render without the app shell.
- **`Product` has no price field.** Reports that imply monetary value cannot be built from
  product master data alone. The inventory report is named *Stock Status* rather than *Stock
  Valuation* for this reason.
- **`ReportService`, `GenerateReportService` and `ExportFileService` are empty interfaces**
  with no implementations. Dead code from an earlier design — either implement or delete.
- **No rate limiting or retry policy on service boundaries.** Fine for the current single-tenant
  deployment; revisit before exposing anything publicly.

---

## Troubleshooting

**Startup fails with `relation "..." does not exist`**
The dev services container did not start. Check `docker ps`, or start PostgreSQL manually to
match `application.properties` — `jdbc:postgresql://localhost:5432/erp`, user and password
`erp` / `erp`.

**Startup fails with `column "deleted" of relation "..." contains null values`**
A table predates the soft delete columns and already has rows. Either drop the dev volume and
let Hibernate recreate the schema, or add the columns manually:

```sql
ALTER TABLE <table> ADD COLUMN deleted boolean NOT NULL DEFAULT false;
ALTER TABLE <table> ADD COLUMN deleted_at timestamptz;
ALTER TABLE <table> ADD COLUMN deleted_by varchar(255);
```

**Redirect loop or 401 on every page**
`AUTH0_ISSUER` is wrong. It must include the scheme and a trailing slash:
`https://<tenant>.eu.auth0.com/`. Also confirm the callback and logout URLs are registered in
the Auth0 application.

**`IllegalStateException: Cannot resolve entity type`**
A repository implements `SoftDeleteRepository` without an explicit type argument. Write
`implements SoftDeleteRepository<Customer>`, not a raw or wildcarded form.

**Vaadin serves a stale UI after a view change**
Dev mode hot-reloads view classes but not `MainLayout` or `Application`. Restart `quarkus:dev`.
