package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.AppDto;
import id.putra.core_erp.purchase_requisition.dto.AppFileDto;
import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.RFQDto;
import id.putra.core_erp.purchase_requisition.dto.SupplierDto;
import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.exception.RFQException;
import id.putra.core_erp.purchase_requisition.exception.RequisitionException;
import id.putra.core_erp.purchase_requisition.model.RFQ;
import id.putra.core_erp.purchase_requisition.model.Users;
import id.putra.core_erp.purchase_requisition.repository.RFQRepo;
import id.putra.core_erp.purchase_requisition.repository.UserRepo;
import id.putra.core_erp.purchase_requisition.service.RFQService;
import id.putra.core_erp.purchase_requisition.util.AppFileUtil;
import id.putra.core_erp.purchase_requisition.util.AppUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("rfqService")
@RequiredArgsConstructor
@Transactional(readOnly = false, rollbackFor = { RFQException.class, Exception.class })
@Slf4j
public class RFQServiceImpl implements RFQService {

    private final RFQRepo rfqRepository;
    private final UserRepo userRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String handleData(AppDto<RFQDto> dto) {
        RFQDto params = dto.getParams();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (params.getId() == null || params.getId().isBlank()) {
            Long count = rfqRepository.count();
            String id = AppUtil.generatedIdFromZeros("RFQ", count);
            RFQ entity = dtoToEntityAdd(params, id, new RFQ());
            rfqRepository.save(entity);
            return MessageConstant.SUCCESS;
        } else {
            Optional<RFQ> rfqOpt = rfqRepository.findById(params.getId());

            if (rfqOpt.isPresent()) {
                Optional<Users> userOpt = userRepo
                        .findByUsernameIgnoreCase(authentication != null ? authentication.getName() : "");
                RFQ entity = dtoToEntityUpdate(userOpt.map(u -> u.getNrp()).orElse(null), params,
                        rfqOpt.get());
                rfqRepository.save(entity);
            } else {
                throw new RFQException("RFQ is not found");
            }
            return MessageConstant.UPDATE;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RFQDto> getAll(AppDto<RFQDto> dto) {
        RFQDto params = dto != null ? dto.getParams() : null;
        List<RFQDto> dtos = new ArrayList<>();

        if (params == null) {
            rfqRepository.findAll().forEach(r -> dtos.add(entityToDto(r)));
            return dtos;
        }

        StringBuilder builder = new StringBuilder("select " +
                "r.* " +
                "from " +
                "rfq r " +
                "cross join lateral jsonb_array_elements(r.details) as element where 1=1 \n");

        if (params.getStatus() != null && !params.getStatus().isBlank()) {
            builder.append("and r.status like '%' || :status || '%' \n");
        }

        if (params.getSearch() != null && !params.getSearch().isBlank()) {
            builder.append("and r.id like '%' || :search || '%' \n");
        }

        if (params.getMin_budget() != null && params.getMax_budget() != null) {
            builder.append("and (element->>'subtotal')::numeric between :min_budget and :max_budget \n");
        }

        if (params.getStart_date() != null && !params.getStart_date().isBlank() && params.getEnd_date() != null
                && !params.getEnd_date().isBlank()) {
            builder.append(
                    "and r.created_date between to_date(:from_date,'YYYY-MM-DD') and to_date(:to_date,'YYYY-MM-DD') \n");
        }

        builder.append("order by r.id asc \n");

        Query query = entityManager.createNativeQuery(builder.toString(), RFQ.class);

        if (params.getStatus() != null && !params.getStatus().isBlank()) {
            query.setParameter("status", params.getStatus());
        }

        if (params.getSearch() != null && !params.getSearch().isBlank()) {
            query.setParameter("search", params.getSearch());
        }

        if (params.getMin_budget() != null && params.getMax_budget() != null) {
            query.setParameter("min_budget", params.getMin_budget());
            query.setParameter("max_budget", params.getMax_budget());
        }

        if (params.getStart_date() != null && !params.getStart_date().isBlank() && params.getEnd_date() != null
                && !params.getEnd_date().isBlank()) {
            query.setParameter("from_date", params.getStart_date());
            query.setParameter("to_date", params.getEnd_date());
        }

        List<RFQ> resultList = query.getResultList();
        resultList.forEach((data) -> dtos.add(entityToDto(data)));

        return dtos;
    }

    @Override
    public RFQDto getSingleData(AppDto<RFQDto> dto) {
        String id = dto.getParams().getId();
        Optional<RFQ> rfq = rfqRepository.findById(id);
        return rfq.map(this::entityToDto).orElse(null);
    }

    @Override
    public Long countAll(AppDto<RFQDto> dto) {
        return rfqRepository.count();
    }

    @Override
    public Boolean checkApprovalFlow(AppDto<RFQDto> dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String id = dto.getParams().getId();
        Optional<RFQ> rfqOpt = rfqRepository.findById(id);
        if (rfqOpt.isPresent()) {
            Optional<Users> userOpt = userRepo
                    .findByUsernameIgnoreCase(authentication != null ? authentication.getName() : "");
            String userNrp = userOpt.map(u -> u.getNrp()).orElse("");
            if (rfqOpt.get().getTemApprovalDtos() != null) {
                return rfqOpt.get().getTemApprovalDtos().stream()
                        .anyMatch(aprv -> aprv.getUsers() != null && aprv.getUsers().getNrp() != null
                                && aprv.getUsers().getNrp().equalsIgnoreCase(userNrp));
            }
            return false;
        } else {
            throw new RFQException("RFQ " + id + " not found");
        }
    }

    @Override
    public ApprovalEnum checkStatusApprovalIsPermanent(AppDto<RFQDto> dto) {
        RFQDto params = dto.getParams();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Users> user = userRepo
                .findByUsernameIgnoreCase(authentication != null ? authentication.getName() : "");
        String nrp = user.map(u -> u.getNrp()).orElse(null);

        String id = params.getId();
        Optional<RFQ> rfqOpt = rfqRepository.findById(id);
        if (rfqOpt.isPresent()) {
            ApprovalEnum result = ApprovalEnum.NON_APPROVAL;
            List<ApprovalDto> approvals = new ArrayList<>();
            rfqOpt.ifPresent(data -> {
                if (data.getTemApprovalDtos() != null) {
                    approvals.addAll(data.getTemApprovalDtos());
                }
            });
            if (nrp != null) {
                int index = 0;
                for (int i = 0; i < approvals.size(); i++) {
                    if (approvals.get(i).getUsers() != null
                            && nrp.equalsIgnoreCase(approvals.get(i).getUsers().getNrp())) {
                        index = i;
                    }
                }
                if (approvals.stream().anyMatch(
                        data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers().getNrp()))) {

                    if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                        if (approvals.stream().anyMatch(
                                data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers().getNrp())
                                        && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                            result = ApprovalEnum.ALREADY_APPROVED;
                        }

                        if (approvals.stream().anyMatch(
                                data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers().getNrp())
                                        && !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
                                && index == (rfqOpt.get().getCurrentApprovalStage() != null
                                        ? rfqOpt.get().getCurrentApprovalStage()
                                        : 0)) {
                            result = ApprovalEnum.NEED_APPROVAL;
                        }

                    }
                }
            }

            return result;
        } else {
            throw new RequisitionException("RFQ " + id + " not found");
        }
    }

    @Override
    public Resource download(AppDto<RFQDto> dto) {
        RFQDto params = dto.getParams();

        String id = params.getId();
        String supplierId = params.getSupplierId();

        Optional<RFQ> rfqOpt = rfqRepository.findById(id);
        if (rfqOpt.isPresent()) {
            try {
                SupplierDto supplierDto = rfqOpt.get().getSuppliers().stream()
                        .filter(data -> data.getAcc_no() != null && data.getAcc_no().equalsIgnoreCase(supplierId))
                        .findFirst().orElse(null);
                if (supplierDto != null && supplierDto.getFile() != null && supplierDto.getFile().getName() != null) {
                    Path path = Paths.get(AppFileUtil.getServerPath(), "data", "rfq",
                            supplierDto.getFile().getName());
                    return new InputStreamResource(new FileInputStream(path.toFile()));
                }
                return null;
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException("Error reading file", e);
            }
        } else {
            throw new RFQException("File ID " + id + " not found");
        }
    }

    private RFQ dtoToEntityAdd(RFQDto dto, String id, RFQ entity) {
        if (id != null) {
            entity.setId(id);
        }
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDue_date());
        entity.setNote(dto.getNote());
        entity.setDetails(dto.getDetails());
        entity.setRejectNote(dto.getReject_note());

        List<SupplierDto> suppliers = new ArrayList<>();
        if (dto.getSuppliers() != null) {
            dto.getSuppliers().forEach(suppliers::add);
        }
        entity.setSuppliers(suppliers);

        return entity;
    }

    private RFQ dtoToEntityUpdate(String nrp, RFQDto dto, RFQ entity) {
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDue_date());
        entity.setNote(dto.getNote());
        entity.setDetails(dto.getDetails());
        entity.setRejectNote(dto.getReject_note());

        List<ApprovalDto> approvalDtos = entity.getTemApprovalDtos();

        if (approvalDtos != null) {
            Optional<ApprovalDto> approvalOpt = approvalDtos.stream()
                    .filter(aprv -> aprv.getUsers() != null && aprv.getUsers().getNrp() != null
                            && aprv.getUsers().getNrp().equalsIgnoreCase(nrp))
                    .findFirst();
            if (approvalOpt.isPresent()) {
                List<ApprovalDto> entityApproval = approvalDtos.stream().map(data -> {
                    if (data.getUsers() != null && data.getUsers().getNrp() != null
                            && data.getUsers().getNrp().equalsIgnoreCase(nrp)) {
                        ApprovalDto newData = data;
                        if (Boolean.TRUE.equals(data.getIsApprovalChecked())) {
                            newData.setIsApprovalPermanent(true);
                            int nextStage = (entity.getCurrentApprovalStage() != null ? entity.getCurrentApprovalStage()
                                    : 0) + 1;
                            entity.setCurrentApprovalStage(nextStage);

                            if ("REJECT".equalsIgnoreCase(dto.getStatus())) {
                                entity.setRejectNote(dto.getReject_note());
                                entity.setCurrentApprovalStage(0);
                                entity.setLastApproved(null);
                                entity.setStatus("REJECT");
                            } else if ("DRAFT".equalsIgnoreCase(dto.getStatus())) {
                                entity.setStatus("DRAFT");
                                entity.setCurrentApprovalStage(0);
                                entity.setLastApproved(null);
                                List<ApprovalDto> updateApprovalDtos = approvalDtos.stream().map(aprv -> {
                                    aprv.setIsApprovalPermanent(false);
                                    return aprv;
                                }).toList();
                                entity.setTemApprovalDtos(updateApprovalDtos);
                            } else {
                                entity.setStatus(dto.getStatus());
                                if (entity.getCurrentApprovalStage() != null
                                        && entity.getCurrentApprovalStage().equals(entity.getTotalApprovalStage())) {
                                    entity.setStatus("APPROVED");
                                }
                                entity.setLastApproved(approvalOpt.get().getUsers().getFullname());
                            }
                        }
                        return newData;
                    }
                    return data;
                }).toList();
                entity.setTemApprovalDtos(entityApproval);
            }
        } else {
            if (dto.getApprovals() != null) {
                entity.setCurrentApprovalStage(0);
                entity.setTotalApprovalStage(dto.getApprovals().size());
                entity.setTemApprovalDtos(dto.getApprovals());
            } else {
                throw new RFQException("Approval cannot be null");
            }
        }

        List<SupplierDto> suppliers = new ArrayList<>();
        if (dto.getSuppliers() != null) {
            dto.getSuppliers().forEach(data -> {
                SupplierDto supplierDto = data;
                if (data.getFile() != null) {
                    try {
                        AppFileDto file = supplierDto.getFile();
                        AppFileUtil.createFileFromBase64(data.getFile().getName(), "rfq", file);
                        file.setContent(null);
                        supplierDto.setFile(file);

                        suppliers.add(data);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e.getCause());
                    }
                }
            });
        }
        if (dto.getSuppliers() != null) {
            entity.setSuppliers(dto.getSuppliers().stream().map(data -> {
                SupplierDto updatedData = data;
                AppFileDto file = null;
                if (updatedData.getFile() != null) {
                    file = updatedData.getFile();
                    file.setContent(null);
                    updatedData.setFile(file);
                } else {
                    updatedData.setFile(null);
                }
                return updatedData;
            }).toList());
        }

        return entity;
    }

    private RFQDto entityToDto(RFQ entity) {
        RFQDto dto = new RFQDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setDetails(entity.getDetails());
        dto.setDue_date(entity.getDueDate());
        dto.setNote(entity.getNote());

        dto.setCreated_by(entity.getUserCreateBy());
        if (entity.getCreatedDate() != null) {
            dto.setCreated_date(Date.from(entity.getCreatedDate()));
        }
        dto.setUpdated_by(entity.getUserUpdateBy());
        if (entity.getUpdatedDate() != null) {
            dto.setUpdated_date(Date.from(entity.getUpdatedDate()));
        }

        List<SupplierDto> supplierDtos = new ArrayList<>();

        if (entity.getSuppliers() != null) {
            entity.getSuppliers().forEach(supplierDtos::add);
        }
        dto.setSuppliers(supplierDtos);
        dto.setLast_approved(entity.getLastApproved());

        List<String> nextApprovals = new ArrayList<>();

        if (entity.getTemApprovalDtos() != null) {
            dto.setApprovals(entity.getTemApprovalDtos());
            nextApprovals.addAll(entity.getTemApprovalDtos().stream()
                    .filter(data -> !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
                    .filter(data -> data.getUsers() != null && data.getUsers().getFullname() != null)
                    .map(data -> data.getUsers().getFullname()).toList());
        }

        dto.setNext_approvals(nextApprovals);

        return dto;
    }

}
