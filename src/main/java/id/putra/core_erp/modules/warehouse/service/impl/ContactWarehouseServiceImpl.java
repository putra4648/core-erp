package id.putra.core_erp.modules.warehouse.service.impl;

import java.util.List;

import id.putra.core_erp.modules.warehouse.model.ContactPersonWarehouse;
import id.putra.core_erp.modules.warehouse.repository.ContactPersonWarehouseRepository;
import id.putra.core_erp.modules.warehouse.service.ContactPersonWarehouseCoreService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ContactWarehouseServiceImpl implements ContactPersonWarehouseCoreService {

    private final ContactPersonWarehouseRepository contactPersonWarehouseRepository;

    public ContactWarehouseServiceImpl(ContactPersonWarehouseRepository contactPersonWarehouseRepository) {
        this.contactPersonWarehouseRepository = contactPersonWarehouseRepository;
    }

    @Override
    public ContactPersonWarehouse getContact(ContactPersonWarehouse dto) {
        return contactPersonWarehouseRepository.findById(dto.getId());
    }

    @Override
    public void add(List<ContactPersonWarehouse> dtos) {
        contactPersonWarehouseRepository.persist(dtos);
    }

    @Override
    public void update(List<ContactPersonWarehouse> dtos) {
        dtos.forEach(dto -> contactPersonWarehouseRepository.getEntityManager().merge(dto));
    }

    @Override
    public void delete(List<ContactPersonWarehouse> dtos) {
        dtos.forEach(dto -> contactPersonWarehouseRepository.deleteById(dto.getId()));
    }

}
