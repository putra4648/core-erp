# Business Rules: Quotation (Request for Quotation / RFQ)

## Entity Definition
Facilitates competitive vendor bidding, price inquiries, and commercial supplier comparison against approved PR items.

| Field                   | Type                    | Constraint           | Business Rule                                                    |
|:------------------------|:------------------------|:---------------------|:-----------------------------------------------------------------|
| `id`                    | Long                    | PK, Auto-increment   | Primary identifier inherited from `BaseEntity`.                  |
| `dueDate`               | Date                    | Required             | Vendor quotation submission deadline.                            |
| `suppliers`             | List<Supplier>          | JSONB column         | Candidate vendors invited to submit bids.                        |
| `details`               | List<RequisitionDetail> | JSONB column         | Line items subject to quotation.                                 |
| `note`                  | String                  | Nullable             | Commercial terms, delivery terms (e.g. Incoterms).               |
| `status`                | String                  | Required             | Workflow status (`DRAFT`, `SENT`, `APPROVED`, `REJECT`).         |
| `rejectNote`            | String                  | Nullable             | Rejection justification.                                         |
| `currentApprovalStage`  | Integer                 | Min 0, Default 0     | Step index for multi-stage RFQ signoff.                          |
| `totalApprovalStage`    | Integer                 | Min 0                | Total approval milestones.                                       |
| `lastApproved`          | String                  | Nullable             | Last reviewer identifier / timestamp.                            |
| `temApprovals`          | List<Approval>          | JSONB column         | Snapshot of approval chain configured for this quotation.        |

## ✅ Validation & Operational Rules
- An RFQ requires at least 1 supplier candidate and 1 item line.
- Winning vendor quote is selected prior to Purchase Order generation.
