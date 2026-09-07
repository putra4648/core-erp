package id.putra.core_erp.warehouse.module.inbound.service.adapter.query.impl;

import java.util.List;

import org.jspecify.annotations.NonNull;

import id.putra.core_erp.warehouse.module.inbound.dto.QualityInspectionDto;
import id.putra.core_erp.warehouse.module.inbound.model.repository.QualityInspectionRepository;
import id.putra.core_erp.warehouse.module.inbound.service.adapter.query.InspectionQueryAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InspectionQueryAdapterImpl implements InspectionQueryAdapter {

    @Inject
    private QualityInspectionRepository qualityInspectionRepository;

    @Override
    public QualityInspectionDto getById(@NonNull Long id) {
        // return
        // qualityInspectionRepository.findById(id).map(QualityInspectionDto::toDto).orElse(null);
        return null;
    }

    @Override
    public List<QualityInspectionDto> getAll(QualityInspectionDto filter) {
        // return
        // qualityInspectionRepository.findAll().list().stream().map(QualityInspectionDto::toDto).toList();
        return null;
    }
}
