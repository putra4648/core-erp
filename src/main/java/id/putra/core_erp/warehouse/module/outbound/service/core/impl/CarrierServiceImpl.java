package id.putra.core_erp.warehouse.module.outbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.outbound.model.entity.Carrier;
import id.putra.core_erp.warehouse.module.outbound.model.repository.CarrierRepository;
import id.putra.core_erp.warehouse.module.outbound.service.core.CarrierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CarrierServiceImpl implements CarrierService {
    @Inject
    private CarrierRepository carrierRepository;

    @Override
    public Carrier create(Carrier dto) {
        carrierRepository.persist(dto);
        return dto;
    }

    @Override
    public Carrier update(Carrier dto) {
        carrierRepository.getEntityManager().merge(dto);
        return dto;
    }

    @Override
    public Boolean delete(Long id) {
        return carrierRepository.deleteById(id);
    }

    @Override
    public Carrier getById(Long id) {
        return carrierRepository.findById(id);
    }

    @Override
    public List<Carrier> getAll(Carrier filter) {
        return carrierRepository.findAll().list();
    }
}
