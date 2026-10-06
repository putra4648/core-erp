# Business Rules: PurchaseOrder & PurchaseOrderLine

## Entity Definition: PurchaseOrder
Binding commercial agreement issued to a supplier committing purchase of specified products at agreed prices.

| Field                | Type                     | Constraint           | Business Rule                                                                |
|:---------------------|:-------------------------|:---------------------|:-----------------------------------------------------------------------------|
| `id`                 | Long                     | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                              |
| `poNumber`           | String                   | Required, Unique     | Generated PO identifier (e.g. `PO-2025-0001`).                               |
| `orderDate`          | Date                     | Required             | Issuance timestamp.                                                          |
| `status`             | OrderStatus (Enum)       | Required             | `DRAFT`, `SUBMITTED`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, etc.|
| `supplier`           | Supplier                 | ManyToOne, Required  | Selected vendor entity.                                                      |
| `purchaseOrderLines` | List<PurchaseOrderLine>  | OneToMany, Cascade   | Ordered product items and quantities.                                        |
| `receivings`         | List<Receiving>          | OneToMany            | Inbound warehouse goods receipt records linked to this PO.                   |

## Entity Definition: PurchaseOrderLine
Line-item quantity and unit cost agreed on the PO.

| Field           | Type          | Constraint         | Business Rule                                            |
|:----------------|:--------------|:-------------------|:---------------------------------------------------------|
| `id`            | Long          | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `product`       | Product       | ManyToOne, Required| Master product catalog entity.                           |
| `quantity`      | Integer       | Min 1, Required    | Ordered units count.                                     |
| `price`         | BigDecimal    | Min 0.00, Required | Unit purchase price agreed.                              |
| `purchaseOrder` | PurchaseOrder | ManyToOne          | Parent PO.                                               |

## ✅ Validation & Operational Rules
- PO cannot be dispatched to a vendor without active supplier and $\ge 1$ line item.
- Receiving orders validate incoming delivery quantities against `PurchaseOrderLine.quantity`.
- Once all lines are received, PO transitions to `DELIVERED` or `CLOSED`.
