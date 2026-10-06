# Business Rules: SalesOrder & SalesOrderLine

## Entity Definition: SalesOrder
Represents a confirmed customer purchase order for fulfillment and shipment.

| Field             | Type                 | Constraint           | Business Rule                                                    |
|:------------------|:---------------------|:---------------------|:-----------------------------------------------------------------|
| `id`              | Long                 | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                  |
| `soNumber`        | String               | Required, Unique     | Sales order number (e.g. `SO-2025-0010`).                        |
| `orderDate`       | Date                 | Required             | Date order was placed.                                           |
| `status`          | String               | Required             | State (`CONFIRMED`, `ALLOCATED`, `PICKED`, `SHIPPED`, `CLOSED`). |
| `customer`        | Department / Customer| ManyToOne            | Ordering client or internal department.                          |
| `salesOrderLines` | List<SalesOrderLine> | OneToMany, Cascade   | Detailed items requested.                                        |

## Entity Definition: SalesOrderLine
Individual line item demanded on the Sales Order.

| Field        | Type       | Constraint         | Business Rule                                   |
|:-------------|:-----------|:-------------------|:------------------------------------------------|
| `id`         | Long       | PK, Auto-increment | Primary identifier inherited from `BaseEntity`. |
| `product`    | Product    | ManyToOne, Required| Master product requested.                       |
| `salesOrder` | SalesOrder | ManyToOne          | Parent Sales Order.                             |

## ✅ Validation & Operational Rules
- Order confirmation reserves stock on [`InventoryItem.reservedQty`](file:///d:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inventory/model/InventoryItem.java).
- Creates linked [`PickingTask`](file:///d:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/PickingTask.java) entries for warehouse staff.
- Once shipped, status transitions to `SHIPPED`, decreasing inventory on hand and creating `OUTBOUND_SHIPMENT` transaction entries.
