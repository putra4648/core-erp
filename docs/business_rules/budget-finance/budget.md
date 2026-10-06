# Business Rules: Budget

## Entity Definition
Represents annual or periodic departmental spending allocation and fiscal limits.

| Field         | Type               | Constraint           | Business Rule                                                |
|:--------------|:-------------------|:---------------------|:-------------------------------------------------------------|
| `id`          | Long               | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.              |
| `fiscalYear`  | Date               | Required             | Fiscal year / validity period (e.g. `2025-01-01`).           |
| `department`  | Department         | ManyToOne, Required  | Owning cost center / organizational unit.                    |
| `details`     | List<BudgetDetail> | OneToMany, Cascade   | Specific line-item allocations under this master budget.     |
| `isActive`    | Boolean            | Default true         | Active status flag.                                          |

## ✅ Validation & Operational Rules
- A department cannot have overlapping budgets for the exact same fiscal year without prior decommission.
- Purchase Requisitions referencing this `Budget` consume available amounts defined across its `BudgetDetail` lines.