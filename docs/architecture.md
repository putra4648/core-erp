# Architecture

## Module layout

```
src/main/java/id/putra/core_erp/
├── config/           Quarkus + Vaadin bootstrap (Application, ExecutorConfiguration)
├── entity/           BaseEntity — audit columns and soft delete contract
├── enums/            ApprovalStatus, ApprovalType, BudgetType, OrderStatus, ReceivingStatus, TypeEnum
├── exceptions/       ModuleException
├── repository/       SoftDeleteRepository — the delete contract
├── service/          BaseService, cross-cutting services + excel/ rendering primitives
├── ui/               MainLayout, LoginView, ReportsView, ViewWithDialog, theme/
└── modules/          16 business modules
```

| Module | Packages | Contains |
|---|---|---|
| `approval` | model, repository, service | Workflow, steps, requests, history |
| `budget` | model, repository, service, views | Budget per department and fiscal year |
| `customer` | model, repository, service, views | Customer master |
| `dashboard` | views | Landing dashboard — no own model |
| `department` | model, **repo**, service | Department master (note: `repo`, not `repository`) |
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
`views`, but `department` names its package `repo` and has no `views`, `transaction` has no
service, and `dashboard`, `report` and `setting` are view- or content-only. Match the module you
are working in rather than forcing a shape onto it.

## Layering rules

What the code actually does, and what to follow when adding to it:

- **Services own transactions and business rules.** `@ApplicationScoped @Transactional`. This is
  the unit other modules depend on.
- **Repositories hold queries only.** No business rules, no orchestration.
- **Views call services, never repositories.** A view showing a count calls
  `<Module>Service.count()`, not the repository behind it. This holds for the dashboards too.
- **Cross-module access goes through the other module's service.** `RequisitionService` depends
  on `ApprovalService`, not on the approval package's repositories.
- **Services never build UI.** They return entities or data; the view formats.

`RequisitionService` is the reference implementation: it validates budget availability, delegates
workflow advancement to `ApprovalService`, and only touches its own repositories.

### Counting rows

`BaseService` declares `long count()`, so every module service can report its own row count
without exposing a repository. `DashboardService` does the same for requisitions, and adds
`countRequisitionsByStatus(ApprovalStatus)` for the per-status breakdown. Soft-deleted rows are
excluded automatically by `@SQLRestriction`.

## Views and routes

Views are Vaadin Flow components registered with `@Route`. Two base classes exist:

- `ViewWithDialog` — for CRUD screens. Implements `addHeader`, `addToolbar`, `refreshData`,
  `configureGrid`, plus a dialog for create/edit. Most module views extend this.
- Plain `VerticalLayout` — for screens that are not a CRUD grid, such as `DashboardView`,
  `SettingsView` and the requisition screens.

Every routed view except `LoginView` declares `layout = MainLayout.class` and carries
`@Authenticated`. Navigation is the drawer built in `MainLayout`, so a new screen only shows up
there if you add a `SideNavItem` for it too.

Department CRUD has no route of its own: it lives in
`setting/views/components/DepartmentComponent`, rendered as a tab inside `SettingsView`. That is
why the `department` module owns no `views` package.
