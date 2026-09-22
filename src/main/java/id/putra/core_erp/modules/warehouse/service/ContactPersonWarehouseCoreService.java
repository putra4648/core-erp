package id.putra.core_erp.modules.warehouse.service;

import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;

import java.util.List;


import id.putra.core_erp.modules.warehouse.model.ContactPersonWarehouse;
import id.putra.core_erp.modules.warehouse.repository.ContactPersonWarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ContactPersonWarehouseCoreService implements BaseService<ContactPersonWarehouse> {

    private final ContactPersonWarehouseRepository contactPersonWarehouseRepository;

    public ContactPersonWarehouseCoreService(ContactPersonWarehouseRepository contactPersonWarehouseRepository) {
        this.contactPersonWarehouseRepository = contactPersonWarehouseRepository;
    }

    public ContactPersonWarehouse getContact(ContactPersonWarehouse dto) {
        return contactPersonWarehouseRepository.findById(dto.getId());
    }

    public void save(ContactPersonWarehouse entity) {
        contactPersonWarehouseRepository.persist(entity);
    }

    public ContactPersonWarehouse findById(Long id) {
        return contactPersonWarehouseRepository.findById(id);
    }

    public Iterable<ContactPersonWarehouse> findAll(ContactPersonWarehouse cpw, Page p) {
        return contactPersonWarehouseRepository.findAll().list();
    }

    public void add(List<ContactPersonWarehouse> dtos) {
        contactPersonWarehouseRepository.persist(dtos);
    }

    public void update(List<ContactPersonWarehouse> dtos) {
        dtos.forEach(dto -> contactPersonWarehouseRepository.getEntityManager().merge(dto));
    }

    public void update(ContactPersonWarehouse entity) {
        contactPersonWarehouseRepository.getEntityManager().merge(entity);
    }

    public void delete(List<ContactPersonWarehouse> dtos) {
        dtos.forEach(dto -> contactPersonWarehouseRepository.deleteById(dto.getId()));
    }

    public void delete(Long id) {
        contactPersonWarehouseRepository.deleteById(id);
    }

}
