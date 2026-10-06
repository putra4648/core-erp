# Business Rules: Multi-Stage Approval Engine

## Entity Definition: Approval
Defines approval steps, stage routing, authorized approver identifiers, and bypass policies.

| Field                 | Type       | Constraint         | Business Rule                                                    |
|:----------------------|:-----------|:-------------------|:-----------------------------------------------------------------|
| `id`                  | Long       | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.                  |
| `stage`               | Integer    | Min 0              | Sequential approval order index (0 = 1st stage, 1 = 2nd stage).  |
| `users`               | String     | Required           | User identifier / NRP assigned to authorize this milestone.      |
| `isApprovalPermanent` | Boolean    | Default false      | If true, recorded step approval is immutable.                    |
| `isBypassPermanent`   | Boolean    | Default false      | If true, allows authorized override to jump remaining steps.     |
| `department`          | Department | ManyToOne          | Associated department hierarchy template.                        |

## ✅ Approval Flow Lifecycle & Rules
1. **Inheritance**: Requisitions and RFQs snapshot their approval chains from `Department.approvals` upon submission.
2. **Sequential Progression**: Current stage must complete successfully (`currentApprovalStage++`) before unlocking the next approver.
3. **Purchaser Stage**: In addition to department manager stages, procurement review is conducted via `temp_purchaser_approval` / `purchaser`.
4. **Rejection Handling**: Any rejecting reviewer transitions document status to `REJECT` and requires `rejectNote`.
5. **Final Approval**: Once `currentApprovalStage == totalApprovalStage`, the document transitions to `APPROVED`, enabling subsequent workflows (e.g. PO generation).