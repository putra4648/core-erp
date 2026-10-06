# Business Rules: RequisitionDetail

## Entity Definition
Individual item specification and quantity requested on a Purchase Requisition.

| Field         | Type         | Constraint         | Business Rule                                            |
|:--------------|:-------------|:-------------------|:---------------------------------------------------------|
| `id`          | Long         | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`        | String       | Required           | Line item title / description.                           |
| `itemNumber`  | String       | Nullable           | Item code or product reference SKU.                      |
| `description` | String       | Nullable           | Technical specification.                                 |
| `note`        | String       | Nullable           | Special handling instructions.                           |
| `specFiles`   | List<String> | Nullable           | File paths of technical specification documents.         |
| `uom`         | String       | Default `PCS`      | Unit of measure (e.g. `PCS`, `BOX`, `KG`).               |
| `qty`         | BigInteger   | Min 1, Required    | Requested quantity.                                      |
| `unitPrice`   | BigDecimal   | Min 0              | Estimated unit cost.                                     |
| `supplier`    | String       | Nullable           | Suggested preferred vendor name/code.                    |
| `subtotal`    | BigDecimal   | Computed           | $\text{subtotal} = \text{qty} \times \text{unitPrice}$.  |
| `requisition` | Requisition  | ManyToOne          | Parent Requisition document.                             |
| `rfq`         | Quotation    | ManyToOne          | Associated RFQ when converted to bidding.                |

## ✅ Validation & Operational Rules
- `name` and positive `qty` ($\ge 1$) are required.
- `subtotal` is recalculated automatically when `qty` or `unitPrice` changes.
