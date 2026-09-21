package id.putra.core_erp.modules.warehouse.repository;

import id.putra.core_erp.modules.warehouse.model.ContactPersonWarehouse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactPersonWarehouseRepository implements PanacheRepository<ContactPersonWarehouse> {

}
