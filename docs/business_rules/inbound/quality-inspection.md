# Business Rules: Quality Inspection (QC)

## Entity Definition
Inspection checklist and quality signoff performed on received goods before acceptance into inventory.

| Field           | Type          | Constraint         | Business Rule                                            |
|:----------------|:--------------|:-------------------|:---------------------------------------------------------|
| `id`            | Long          | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `result`        | String        | Required           | Inspection verdict (`PASSED`, `FAILED`, `CONDITIONAL`).  |
| `remarks`       | String        | Nullable           | Detailed defect notes, measurement variance, or reasons. |
| `receivingLine` | ReceivingLine | ManyToOne, Required| The specific delivery line inspected.                    |

## ✅ Validation & Operational Rules
- `PASSED` inspection allows the item to proceed to **Labelling** and **Putaway**.
- `FAILED` inspection triggers a **Return to Vendor (RTV)** workflow and prevents stock entry.
- Defect quantities and remarks are logged for supplier performance scoring.
