package id.putra.core_erp.warehouse.module.inbound.service.core.impl;

import java.util.List;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Return;
import id.putra.core_erp.warehouse.module.inbound.model.repository.ReturnRepository;
import id.putra.core_erp.warehouse.module.inbound.service.core.ReturnInboundService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReturnInboundServiceImpl implements ReturnInboundService {

    @Inject
    private ReturnRepository returnRepository;

    @Override
    public Return create(Return dto) {
        returnRepository.persist(dto);
        return dto;
    }

    @Override
    public Return update(Return dto) {
        return returnRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(Long id) {
        return returnRepository.deleteById(id);
    }

    @Override
    public Return getById(Long id) {
        return returnRepository.findById(id);
    }

    @Override
    public List<Return> getAll(Return filter) {
        return returnRepository.findAll().list();
    }
}
