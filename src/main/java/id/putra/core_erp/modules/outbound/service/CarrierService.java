package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.shared.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.outbound.model.Carrier;
import id.putra.core_erp.modules.outbound.repository.CarrierRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CarrierService implements BaseService<Carrier> {

    private final CarrierRepository carrierRepository;

    public CarrierService(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }
    public Carrier save(Carrier entity) {
        carrierRepository.persist(entity);
        return entity;
    }
    public Carrier create(Carrier dto) {
        carrierRepository.persist(dto);
        return dto;
    }
    public void update(Carrier dto) {
        carrierRepository.getEntityManager().merge(dto);
    }
    public void delete(Long id) {
        carrierRepository.deleteById(id);
    }
    public Carrier getById(Long id) {
        return carrierRepository.findById(id);
    }
    public Carrier findById(Long id) {
        return carrierRepository.findById(id);
    }
    public Iterable<Carrier> findAll() {
        return carrierRepository.findAll().list();
    }
    public List<Carrier> getAll(Carrier filter) {
        return carrierRepository.findAll().list();
    }
}
