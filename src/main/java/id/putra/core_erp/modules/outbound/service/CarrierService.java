package id.putra.core_erp.modules.outbound.service;

import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;

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

    public void save(Carrier entity) {
        carrierRepository.persist(entity);
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

    public Iterable<Carrier> findAll(Carrier c, Page p) {
        return carrierRepository.findAll().list();
    }
}
