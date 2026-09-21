package id.putra.core_erp.modules.inbound.service;

import id.putra.core_erp.shared.service.BaseService;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.Return;
import id.putra.core_erp.modules.inbound.repository.ReturnRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReturnInboundService implements BaseService<Return> {

    private final ReturnRepository returnRepository;

    public ReturnInboundService(ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }
    public Return save(Return entity) {
        returnRepository.persist(entity);
        return entity;
    }
    public Return create(Return dto) {
        returnRepository.persist(dto);
        return dto;
    }
    public void update(Return dto) {
        returnRepository.getEntityManager().merge(dto);
    }
    public void delete(Long id) {
        returnRepository.deleteById(id);
    }
    public Return findById(Long id) {
        return returnRepository.findById(id);
    }

    public Return getById(Long id) {
        return findById(id);
    }
    public Iterable<Return> findAll() {
        return returnRepository.findAll().list();
    }
    public List<Return> getAll(Return filter) {
        return returnRepository.findAll().list();
    }
}
