package id.putra.core_erp.warehouse.module.inbound.service.impl;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.data.provider.DataProvider;

import id.putra.core_erp.warehouse.module.inbound.model.entity.Receiving;
import id.putra.core_erp.warehouse.module.inbound.model.repository.ReceivingRepository;
import id.putra.core_erp.warehouse.module.inbound.service.ReceivingOrderService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ReceivingOrderServiceImpl implements ReceivingOrderService {

    private final ReceivingRepository receivingRepository;

    public ReceivingOrderServiceImpl(ReceivingRepository receivingRepository) {
        this.receivingRepository = receivingRepository;
    }

    @Override
    public Receiving create(Receiving dto) {
        receivingRepository.persist(dto);
        return dto;
    }

    @Override
    public Receiving update(Receiving dto) {
        return receivingRepository.getEntityManager().merge(dto);
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        return receivingRepository.deleteById(id);
    }

    @Override
    public DataProvider<Receiving, Void> getAll(String value, Page page) {
        return DataProvider.fromCallbacks(
                query -> receivingRepository.findAll()
                        .page(Page.of(query.getOffset() / query.getLimit(), query.getLimit()))
                        .list().stream(),
                query -> Math.toIntExact(receivingRepository.count()));
    }

}
