# Core ERP — Business Process Specification & Architecture

This document provides a comprehensive overview of the business processes, domain workflows, state machines, and cross-module interactions within the Core ERP system (`id.putra.core_erp.modules`).

---

## 📑 Table of Contents

- [Core ERP — Business Process Specification \& Architecture](#core-erp--business-process-specification--architecture)
  - [📑 Table of Contents](#-table-of-contents)
  - [1. System Overview \& Module Map](#1-system-overview--module-map)
  - [2. Master Data Management](#2-master-data-management)
  - [3. Budgeting \& Financial Control](#3-budgeting--financial-control)
    - [Key Rules](#key-rules)
  - [4. Multi-Stage Approval Workflow Engine](#4-multi-stage-approval-workflow-engine)
    - [Approval Entity Fields \& Semantics](#approval-entity-fields--semantics)
  - [5. Procure-to-Pay (P2P) Process](#5-procure-to-pay-p2p-process)
    - [Module States \& Enums](#module-states--enums)
  - [6. Inbound Warehouse Logistics Flow](#6-inbound-warehouse-logistics-flow)
    - [Inbound Operational Steps](#inbound-operational-steps)
  - [7. Inventory Management \& Stock Tracking](#7-inventory-management--stock-tracking)
    - [Stock Calculation Logic](#stock-calculation-logic)
  - [8. Order-to-Cash \& Outbound Fulfillment (O2C)](#8-order-to-cash--outbound-fulfillment-o2c)
    - [Outbound Workflow Stages](#outbound-workflow-stages)
  - [9. Transaction Ledger \& Audit Trail](#9-transaction-ledger--audit-trail)
    - [Transaction Types (`txnType`)](#transaction-types-txntype)
  - [10. End-to-End Integrated Lifecycle Diagram](#10-end-to-end-integrated-lifecycle-diagram)
  - [Summary of Key Entities by Module](#summary-of-key-entities-by-module)

---

## 1. System Overview & Module Map

The Core ERP system is architected as modular business domains handling procurement, warehousing, inventory management, sales fulfillment, budgeting, and approval hierarchies.

```mermaid
graph TB
    subgraph MasterData ["🏢 Master Data"]
        MD_Comp["Company"]
        MD_Dept["Department"]
        MD_Prod["Product"]
        MD_Supp["Supplier"]
        MD_Cust["Customer"]
        MD_Param["Parameter"]
        MD_WH["Warehouse Topology (Zone/Rack/Loc)"]
    end

    subgraph Finance ["💰 Finance & Governance"]
        MOD_Budget["Budget & Budget Detail"]
        MOD_Appr["Approval Engine"]
        MOD_Txn["Transaction Ledger"]
    end

    subgraph Procurement ["🛒 Procure-to-Pay (P2P)"]
        PR_Req["Requisition (PR)"]
        PR_Quote["Quotation / RFQ"]
        PR_Transfer["Transfer Request"]
        PR_PO["Purchase Order (PO)"]
    end

    subgraph Inbound ["📥 Inbound Logistics"]
        IN_Recv["Receiving & Lines"]
        IN_QC["Quality Inspection"]
        IN_Label["Labelling"]
        IN_Putaway["Putaway"]
        IN_RTV["Return to Vendor (RTV)"]
    end

    subgraph Inventory ["📦 Inventory & Storage"]
        INV_Item["Inventory Item Stock"]
        INV_Loc["Location & Rack Bins"]
    end

    subgraph Outbound ["📤 Order-to-Cash (O2C)"]
        OUT_SO["Sales Order"]
        OUT_Pick["Picking Task"]
        OUT_Pack["Packing"]
        OUT_Ship["Carrier & Shipment"]
    end

    %% Dependencies
    MD_Dept --> MOD_Budget
    MOD_Budget --> PR_Req
    MOD_Appr --> PR_Req
    MOD_Appr --> PR_Quote
    MOD_Appr --> PR_Transfer
    PR_Req --> PR_Quote
    PR_Quote --> PR_PO
    PR_PO --> IN_Recv
    IN_Recv --> IN_QC
    IN_QC -->|Passed| IN_Label
    IN_QC -->|Failed| IN_RTV
    IN_Label --> IN_Putaway
    IN_Putaway --> INV_Item
    OUT_SO --> OUT_Pick
    OUT_Pick --> INV_Item
    OUT_Pick --> OUT_Pack
    OUT_Pack --> OUT_Ship
    IN_Putaway -.-> MOD_Txn
    OUT_Ship -.-> MOD_Txn
```

---

## 2. Master Data Management

Master Data acts as the single source of truth across all operational modules:

| Domain         | Entity                                                                                                                        | Purpose & Relationships                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| :------------- | :---------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Company**    | [`Company`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/company/model/Company.java)                   | Legal entity configuration, tax details, corporate headquarters.                                                                                                                                                                                                                                                                                                                                                                                                             |
| **Department** | [`Department`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/department/model/Department.java)          | Organization structure (`dept_number`, code, manager), requisition originator, and cost center.                                                                                                                                                                                                                                                                                                                                                                              |
| **Parameter**  | [`ParameterLookup`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/parameter/model/ParameterLookup.java) | Dynamic lookup engine for system-wide configuration keys and codes.                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Product**    | [`Product`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/product/model/Product.java)                   | Item catalog (SKU, barcode, name, unit of measure, category).                                                                                                                                                                                                                                                                                                                                                                                                                |
| **Supplier**   | [`Supplier`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/supplier/model/Supplier.java)                | Vendor profiles with contacts ([`SupplierContact`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/supplier/model/SupplierContact.java)), billing/shipping addresses ([`SupplierAddress`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/supplier/model/SupplierAddress.java)), and catalog mapping ([`ProductSupplier`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/supplier/model/ProductSupplier.java)). |
| **Customer**   | [`Customer`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/customer/model/Customer.java)                | Client directory with account contacts ([`ContactPersonCustomer`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/customer/model/ContactPersonCustomer.java)) and billing profiles.                                                                                                                                                                                                                                                                      |
| **Warehouse**  | [`Warehouse`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/warehouse/model/Warehouse.java)             | Physical storage topology: `Warehouse` &rarr; `Zone` &rarr; `Rack` &rarr; `Location` (Bin).                                                                                                                                                                                                                                                                                                                                                                                  |

---

## 3. Budgeting & Financial Control

The budget module ensures corporate spending governance prior to procurement commitment.

```mermaid
flowchart TD
    A[Department Plan] --> B[Create Budget & BudgetDetail]
    B --> C[Assign Fiscal Period & Amount]
    C --> D[Purchase Requisition Raised]
    D --> E{Check Budget Availability}
    E -->|Sufficient Funds| F[Commit Budget Line]
    E -->|Exceeded / Insufficient| G[Reject or Require Budget Override]
    F --> H[Proceed to Approval Stages]
```

### Key Rules

1. Every Requisition can reference a [`Budget`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/budget/model/Budget.java) and `budget_detail_id`.
2. Requisitions track currency, total price, and allocated department cost centers.
3. Over-budget requests require executive approval or explicit budget variance authorization.

---

## 4. Multi-Stage Approval Workflow Engine

The Core ERP system utilizes a multi-stage approval engine supporting sequential hierarchy steps, purchaser reviews, permanent approvals, and bypass rules.

```mermaid
stateDiagram-v2
    [*] --> DRAFT : User drafts request
    DRAFT --> WAITING_APPROVAL : Submit request (SEND)
    
    state WAITING_APPROVAL {
        Stage_1 : Stage 1 (Department Approver)
        Stage_2 : Stage 2 (Purchasing Reviewer)
        Stage_N : Stage N (Finance / Executive)
        
        Stage_1 --> Stage_2 : Approve (increment stage)
        Stage_2 --> Stage_N : Approve (increment stage)
    }

    WAITING_APPROVAL --> APPROVED : All stages completed
    WAITING_APPROVAL --> REJECT : Rejected by any reviewer (with Reject Note)
    APPROVED --> PO_CREATED : Purchase Order Generated
    APPROVED --> CLOSED : Completed / Fulfilled
    REJECT --> DRAFT : Revise and resubmit
```

### Approval Entity Fields & Semantics

- **`currentApprovalStage`**: Current step index (0-based or 1-based index).
- **`totalApprovalStage`**: Total number of required approval milestones.
- **`lastApproved`**: Timestamp and identifier of the last authorized user.
- **`nextApprovals`**: List of eligible approval user identifiers or roles for the pending stage.
- **`isApprovalPermanent`** / **`isBypassPermanent`**: Direct routing flags for accelerated or automated approvals.

---

## 5. Procure-to-Pay (P2P) Process

The procurement module coordinates purchase requisitions, quotations/bidding, and purchase order creation.

```mermaid
sequenceDiagram
    autonumber
    actor Requester as Department Requester
    actor Approver as Dept Manager / Approver
    actor Purchaser as Procurement Officer
    actor Supplier as Supplier / Vendor
    participant PR as Requisition Module
    participant Quot as Quotation Module
    participant PO as Purchase Order Module

    Requester->>PR: Create Requisition (Items, Budget, Due Date)
    Requester->>PR: Submit Requisition (Status = SEND)
    PR->>Approver: Route for Stage-1 Approval
    Approver->>PR: Approve Stage-1
    PR->>Purchaser: Route for Purchaser Approval
    Purchaser->>PR: Approve (Status = APPROVED)
    
    Purchaser->>Quot: Create RFQ / Quotation with selected Suppliers
    Supplier-->>Quot: Submit Price Quotes & Terms
    Purchaser->>Quot: Select Winning Supplier & Submit Quotation Approval
    
    Purchaser->>PO: Generate Purchase Order (PO Number, Lines, Delivery Date)
    PO->>Supplier: Dispatch PO to Vendor
    PO->>PR: Update Requisition Status to PO_CREATED
```

### Module States & Enums

- [`RequisitionEnum`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/enums/RequisitionEnum.java):
  - `DRAFT` &rarr; `SEND` &rarr; `WAITING_APPROVAL` &rarr; `APPROVED` &rarr; `PO_CREATED` &rarr; `CLOSED` (or `REJECT`).
- [`Transfer`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/model/Transfer.java): Used for inter-departmental transfers and asset reallocation.

---

## 6. Inbound Warehouse Logistics Flow

When goods arrive from a supplier against a Purchase Order, the inbound receiving pipeline inspects, labels, and puts away items.

```mermaid
flowchart TD
    A[Supplier Delivery Arrives] --> B[Receiving Entry against PO]
    B --> C[Create ReceivingLines with Lot/Batch & Expiry Date]
    C --> D[Quality Inspection QC]
    
    D --> E{QC Passed?}
    E -->|Pass| F[Generate Barcode / Labelling]
    E -->|Fail / Defective| G[Return to Vendor RTV / RMA]
    
    F --> H[Determine Optimal Warehouse Location]
    H --> I[Execute Putaway to Rack / Bin]
    I --> J[Update Inventory Stock Level & Transaction Ledger]
```

### Inbound Operational Steps

1. **Receiving (`Receiving`, `ReceivingLine`)**:
   - Records goods delivery against the PO number.
   - Captures `qtyReceived`, `lotBatch`, and `expiryDate`.
2. **Quality Inspection (`QualityInspection`)**:
   - Inspects physical items against standards.
   - Logs `result` (`PASSED`, `FAILED`, `CONDITIONAL`) and detailed `remarks`.
3. **Labelling (`Labelling`)**:
   - Assigns scannable labels, batch identifiers, and tracking codes.
4. **Putaway (`Putaway`)**:
   - Moves products from the receiving dock to assigned storage `Location` (Zone &rarr; Rack &rarr; Bin).
5. **Return to Vendor (`ReturnToVendor`, `Return`)**:
   - Isolates rejected goods and prepares vendor return documentation.

---

## 7. Inventory Management & Stock Tracking

The inventory module maintains accurate visibility over available, allocated, and physical stock.

```mermaid
classDiagram
    class Warehouse {
        +Long id
        +String name
        +String code
        +String address
    }
    class Zone {
        +Long id
        +String name
        +Warehouse warehouse
    }
    class Rack {
        +Long id
        +String code
        +Zone zone
    }
    class Location {
        +Long id
        +String code
        +String barcode
        +Rack rack
    }
    class InventoryItem {
        +Long id
        +Product product
        +Location location
        +BigDecimal qtyOnHand
        +BigDecimal qtyAllocated
        +BigDecimal qtyAvailable
        +String lotBatch
        +Date expiryDate
    }

    Warehouse "1" *-- "many" Zone
    Zone "1" *-- "many" Rack
    Rack "1" *-- "many" Location
    Location "1" o-- "many" InventoryItem
```

### Stock Calculation Logic

$$\text{Quantity Available} = \text{Quantity On Hand} - \text{Quantity Allocated (Reserved for Outbound)}$$

- **Inbound Putaway**: Increases `qtyOnHand` and `qtyAvailable` in designated `Location`.
- **Sales Order Reservation**: Increases `qtyAllocated` and reduces `qtyAvailable`.
- **Picking & Dispatch**: Decreases `qtyOnHand` and `qtyAllocated`.

---

## 8. Order-to-Cash & Outbound Fulfillment (O2C)

The outbound module manages customer order capture, warehouse picking waves, packaging, carrier assignment, and final dispatch.

```mermaid
sequenceDiagram
    autonumber
    actor Customer as Customer
    actor Sales as Sales Team
    actor Warehouse as Warehouse Operator
    actor Logistics as Carrier / Logistics
    participant SO as Sales Order
    participant Pick as Picking Task
    participant Pack as Packing
    participant Ship as Shipment

    Customer->>Sales: Place Purchase Request
    Sales->>SO: Create Sales Order (SO Number, Customer, Lines)
    SO->>Pick: Generate Picking Tasks for warehouse locations
    Warehouse->>Pick: Execute Item Picking (Status = PICKED)
    Warehouse->>Pack: Package Items, Record Weight & Dimension
    Pack->>Pack: Generate Package Number
    Logistics->>Ship: Assign Carrier (Tracking #, Manifest)
    Logistics->>Ship: Dispatch Shipment (Status = SHIPPED)
    Ship->>SO: Complete Order (Status = DELIVERED)
```

### Outbound Workflow Stages

1. **Sales Order (`SalesOrder`, `SalesOrderLine`)**: Customer demand confirmation.
2. **Picking (`PickingTask`)**: Directed picking from specific warehouse racks/bins.
3. **Packing (`Packing`)**: Packaging verification, weight/dimension recording, packing slip generation.
4. **Shipment (`Shipment`, `Carrier`)**: Carrier assignment, tracking manifest, and shipping event recording.

---

## 9. Transaction Ledger & Audit Trail

Every physical and monetary movement is logged immutably in the universal [`Transaction`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/transaction/model/Transaction.java) and [`TransactionItem`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/transaction/model/TransactionItem.java) tables.

```mermaid
erDiagram
    TRANSACTION ||--|{ TRANSACTION_ITEM : contains
    TRANSACTION }|--|| SUPPLIER : involves
    TRANSACTION }|--|| CUSTOMER : involves

    TRANSACTION {
        bigint id PK
        string txnNumber
        string txnType "INBOUND | OUTBOUND | TRANSFER | ADJUSTMENT"
        datetime txnDate
        string sourceLocId
        string destLocId
        string refNumber "PO# / SO# / PR#"
        string refType "PO | SO | TRANSFER"
        string status
        decimal totalAmount
        bigint quantity
    }

    TRANSACTION_ITEM {
        bigint id PK
        bigint transaction_id FK
        bigint product_id FK
        decimal quantity
        decimal unitPrice
        decimal totalPrice
        string lotBatch
    }
```

### Transaction Types (`txnType`)

- `INBOUND_RECEIPT`: Stock added via purchase order receiving & putaway.
- `OUTBOUND_SHIPMENT`: Stock deducted via sales order shipment.
- `INTERNAL_TRANSFER`: Stock moved between warehouses or rack locations.
- `INVENTORY_ADJUSTMENT`: Cycle count or stock reconciliation adjustments.
- `RETURN_TO_VENDOR`: Defective item return to supplier.
- `CUSTOMER_RETURN`: Return from customer into quarantine/inspection location.

---

## 10. End-to-End Integrated Lifecycle Diagram

```mermaid
flowchart LR
    subgraph Plan ["1. Plan & Budget"]
        D[Dept Budget] --> PR[Purchase Requisition]
    end

    subgraph Procure ["2. Sourcing & Purchase"]
        PR --> Appr[Approval Workflow]
        Appr --> Quot[Quotation / RFQ]
        Quot --> PO[Purchase Order]
    end

    subgraph InboundFlow ["3. Inbound Logistics"]
        PO --> Recv[Goods Receiving]
        Recv --> QC[QC Inspection]
        QC --> Label[Labelling]
        Label --> Put[Putaway]
    end

    subgraph Stock ["4. Stock & Ledger"]
        Put --> Inv[Inventory On-Hand]
        Put --> Txn[Transaction Ledger]
    end

    subgraph OutboundFlow ["5. Outbound Logistics"]
        SO[Sales Order] --> Pick[Picking Task]
        Inv --> Pick
        Pick --> Pack[Packing]
        Pack --> Ship[Carrier Shipment]
        Ship --> Txn
        Ship --> Cust[Customer Delivery]
    end
```

---

## Summary of Key Entities by Module

| Module Directory | Key Domain Models                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Key Services & Responsibilities                                                   |
| :--------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------- |
| `approval`       | [`Approval`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/approval/model/Approval.java)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Multi-stage routing, authorization limits, approval logging.                      |
| `budget`         | [`Budget`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/budget/model/Budget.java), [`BudgetDetail`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/budget/model/BudgetDetail.java), [`BudgetType`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/budget/model/BudgetType.java)                                                                                                                                                                                                                                                                                                                                                                                   | Department fiscal allocation, spending limits, budget check.                      |
| `procurement`    | [`Requisition`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/model/Requisition.java), [`Quotation`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/model/Quotation.java), [`Transfer`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/model/Transfer.java), [`Purchase`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/procurement/model/Purchase.java)                                                                                                                                                                                                                                                 | PR creation, vendor bidding, purchasing approval, asset transfer.                 |
| `inbound`        | [`PurchaseOrder`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/PurchaseOrder.java), [`Receiving`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/Receiving.java), [`QualityInspection`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/QualityInspection.java), [`Labelling`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/Labelling.java), [`Putaway`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/Putaway.java), [`ReturnToVendor`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inbound/model/ReturnToVendor.java) | Dock receiving, lot/expiry tracking, quality check, putaway to bins, RMA returns. |
| `inventory`      | [`InventoryItem`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/inventory/model/InventoryItem.java)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | Real-time stock on hand, allocated quantities, lot balances.                      |
| `outbound`       | [`SalesOrder`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/SalesOrder.java), [`PickingTask`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/PickingTask.java), [`Packing`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/Packing.java), [`Carrier`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/Carrier.java), [`Shipment`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/outbound/model/Shipment.java)                                                                                                                                               | Sales demand processing, wave picking, box packing, carrier dispatch.             |
| `transaction`    | [`Transaction`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/transaction/model/Transaction.java), [`TransactionItem`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/transaction/model/TransactionItem.java)                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | Immutable audit log of all financial and physical movements.                      |
| `warehouse`      | [`Warehouse`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/warehouse/model/Warehouse.java), [`Zone`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/warehouse/model/Zone.java), [`Rack`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/warehouse/model/Rack.java), [`Location`](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/modules/warehouse/model/Location.java)                                                                                                                                                                                                                                                                               | Storage facility hierarchy, bin coordinates, warehouse contact persons.           |
| `master data`    | `company`, `department`, `customer`, `supplier`, `product`, `parameter`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Foundation master records utilized across all modules.                            |
| `dashboard`      | View aggregation layer                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Operational KPI analytics, pipeline summaries, alert queues.                      |
