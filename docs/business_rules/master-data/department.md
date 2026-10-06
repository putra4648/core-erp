# Business Rules: Department

## Entity Definition
Represents organizational business units, cost centers, and requisition originators.

| Field                | Type            | Constraint           | Business Rule                                                    |
|:---------------------|:----------------|:---------------------|:-----------------------------------------------------------------|
| `id`                 | Long            | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                  |
| `code`               | String          | Required, Unique     | Department code/number (`dept_number`, e.g. `DEPT-001`, `IT-01`).|
| `name`               | String          | Required             | Full department name (`dept_name`, e.g. `Information Tech`).     |
| `status`             | Status (Enum)   | Nullable             | Operational status (`ACTIVE`, `INACTIVE`, etc.).                 |
| `deptManager`        | String          | Nullable             | NRP / Identifier of Department Manager (`dept_manager_nrp`).     |
| `deptPurchaser`      | String          | Nullable             | NRP / Identifier of Assigned Purchaser (`dept_purchaser_nrp`).   |
| `approvalPresident`  | String          | Nullable             | NRP / Identifier of Executive / President Approver.              |
| `budgets`            | List<Budget>    | OneToMany            | Associated departmental budgets.                                 |
| `approvals`          | List<Approval>  | OneToMany, Cascade   | Configured multi-stage approval sequence for this department.    |
| `requisitions`       | List<Requisition>| OneToMany          | Historical and active purchase requisitions raised by this dept. |

## ✅ Validation & Operational Rules
- `code` must be unique across the organization.
- Requisitions originated by a department automatically inherit its default `approvals` chain and `deptPurchaser`.
