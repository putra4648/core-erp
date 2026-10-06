# Business Rules: InventoryItem

## Entity Definition
Maintains bin-level physical stock balances, serial numbers, lot tracking, and reservation states.

| Field          | Type          | Constraint         | Business Rule                                                |
|:---------------|:--------------|:-------------------|:-------------------------------------------------------------|
| `id`           | Long          | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.              |
| `product`      | Product       | ManyToOne, Required| Master product catalog entity (`product_sku`).               |
| `name`         | String        | Nullable           | Item instance name or descriptive label.                     |
| `quantity`     | Long          | Min 0, Required    | Total physical on-hand quantity at this specific location.   |
| `reservedQty`  | Long          | Min 0, Default 0   | Quantity allocated / reserved for active outbound pick waves.|
| `lotNumber`    | String        | Nullable           | Production batch or lot number.                              |
| `serialNumber` | String        | Nullable           | Unique serial number for serialized items.                   |
| `expiredDate`  | ZonedDateTime | Nullable           | Expiration timestamp for batch/lot.                          |
| `status`       | String        | Default `AVAILABLE`| State (`AVAILABLE`, `RESERVED`, `QUARANTINE`, `DAMAGED`).    |

## ✅ Stock Quantity Invariant Formula
$$\text{Available Stock} = \text{quantity} - \text{reservedQty}$$

## ✅ Validation & Operational Rules
- `quantity` and `reservedQty` must remain $\ge 0$.
- `reservedQty` must not exceed `quantity`.
- Expired items (`expiredDate < now()`) must be quarantined automatically (`status = QUARANTINE`).
- Stock movements create corresponding records in the `Transaction` ledger.
