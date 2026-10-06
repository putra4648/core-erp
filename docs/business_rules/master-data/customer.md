# Business Rules: Customer

## Entity Definition
Maintains customer master records for sales, quotations, dispatch, and receivables.

| Field       | Type    | Constraint         | Business Rule                                            |
|:------------|:--------|:-------------------|:---------------------------------------------------------|
| `id`        | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`      | String  | Required           | Customer or client company/individual name.              |
| `phone`     | String  | Nullable           | Contact phone number.                                    |
| `email`     | String  | Nullable           | Billing & notification email address.                    |
| `address`   | String  | Nullable           | Primary shipping and invoicing address.                  |
| `isActive`  | Boolean | Default true       | Inactive customers cannot have new Sales Orders created. |

## ✅ Validation & Operational Rules
- `name` is mandatory for customer creation.
- If `email` is provided, it must conform to standard email syntax.
- Outbound sales order creation checks `isActive == true`.
