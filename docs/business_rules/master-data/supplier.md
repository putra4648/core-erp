# Business Rules: Supplier

## Entity Definition
Maintains vendor and supplier directory records for procurement and receiving.

| Field       | Type    | Constraint         | Business Rule                                         |
|:------------|:--------|:-------------------|:------------------------------------------------------|
| `id`        | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.       |
| `code`      | String  | Unique, Nullable   | Unique vendor identifier code (e.g. `SUPP-001`).      |
| `name`      | String  | Required           | Registered vendor company name.                       |
| `email`     | String  | Nullable           | Official sales/billing email address.                 |
| `phone`     | String  | Nullable           | Contact telephone number.                             |
| `address`   | String  | Nullable           | Factory / billing address.                            |
| `notes`     | String  | Nullable           | Terms of payment, performance notes, or ratings.      |
| `isActive`  | Boolean | Default true       | Inactive suppliers cannot receive new Purchase Orders.|

## ✅ Validation & Operational Rules
- `name` is required.
- Inactive suppliers are excluded from active RFQ/Quotation bidding.
