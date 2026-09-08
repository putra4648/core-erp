package id.putra.core_erp.warehouse.module.inbound.service.adapter.query;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.PurchaseOrder;
import io.quarkus.panache.common.Page;

public interface PurchaseOrderQueryAdapter {

    List<PurchaseOrder> list(PurchaseOrder dto, Page page);

}
