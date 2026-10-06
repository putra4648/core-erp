# Business Rules: Warehouse & Storage Topology

## Warehouse
Top-level physical storage facility.

| Field       | Type        | Constraint         | Business Rule                                            |
|:------------|:------------|:-------------------|:---------------------------------------------------------|
| `id`        | Long        | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`      | String      | Required           | Facility name (e.g. `Main Warehouse Jakarta`).           |
| `location`  | String      | Nullable           | Address or geographic location.                          |
| `area`      | Double      | Nullable           | Total floor area in square meters ($m^2$).               |
| `total`     | Integer     | Nullable           | Capacity or total bin counts.                            |
| `isActive`  | Boolean     | Default true       | Status flag.                                             |
| `zones`     | List<Zone>  | OneToMany, Cascade | Subdivided zones within this warehouse facility.         |

## Zone
Logical or functional subdivision inside a warehouse (e.g., Receiving, Cold Storage, Staging).

| Field       | Type        | Constraint         | Business Rule                                            |
|:------------|:------------|:-------------------|:---------------------------------------------------------|
| `id`        | Long        | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `code`      | String      | Required           | Zone short code (e.g., `ZN-COLD-01`).                    |
| `name`      | String      | Required           | Descriptive zone name.                                   |
| `type`      | String      | Nullable           | Functional category (e.g., `DRY`, `COLD`, `HAZARDOUS`).  |
| `warehouse` | Warehouse   | ManyToOne          | Parent warehouse.                                        |
| `racks`     | List<Rack>  | OneToMany, Cascade | Physical storage racks located in this zone.             |
| `isActive`  | Boolean     | Default true       | Active status indicator.                                 |

## Rack
Physical storage aisle and shelf structure.

| Field       | Type           | Constraint         | Business Rule                                         |
|:------------|:---------------|:-------------------|:------------------------------------------------------|
| `id`        | Long           | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.       |
| `name`      | String         | Required           | Rack label/number (e.g., `Rack-A1`).                  |
| `rows`      | Long           | Min 1, Default 1   | Vertical tier count.                                  |
| `cols`      | Long           | Min 1, Default 1   | Horizontal column slot count.                         |
| `zone`      | Zone           | ManyToOne          | Parent zone.                                          |
| `locations` | List<Location> | OneToMany          | Specific storage bins / slots on this rack.           |
| `isActive`  | Boolean        | Default true       | Active status indicator.                              |

## Location (Bin)
Smallest addressable putaway / pick unit in the warehouse.

| Field       | Type    | Constraint         | Business Rule                                            |
|:------------|:--------|:-------------------|:---------------------------------------------------------|
| `id`        | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`      | String  | Required           | Bin coordinate/name (e.g., `LOC-A1-01-02`).              |
| `binNumber` | String  | Required           | Scannable bin identifier / barcode representation.       |
| `type`      | String  | Nullable           | Location type (e.g., `PALLET`, `SHELF`, `BULK`).         |
| `rack`      | Rack    | ManyToOne          | Parent rack structure.                                   |
| `isActive`  | Boolean | Default true       | Active status indicator.                                 |

## ✅ Topology Hierarchy
$$\text{Warehouse} \longrightarrow \text{Zone} \longrightarrow \text{Rack} \longrightarrow \text{Location (Bin)}$$
- Putaway operations must resolve down to a valid `Location`.
- Deleting a parent cascade-removes or validates child structures.
