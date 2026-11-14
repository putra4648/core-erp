# Testing

Three test classes, two styles:

| Class                             | Style                               | Needs DB           |
| --------------------------------- | ----------------------------------- | ------------------ |
| `ExcelSheetBuilderTest`           | plain JUnit                         | no                 |
| `SoftDeleteTest`                  | `@QuarkusTest` + `@TestTransaction` | yes (dev services) |
| `RequisitionSearchSoftDeleteTest` | `@QuarkusTest` + `@TestTransaction` | yes                |

`@TestTransaction` rolls back after each test, so tests do not pollute the dev database and do not
depend on each other.

Anything touching CDI or the database needs `@QuarkusTest`; everything else should be plain JUnit
so it stays fast.

## Running without Docker

`@QuarkusTest` classes need the dev services PostgreSQL container, so they fail with
`Connection to localhost:5432 refused` when Docker is not running. Plain-JUnit tests still pass:

```bash
./mvnw test -Dtest=ExcelSheetBuilderTest
```

## Coverage gaps

Rate limiting (`RateLimitService`, `RateLimitKeyResolver`) is covered by plain-JUnit tests; the
Vert.x filter wiring itself is not, since exercising it needs a booted application and a real HTTP
client. A wiring regression therefore shows up as a 429 storm in dev mode rather than in the suite.

Nothing currently covers the dashboards, the outbound views, or the product master data. Changes
to view-layer counts or entity fields are verified by hand in dev mode:

- `/` — dashboard metric cards (product, warehouse, supplier, customer, requisition, pending,
  purchase order, sales order counts)
- `/requisition/dashboard` — requisition status breakdown
- `/master/products` — product price field and grid column

Steps that touch the schema fail loudly on startup if wrong, so those are self-checking.
View-layer count changes are not.
