# Business Rules: Product

## Entity Definition
Represents master items, SKUs, inventory thresholds, and stock levels.

| Field         | Type    | Constraint           | Business Rule                                            |
|:--------------|:--------|:---------------------|:---------------------------------------------------------|
| `id`          | Long    | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.          |
| `sku`         | String  | Required, Unique     | Stock Keeping Unit (e.g. `SKU-IPHONE15-128`).            |
| `name`        | String  | Required             | Product commercial name.                                 |
| `quantity`    | Long    | Min 0, Default 0     | Aggregated on-hand stock quantity.                       |
| `minStock`    | Long    | Min 0, Default 0     | Reorder point threshold alerting inventory shortages.    |
| `maxStock`    | Long    | Min 0, Default 0     | Maximum storage capacity threshold.                      |
| `description` | String  | Nullable             | Product details, specification, or notes.                |
| `isActive`    | Boolean | Default true         | Inactive products cannot be selected in POs or SOs.      |

## ✅ Validation & Operational Rules
- `sku` and `name` cannot be blank.
- `minStock` must be $\le$ `maxStock` when `maxStock > 0`.
- Stock transactions automatically update the aggregated `quantity` balance.
