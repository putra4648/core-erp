# Business Rules: Transfer & Item Management

## Entity Definition: Transfer (Transfer Request)
Inter-departmental or cross-facility material transfer and reallocation requests.

| Field                   | Type                 | Constraint           | Business Rule                                                    |
|:------------------------|:---------------------|:---------------------|:-----------------------------------------------------------------|
| `id`                    | Long                 | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                  |
| `title`                 | String               | Required             | Transfer title / purpose.                                        |
| `dueDate`               | Date                 | Required             | Required completion date.                                        |
| `suppliers`             | List<Supplier>       | JSONB column         | Candidate vendors/transporters if external logistics needed.     |
| `details`               | List<TransferDetail> | JSONB column         | Line items requested for transfer.                               |
| `note`                  | String               | Nullable             | Notes or special handling instructions.                          |
| `status`                | String               | Required             | Workflow status (`DRAFT`, `SEND`, `APPROVED`, `REJECT`, etc.).   |
| `rejectNote`            | String               | Nullable             | Rejection reason if not approved.                                |
| `currentApprovalStage`  | Integer              | Min 0, Default 0     | Step index for multi-stage approval.                             |
| `totalApprovalStage`    | Integer              | Min 0                | Total approval milestones required.                              |
| `lastApproved`          | String               | Nullable             | Identifier of last approver.                                     |
| `department`            | Department           | ManyToOne            | Originating department.                                          |
| `temApprovals`          | List<Approval>       | JSONB column         | Configured approval sequence.                                    |
| `tempPurchaserApproval` | Approval             | JSONB column         | Purchaser review stage metadata.                                 |
| `filename`              | String               | Nullable             | Attached approval note or shipping manifest file path.           |

## Entity Definition: Item & ItemMeasurement
Legacy or supplementary procurement catalog item specification.

| Field           | Type            | Constraint         | Business Rule                                            |
|:----------------|:----------------|:-------------------|:---------------------------------------------------------|
| `id`            | Long            | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `name`          | String          | Required           | Item catalog designation.                                |
| `price`         | BigInteger      | Min 0              | Reference standard price.                                |
| `description`   | String          | Nullable           | Item detail description.                                 |
| `status`        | String          | Default `ACTIVE`   | Status (`ACTIVE`, `INACTIVE`).                           |
| `otherSupplier` | String          | Nullable           | Secondary vendor name.                                   |
| `otherPhone`    | String          | Nullable           | Secondary vendor contact.                                |
| `unit`          | String          | Nullable           | Unit of measurement symbol.                              |
| `measurement`   | ItemMeasurement | OneToOne, Cascade  | Packaging specification and measurement unit details.    |
