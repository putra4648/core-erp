| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| budget_id               | Long   | Required, FK                  | Wajib merujuk ke budget parent                       |
| expense_type            | Enum   | Required                      | Tipe pengeluaran wajib dari list valid types          |

✅ Validation:
- Expense type harus dalam kategori yang valid (CAPEX/OPEX/Operating/etc.)
- Variance dihitung otomatis sebagai actual_spent - planned_amount
