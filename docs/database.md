# Database

## Schema management

Schema management is `quarkus.hibernate-orm.database.generation=update` — Hibernate diffs the
entity model against the live database and applies changes at startup. There is no migration
tool.

That is fine while the model is still moving, but it has three consequences worth knowing:

- **Destructive changes are not applied.** Dropping or retyping a column needs a manual
  `ALTER TABLE`. Hibernate will not do it.
- **Adding a `NOT NULL` column to a populated table fails** on PostgreSQL unless the entity
  supplies a default. `BaseEntity.deleted` carries `columnDefinition = "boolean default false"`
  for exactly this reason, and `Product.price` does the same with `numeric(19,4) default 0`.
- **A foreign key already in the database is not repointed.** Changing a field's type from one
  entity to another leaves the old constraint in place.

Before production, or as soon as more than one environment holds real data, add Flyway and
switch `generation` to `validate`.

## Pending manual DDL

`SalesOrder.customer` and `Transaction.customer` were retyped from `Department` to `Customer`.
Hibernate will not repoint the existing constraints, so this has to be applied once by hand:

```sql
ALTER TABLE sales_order DROP CONSTRAINT IF EXISTS sales_order_customer_id_fkey;
ALTER TABLE sales_order ADD CONSTRAINT sales_order_customer_id_fkey
    FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS transactions_customer_id_fkey;
ALTER TABLE transactions ADD CONSTRAINT transactions_customer_id_fkey
    FOREIGN KEY (customer_id) REFERENCES customer(id);
```

Any pre-existing `customer_id` values referenced `department` rows, so they must be cleared or
the dev volume dropped before the new constraint will hold.

## Soft delete

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
including `findById`, `findAll`, `count` and association joins. **Reads need no filtering code.**
A row disappears the moment the flag flips.

Writes go through `SoftDeleteRepository`:

```java
public interface CustomerRepository extends SoftDeleteRepository<Customer> { }

// in the service
customerRepository.softDeleteById(id);
```

Three things to know:

1. **`@SQLRestriction` does not cover native SQL.** `RequisitionRepo` hand-writes native queries,
   so each WHERE clause carries an explicit `r.deleted = false`. Native queries also need the `#`
   prefix on `PanacheRepository.find(...)` — without it, Panache parses the string as HQL and
   rejects `select r.*`.
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
