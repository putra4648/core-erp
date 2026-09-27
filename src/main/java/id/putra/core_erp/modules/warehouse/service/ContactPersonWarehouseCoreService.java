package id.putra.core_erp.modules.warehouse.service;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.ContactPersonWarehouse;
import id.putra.core_erp.modules.warehouse.repository.ContactPersonWarehouseRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ContactPersonWarehouseCoreService implements BaseService<ContactPersonWarehouse> {
    private final ContactPersonWarehouseRepository contactPersonWarehouseRepository;

    public ContactPersonWarehouseCoreService(ContactPersonWarehouseRepository contactPersonWarehouseRepository) {
        this.contactPersonWarehouseRepository = contactPersonWarehouseRepository;
    }

    public void save(ContactPersonWarehouse entity) {
        contactPersonWarehouseRepository.persist(entity);
    }

    public ContactPersonWarehouse findById(Long id) {
        return contactPersonWarehouseRepository.findById(id);
    }

    public void delete(Long id) {
        contactPersonWarehouseRepository.deleteById(id);
    }

    public List<ContactPersonWarehouse> findAll(ContactPersonWarehouse cpw, Page p) {
        return contactPersonWarehouseRepository.findAll().list();
    }

    @Override
    public void update(ContactPersonWarehouse entity) {
        contactPersonWarehouseRepository.update(
                "name = ?1, phone = ?2, email = ?3 where id = ?4",
                entity.getName(), entity.getPhone(), entity.getEmail(), entity.getId());
    }
}
