# Business Rules: BudgetDetail & BudgetAllocation

## Entity Definition: BudgetDetail
Line item breakdown under a master budget categorized by expenditure type.

| Field          | Type                   | Constraint         | Business Rule                                                    |
|:---------------|:-----------------------|:-------------------|:-----------------------------------------------------------------|
| `id`           | Long                   | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.                  |
| `name`         | String                 | Required           | Line item title (e.g. `IT Hardware & Peripherals`).              |
| `note`         | String                 | Nullable           | Description or justification.                                    |
| `totalBudget`  | BigInteger             | Min 0              | Annual / Period ceiling allocation amount.                       |
| `amount`       | BigInteger             | Nullable           | Disbursed or base allocation amount.                             |
| `type`         | BudgetType (Enum)      | Required           | Classification (`OPEX`, `CAPEX`, `EXPENSE`, etc.).               |
| `budget`       | Budget                 | ManyToOne          | Parent master budget.                                            |
| `allocations`  | List<BudgetAllocation> | JSONB column       | Periodic / monthly planned vs. actual spending milestones.       |

## Value Object: BudgetAllocation
Structured schedule of planned vs actual consumption stored in JSONB.

| Field    | Type       | Business Rule                                            |
|:---------|:-----------|:---------------------------------------------------------|
| `date`   | Date       | Milestone date (e.g., month start `2025-01-01`).         |
| `plan`   | BigInteger | Planned expenditure target for this interval.            |
| `actual` | BigInteger | Recorded actual realization for this interval.           |

## ✅ Validation & Operational Rules
- $\sum \text{BudgetAllocation.plan} \le \text{totalBudget}$.
- PR items assigned to a `budget_detail_id` verify whether requested price exceeds remaining planned balance.
