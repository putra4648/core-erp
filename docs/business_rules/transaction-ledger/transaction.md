# Business Rules: Transaction Ledger

## Entity Definition: Transaction
Master financial and physical goods movement journal entry.

| Field         | Type                    | Constraint           | Business Rule                                                                |
|:--------------|:------------------------|:---------------------|:-----------------------------------------------------------------------------|
| `id`          | Long                    | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                              |
| `supplier`    | Supplier                | ManyToOne, Nullable  | Associated vendor (for inbound / vendor transactions).                       |
| `customer`    | Department / Customer   | ManyToOne, Nullable  | Associated client / internal receiving department.                           |
| `txnNumber`   | String                  | Required, Unique     | Transaction journal voucher number (e.g., `TXN-2025-00001`).                 |
| `txnType`     | String (Enum)           | Required             | `INBOUND`, `OUTBOUND`, `TRANSFER`, `ADJUSTMENT`, `RETURN_TO_VENDOR`.         |
| `txnDate`     | ZonedDateTime           | Required             | Timestamp of transaction execution.                                         |
| `sourceLocId` | String                  | Nullable             | Origin storage location / warehouse bin.                                     |
| `destLocId`   | String                  | Nullable             | Destination storage location / warehouse bin.                                |
| `refNumber`   | String                  | Nullable             | Source document reference (`PO#`, `SO#`, `PR#`, `RMA#`).                     |
| `refType`     | String                  | Nullable             | Reference document classification (`PO`, `SO`, `TRANSFER`, `RETURN`).        |
| `status`      | String                  | Required             | Journal status (`POSTED`, `COMPLETED`, `VOID`).                              |
| `notes`       | String                  | Nullable             | Transaction reason or audit commentary.                                      |
| `totalAmount` | BigDecimal              | Min 0.00             | Monetary value of transaction lines sum.                                     |
| `quantity`    | Long                    | Min 0                | Total aggregate item quantity moved.                                         |
| `items`       | List<TransactionItem>   | OneToMany, Cascade   | Line-item details of products moved.                                         |

## ✅ Validation & Operational Rules
- `txnNumber` and `txnType` are mandatory.
- Posted transactions are immutable and cannot be modified; reversals require a compensatory offset transaction.
