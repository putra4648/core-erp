# Business Rules: Receiving & ReceivingLine

## Entity Definition: Receiving
Represents the gate/dock entry of vendor deliveries against a Purchase Order.

| Field              | Type                 | Constraint           | Business Rule                                                    |
|:-------------------|:---------------------|:---------------------|:-----------------------------------------------------------------|
| `id`               | Long                 | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                  |
| `receivingNumber`  | String               | Required, Unique     | Inbound receipt tracking number (e.g. UUID / `REC-2025-001`).    |
| `receivedDate`     | Date                 | Required             | Date goods arrived at dock.                                      |
| `status`           | OrderStatus (Enum)   | Required             | State of goods receipt (`PROCESSING`, `DELIVERED`, `CANCELLED`). |
| `purchaseOrder`    | PurchaseOrder        | ManyToOne            | Associated PO being fulfilled.                                   |
| `receivingLines`   | List<ReceivingLine>  | OneToMany, Cascade   | Detailed breakdown of arrived goods per product/batch.          |

## Entity Definition: ReceivingLine
Individual line item delivered at the dock.

| Field          | Type          | Constraint         | Business Rule                                                |
|:---------------|:--------------|:-------------------|:-------------------------------------------------------------|
| `id`           | Long          | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.              |
| `product`      | Product       | ManyToOne, Required| Product received.                                            |
| `receivedDate` | Date          | Required           | Date/timestamp item was checked in.                          |
| `status`       | String        | Nullable           | Line item status (`PENDING_QC`, `PASSED`, `REJECTED`).       |
| `lotBatch`     | String        | Nullable           | Manufacturer lot number or production batch code.            |
| `expiryDate`   | Date          | Nullable           | Expiration date for perishable or shelf-life items.          |
| `qty`          | BigDecimal    | Required           | Ordered / expected quantity (`ordered_qty`).                 |
| `qtyReceived`  | BigDecimal    | Min 0.00, Required | Actual physically counted quantity received.                 |
| `receiving`    | Receiving     | ManyToOne          | Parent receiving document.                                   |

## ✅ Validation & Operational Rules
- `qtyReceived` cannot be negative.
- If `qtyReceived < qty`, the remaining quantity remains pending on the PO for subsequent deliveries.
- Upon receiving completion, line items move to the **Quality Inspection** stage.
