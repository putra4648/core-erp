# Business Rules: Carrier, Packing & PickingTask

## Entity Definition: Carrier
Third-party logistics, freight provider, or internal fleet profile.

| Field         | Type           | Constraint         | Business Rule                                         |
|:--------------|:---------------|:-------------------|:------------------------------------------------------|
| `id`          | Long           | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.       |
| `name`        | String         | Required           | Transporter / logistics company name (e.g. `JNE Express`, `DHL`). |
| `serviceType` | String         | Nullable           | Service tier (e.g. `OVERNIGHT`, `ECONOMY`, `FREIGHT`).|
| `shipments`   | List<Shipment> | OneToMany          | Assigned dispatch shipments.                          |

## Entity Definition: Packing
Consolidation and packaging details for goods ready for shipment.

| Field           | Type           | Constraint         | Business Rule                                     |
|:----------------|:---------------|:-------------------|:--------------------------------------------------|
| `id`            | Long           | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.   |
| `packageNumber` | String         | Required, Unique   | Tracking carton or box number (e.g. `PKG-00101`). |
| `dimension`     | String         | Nullable           | Package dimensions ($L \times W \times H$ in cm). |
| `weight`        | BigDecimal     | Min 0.00           | Gross package weight in kilograms.                |
| `shipments`     | List<Shipment> | OneToMany          | Associated shipment manifests.                    |

## Entity Definition: PickingTask
Warehouse picking wave instruction linked to a Sales Order line.

| Field            | Type           | Constraint         | Business Rule                                        |
|:-----------------|:---------------|:-------------------|:-----------------------------------------------------|
| `id`             | Long           | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.      |
| `status`         | String         | Required           | State (`PENDING`, `IN_PROGRESS`, `PICKED`, `STAGED`).|
| `salesOrderLine` | SalesOrderLine | ManyToOne          | Associated Sales Order line item.                    |
