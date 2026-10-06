# Business Rules: Shipment

## Entity Definition
Outbound dispatch manifest, carrier handoff, and tracking lifecycle.

| Field            | Type    | Constraint         | Business Rule                                                    |
|:-----------------|:--------|:-------------------|:-----------------------------------------------------------------|
| `id`             | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.                  |
| `shipmentNumber` | String  | Required, Unique   | Tracking number or Bill of Lading (BOL).                         |
| `shipDate`       | Date    | Required           | Handoff / dispatch date.                                         |
| `status`         | String  | Required           | Status (`DISPATCHED`, `IN_TRANSIT`, `DELIVERED`, `RETURNED`).    |
| `packing`        | Packing | ManyToOne          | Associated physical package.                                     |
| `carrier`        | Carrier | ManyToOne          | Assigned logistics provider.                                     |

## ✅ Validation & Operational Rules
- `shipmentNumber` and `shipDate` must be valid.
- Handoff updates `status = DISPATCHED`, releases inventory allocation, and logs an `OUTBOUND_SHIPMENT` transaction.
