# Warehouse Module Refactoring Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Refactor the warehouse module by removing all boilerplate Converter classes in favor of simple DTOs, migrating necessary shared assets to the root `shared` package, removing the unused `warehouse/shared` directory, and updating all services, adapters, and UI views.

**Architecture:** 
- Eliminate all 26 standalone `Converter` classes across warehouse sub-modules (`warehouse`, `supplier`, `inventory`, `customer`, `inbound`, `outbound`, `transaction`).
- Transition DTOs into simple POJOs with clean Lombok annotations (`@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`) and standard conversion methods (`toEntity()` / `toDto()` on DTO or inline mapping in service/adapter).
- Move active shared components (`ProductDto`, `BaseDto`, `ModuleException`, `ResponseEnum`, `OrderStatus`, `ReceivingStatus`) to the root `id.putra.core_erp.shared` package (or module enums), and delete all unused legacy response/param/helper boilerplate in `warehouse/shared`.

**Tech Stack:** Java 21, Spring Boot 3.x, Spring Data JPA, Lombok, Vaadin Flow.

---

### Task 1: Move shared dependencies to root shared package and delete unused warehouse/shared files

**Files:**
- Create: `src/main/java/id/putra/core_erp/shared/dto/BaseDto.java`
- Create: `src/main/java/id/putra/core_erp/shared/dto/ProductDto.java`
- Create: `src/main/java/id/putra/core_erp/shared/enums/OrderStatus.java`
- Create: `src/main/java/id/putra/core_erp/shared/enums/ReceivingStatus.java`
- Create: `src/main/java/id/putra/core_erp/shared/enums/ResponseEnum.java`
- Create: `src/main/java/id/putra/core_erp/shared/exception/ModuleException.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/helpers/ResponseHelper.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/converter/DtoConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/converter/ProductConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/BaseDto.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/ProductDto.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/param/FilterParam.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/param/SearchParam.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/param/SortParam.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/PaginatedData.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/ResponseData.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/ResponseError.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/ResponseList.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/ResponseMeta.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/attribute/ErrorAttribute.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/attribute/MetaAttribute.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/base/dto/response/attribute/ResponseSchemaAttribute.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/enums/OrderStatus.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/enums/ReceivingStatus.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/enums/ResponseEnum.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/shared/exceptions/ModuleException.java`

**Details:**
- Move needed classes to `id.putra.core_erp.shared.*` with updated package declarations.
- Add `toEntity()` and `toDto(Product entity)` static/helper mapping to `ProductDto` to make it a self-contained simple DTO.
- Remove all unused boilerplate response wrappers and param classes.

---

### Task 2: Refactor Customer submodule (Remove converters, use Simple DTOs)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/dto/CustomerDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/dto/ContactPersonCustomerDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/service/core/CustomerService.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/service/adapter/command/impl/CustomerCommandAdapterImpl.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/service/adapter/command/impl/ContactPersonCustomerCommandAdapterImpl.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/service/adapter/query/impl/CustomerQueryAdapterImpl.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/customer/service/adapter/query/impl/ContactPersonCustomerQueryAdapterImpl.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/customer/converter/CustomerConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/customer/converter/ContactPersonCustomerConverter.java`

**Details:**
- Equip `CustomerDto` and `ContactPersonCustomerDto` with `toDto(entity)` and `toEntity()` mapping methods directly on the DTO classes.
- Update `CustomerService` and query/command adapters to use `CustomerDto::toDto`, `CustomerDto::toEntity`, `ContactPersonCustomerDto::toDto`, `ContactPersonCustomerDto::toEntity`.
- Delete `CustomerConverter.java` and `ContactPersonCustomerConverter.java`.

---

### Task 3: Refactor Supplier submodule (Remove converters, use Simple DTOs)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/dto/SupplierDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/dto/ProductSupplierDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/dto/ContactPersonSupplierDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/dto/ProductSupplierIdDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/service/adapter/command/impl/SupplierCommandAdapterImpl.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/supplier/service/adapter/query/impl/SupplierQueryAdapterImpl.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/supplier/converter/SupplierConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/supplier/converter/ProductSupplierConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/supplier/converter/ProductSupplierIdConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/supplier/converter/ContactPersonSupplierConverter.java`

**Details:**
- Add conversion methods directly into DTOs or map inline.
- Update adapters to call DTO methods directly and delete converter classes.

---

### Task 4: Refactor Warehouse (Zone, Rack, Location, Warehouse, ContactPerson) submodule

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/dto/WarehouseDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/dto/ZoneDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/dto/RackDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/dto/LocationDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/dto/ContactPersonWarehouseDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/service/adapter/query/impl/*`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/service/adapter/command/impl/*`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/converter/WarehouseConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/converter/ZoneConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/converter/RackConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/converter/LocationConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/warehouse/converter/ContactPersonWarehouseConverter.java`

**Details:**
- Move mapping logic into simple DTO methods (`WarehouseDto::toDto`, `WarehouseDto::toEntity`, etc.).
- Update query & command adapters and delete all converter classes.

---

### Task 5: Refactor Inventory submodule (Category, InventoryItem, Product)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inventory/dto/CategoryDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inventory/dto/InventoryItemDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inventory/service/adapter/query/impl/*`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inventory/service/adapter/command/impl/*`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/inventory/converter/CategoryConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/inventory/converter/InventoryItemConverter.java`

**Details:**
- Implement `toDto()` and `toEntity()` inside `CategoryDto` and `InventoryItemDto`.
- Update adapters to use `ProductDto::toDto` / `ProductDto::toEntity` from root shared DTO.
- Delete `CategoryConverter.java` and `InventoryItemConverter.java`.

---

### Task 6: Refactor Inbound submodule (Receiving, QualityInspection, PurchaseOrder)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/dto/PurchaseOrderDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/dto/PurchaseOrderLineDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/dto/ReceivingDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/dto/ReceivingLineDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/dto/QualityInspectionDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/service/adapter/query/impl/*`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/inbound/service/adapter/command/impl/*`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/inbound/converter/PurchaseOrderConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/inbound/converter/QualityInspectionConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/inbound/converter/ReceivingConverter.java`

**Details:**
- Provide direct mapping in Inbound DTOs.
- Update `OrderStatus` and `ReceivingStatus` imports to `id.putra.core_erp.shared.enums.*`.
- Update adapters and delete all converter classes.

---

### Task 7: Refactor Outbound submodule (Carrier, Packing, PickingTask, SalesOrder, Shipment)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/CarrierDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/PackingDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/PickingTaskDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/SalesOrderDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/SalesOrderLineDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/dto/ShipmentDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/service/adapter/query/impl/*`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/outbound/service/adapter/command/impl/*`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/CarrierConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/PackingConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/PickingTaskConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/SalesOrderConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/SalesOrderLineConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/outbound/converter/ShipmentConverter.java`

**Details:**
- Add `toDto()` and `toEntity()` to Outbound DTOs.
- Update adapters and delete converter classes.

---

### Task 8: Refactor Transaction submodule (Transaction, TransactionItem)

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/transaction/dto/TransactionDto.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/module/transaction/dto/TransactionItemDto.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/transaction/converter/TransactionConverter.java`
- Delete: `src/main/java/id/putra/core_erp/warehouse/module/transaction/converter/TransactionItemConverter.java`

**Details:**
- Add `toDto()` and `toEntity()` to `TransactionDto` and `TransactionItemDto`.
- Delete converter classes.

---

### Task 9: Update Warehouse UI Views imports and verify build

**Files:**
- Modify: `src/main/java/id/putra/core_erp/warehouse/ui/views/master/ProductView.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/ui/views/inventory/InventoryView.java`
- Modify: `src/main/java/id/putra/core_erp/warehouse/ui/views/dashboard/DashboardView.java`
- Modify: Any other UI views or services referencing deleted packages

**Details:**
- Update all remaining imports from `id.putra.core_erp.warehouse.shared.*` to `id.putra.core_erp.shared.*`.
- Execute `./mvnw clean test-compile` and `./mvnw test` to ensure successful compilation and verify all unit tests pass without regressions.
