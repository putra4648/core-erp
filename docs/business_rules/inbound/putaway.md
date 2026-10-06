# Business Rules: Putaway & Labelling

## Entity Definition: Putaway & Labelling
Tracks movement of verified items from the inbound receiving dock into warehouse bin storage locations.

| Field         | Type    | Constraint         | Business Rule                                            |
|:--------------|:--------|:-------------------|:---------------------------------------------------------|
| `id`          | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `sku`         | String  | Required           | Product SKU being put away.                              |
| `lotNumber`   | String  | Nullable           | Batch / lot number assigned during check-in.             |
| `putaway`     | String  | Required           | Target bin / location identifier (e.g., `LOC-A1-02-03`). |
| `inboundId`   | Long    | Nullable           | Reference ID of inbound receiving shipment.              |
| `outboundId`  | String  | Nullable           | Cross-docking outbound reference (if applicable).        |
| `pickId`      | String  | Nullable           | Tracking pick wave identifier.                           |

## ✅ Validation & Operational Rules
- Putaway requires successful quality inspection clearance (`QC == PASSED`).
- Executing putaway automatically creates or increments the [`InventoryItem`](file:///d:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inventory/model/InventoryItem.java) balance at the target location.
- Generates an immutable `INBOUND_RECEIPT` record in the [`Transaction`](file:///d:/Project/core-erp/src/main/java/id/putra/core_erp/modules/transaction/model/Transaction.java) ledger.
