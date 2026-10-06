# Business Rules: Return to Vendor (RTV) & Returns

## Entity Definition: ReturnToVendor
Handles rejection, RMA, and shipping of damaged, defective, or incorrect goods back to the supplier.

| Field           | Type          | Constraint         | Business Rule                                            |
|:----------------|:--------------|:-------------------|:---------------------------------------------------------|
| `id`            | Long          | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `reason`        | String        | Required           | Rejection reason (e.g. `Damaged packaging`, `Expired`).  |
| `qty`           | BigDecimal    | Min 0.01, Required | Quantity of items being returned to the vendor.          |
| `receivingLine` | ReceivingLine | ManyToOne, Required| The defective receiving check-in line.                   |

## Entity Definition: Return
Generic return record metadata.

| Field         | Type    | Constraint         | Business Rule                                            |
|:--------------|:--------|:-------------------|:---------------------------------------------------------|
| `id`          | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`        | String  | Required           | Return authorization name or RMA tracking number.        |
| `description` | String  | Nullable           | Narrative return justification.                          |

## ✅ Validation & Operational Rules
- `qty` returned cannot exceed the `qtyReceived` on the associated `receivingLine`.
- Stock for RTV items is quarantined and deducted from warehouse inventory.
- Creates a `RETURN_TO_VENDOR` audit entry in the `Transaction` ledger.
