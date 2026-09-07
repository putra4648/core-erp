package id.putra.core_erp.purchase_requisition.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.AppDto;
import id.putra.core_erp.purchase_requisition.dto.InventoryDto;
import id.putra.core_erp.purchase_requisition.model.Inventory;
import id.putra.core_erp.purchase_requisition.repository.InventoryRepo;
import id.putra.core_erp.purchase_requisition.service.InventoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String handleData(AppDto<InventoryDto> dto) {
        if (dto != null && dto.getParams() != null) {
            Inventory entity = dtoToEntity(new Inventory(), dto.getParams());
            inventoryRepo.persist(entity);
            return MessageConstant.SUCCESS;
        }
        return null;
    }

    public void handleAllData(AppDto<List<InventoryDto>> dto) {
        List<InventoryDto> params = dto.getParams();

        if (params != null) {
            params.forEach(param -> {
                Optional<Inventory> invOpt = inventoryRepo.findByIdOptional(param.getPart_number());
                Inventory entity;
                if (invOpt.isPresent()) {
                    entity = invOpt.get();

                    if (param.getPart_name() != null && !param.getPart_name().isBlank()) {
                        entity.setPartName(param.getPart_name());
                    }
                    if (param.getSupplier() != null && !param.getSupplier().isBlank()) {
                        entity.setSupplier(param.getSupplier());
                    }
                    if (param.getUom() != null && !param.getUom().isBlank()) {
                        entity.setUom(param.getUom());
                    }
                    entity.setPoNumber(param.getPo_number());
                    if (param.getQty() != null) {
                        entity.setQty((entity.getQty() != null ? entity.getQty() : java.math.BigInteger.ZERO)
                                .add(param.getQty()));
                    }
                    entity.setShelf(param.getShelf());
                    entity.setDate(param.getDate());
                    entity.setLocation(param.getLocation());

                } else {
                    entity = dtoToEntity(new Inventory(), param);
                }
                inventoryRepo.persist(entity);
            });
        }
    }

    @Override
    public List<InventoryDto> getAll(AppDto<InventoryDto> dto) {
        List<InventoryDto> dtos = new ArrayList<>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Inventory> query = cb.createQuery(Inventory.class);
        Root<Inventory> root = query.from(Inventory.class);

        String searchStr = (dto != null && dto.getParams() != null && dto.getParams().getSearch() != null)
                ? dto.getParams().getSearch().toUpperCase()
                : "";

        Predicate partNumber = cb.like(cb.upper(root.get("partNumber")), "%" + searchStr + "%");
        Predicate partName = cb.like(cb.upper(root.get("partName")), "%" + searchStr + "%");
        Predicate orResult = cb.or(partNumber, partName);
        List<Inventory> result = em.createQuery(query.select(root).where(orResult)).getResultList();
        result.forEach(data -> dtos.add(entityToDto(data)));

        return dtos;
    }

    @Override
    public InventoryDto getSingleData(AppDto<InventoryDto> dto) {
        if (dto != null && dto.getParams() != null && dto.getParams().getPart_number() != null) {
            return inventoryRepo.findById(dto.getParams().getPart_number()).map(this::entityToDto).orElse(null);
        }
        return null;
    }

    @Override
    public Long countAll(AppDto<InventoryDto> dto) {
        return inventoryRepo.count();
    }

    private InventoryDto entityToDto(Inventory entity) {
        InventoryDto dto = new InventoryDto();
        dto.setPo_number(entity.getPoNumber());
        dto.setPart_number(entity.getPartNumber());
        dto.setPart_name(entity.getPartName());
        dto.setDescription(entity.getDescription());
        dto.setArea(entity.getArea());
        dto.setShelf(entity.getShelf());
        dto.setSupplier(entity.getSupplier());
        dto.setUnit(entity.getUnit());
        dto.setQty(entity.getQty());
        dto.setItem_area(entity.getItemArea());
        dto.setDate(entity.getDate());
        dto.setLocation(entity.getLocation());
        dto.setUom(entity.getUom());
        return dto;
    }

    private Inventory dtoToEntity(Inventory entity, InventoryDto dto) {
        if (dto.getPart_number() != null) {
            entity.setPartNumber(dto.getPart_number());
        }
        entity.setPoNumber(dto.getPo_number());
        entity.setPartName(dto.getPart_name());
        entity.setDescription(dto.getDescription());
        entity.setArea(dto.getArea());
        entity.setShelf(dto.getShelf());
        entity.setSupplier(dto.getSupplier());
        entity.setUnit(dto.getUnit());
        entity.setQty(dto.getQty());
        entity.setItemArea(dto.getItem_area());
        entity.setDate(dto.getDate());
        entity.setLocation(dto.getLocation());
        entity.setUom(dto.getUom());
        return entity;
    }

}
