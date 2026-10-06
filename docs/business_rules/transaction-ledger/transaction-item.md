# Business Rules: TransactionItem

## Entity Definition
Individual item ledger movement entry linked to a parent Transaction.

| Field                | Type          | Constraint           | Business Rule                                            |
|:---------------------|:--------------|:---------------------|:---------------------------------------------------------|
| `id`                 | Long          | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.          |
| `transaction`        | Transaction   | ManyToOne, Required  | Parent transaction voucher.                              |
| `product`            | Product       | ManyToOne, Required  | Master catalog product moved.                            |
| `sourceInventoryItem`| InventoryItem | ManyToOne, Nullable  | Origin inventory balance record (if deducted).           |
| `destInventoryItem`  | InventoryItem | ManyToOne, Nullable  | Target inventory balance record (if incremented).        |
| `quantity`           | Long          | Min 1, Required      | Units moved.                                             |
| `unitPrice`          | BigDecimal    | Min 0.00             | Unit valuation.                                          |
| `totalPrice`         | BigDecimal    | Computed             | $\text{totalPrice} = \text{quantity} \times \text{unitPrice}$. |
| `lotNumber`          | String        | Nullable             | Batch / lot identifier.                                  |
| `serialNumber`       | String        | Nullable             | Serial number for serialized assets.                     |
| `notes`              | String        | Nullable             | Line item audit notes.                                   |

## ✅ Validation & Operational Rules
- `quantity` must be positive ($\ge 1$).
- Total price must equal $\text{quantity} \times \text{unitPrice}$.
