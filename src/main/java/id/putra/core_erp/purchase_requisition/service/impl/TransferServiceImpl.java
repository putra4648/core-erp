package id.putra.core_erp.purchase_requisition.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
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

import id.putra.core_erp.purchase_requisition.constant.MessageConstant;
import id.putra.core_erp.purchase_requisition.dto.AppDto;
import id.putra.core_erp.purchase_requisition.dto.AppFileDto;
import id.putra.core_erp.purchase_requisition.dto.ApprovalDto;
import id.putra.core_erp.purchase_requisition.dto.DepartmentDto;
import id.putra.core_erp.purchase_requisition.dto.TransferDetailDto;
import id.putra.core_erp.purchase_requisition.dto.TransferDto;
import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.enums.RequisitionEnum;
import id.putra.core_erp.purchase_requisition.exception.RequisitionException;
import id.putra.core_erp.purchase_requisition.exception.TransferException;
import id.putra.core_erp.purchase_requisition.model.Department;
import id.putra.core_erp.purchase_requisition.model.Inventory;
import id.putra.core_erp.purchase_requisition.model.Transfer;
import id.putra.core_erp.purchase_requisition.model.Users;
import id.putra.core_erp.purchase_requisition.repository.InventoryRepo;
import id.putra.core_erp.purchase_requisition.repository.TransferRequestRepo;
import id.putra.core_erp.purchase_requisition.repository.UserRepo;
import id.putra.core_erp.purchase_requisition.service.TransferService;
import id.putra.core_erp.purchase_requisition.util.AppFileUtil;
import id.putra.core_erp.purchase_requisition.util.AppUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("transferService")
@RequiredArgsConstructor
@Transactional(rollbackOn = { RuntimeException.class })
public class TransferServiceImpl implements TransferService {

    private final InventoryRepo inventoryRepo;
    private final TransferRequestRepo transferRequestRepo;
    private final UserRepo userRepo;

    @Override
    public String handleData(AppDto<TransferDto> dto) {
        TransferDto params = dto.getParams();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long count = transferRequestRepo.count();
        if (params.getId() == null || params.getId().isBlank()) {
            String id = AppUtil.generatedIdFromZeros("TFQ", count);
            Transfer entity = dtoToEntityAdd(params, id, new Transfer());
            transferRequestRepo.save(entity);
            return MessageConstant.SUCCESS;
        } else {
            Optional<Transfer> rfqOpt = transferRequestRepo.findById(params.getId());
            if (rfqOpt.isPresent()) {
                Optional<Users> userOpt = userRepo
                        .findByUsernameIgnoreCase(authentication != null ? authentication.getName() : "");
                Transfer entity = dtoToEntityUpdate(userOpt.map(u -> u.getNrp()).orElse(null), params, rfqOpt.get());
                transferRequestRepo.save(entity);
                return MessageConstant.UPDATE;
            } else {
                String id = AppUtil.generatedIdFromZeros("TFQ", count);
                Transfer entity = dtoToEntityAdd(params, id, new Transfer());
                transferRequestRepo.save(entity);
                return MessageConstant.SUCCESS;
            }
        }
    }

    @Override
    public List<TransferDto> getAll(AppDto<TransferDto> dto) {
        List<TransferDto> dtos = new ArrayList<>();
        String status = (dto != null && dto.getParams() != null && dto.getParams().getStatus() != null
                && !dto.getParams().getStatus().isBlank())
                        ? dto.getParams().getStatus()
                        : "%%";
        String id = (dto != null && dto.getParams() != null && dto.getParams().getId() != null
                && !dto.getParams().getId().isBlank())
                        ? dto.getParams().getId()
                        : "%%";

        transferRequestRepo.findByStatusLikeAndIdLike(status, id)
                .forEach(transfer -> dtos.add(entityToDto(transfer)));
        return dtos;
    }

    @Override
    public TransferDto getSingleData(AppDto<TransferDto> dto) {
        TransferDto result = null;

        TransferDto params = dto.getParams();
        String id = params.getId();

        Optional<Transfer> transferOpt = transferRequestRepo.findById(id);

        if (transferOpt.isPresent()) {
            result = new TransferDto();

            Transfer req = transferOpt.get();

            result.setId(req.getId());
            result.setTitle(req.getTitle());
            result.setNote(req.getNote());
            result.setDue_date(req.getDueDate());
            result.setStatus(req.getStatus());
            result.setReject_note(req.getRejectNote());

            if (req.getDepartment() != null) {
                DepartmentDto departmentDto = new DepartmentDto();

                departmentDto.setDept_name(req.getDepartment().getDeptName());
                departmentDto.setDept_number(req.getDepartment().getDeptNumber());
                result.setDepartment(departmentDto);
            }

            List<ApprovalDto> approvals = new ArrayList<>();
            if (req.getTemApprovalDtos() != null) {
                req.getTemApprovalDtos().forEach(approvals::add);
            }
            result.setApprovals(approvals);
            result.setPurchaser(req.getTempPurchaserApprovalDto());

            List<TransferDetailDto> detailDtos = new ArrayList<>();
            if (req.getDetails() != null) {

                req.getDetails().forEach(dtl -> {
                    TransferDetailDto detailDto = new TransferDetailDto();

                    detailDto.setItem_name(dtl.getItem_name());
                    detailDto.setItem_number(dtl.getItem_number());
                    detailDto.setId(dtl.getId());
                    detailDto.setItem_description(dtl.getItem_description());

                    List<AppFileDto> specFileDtos = new ArrayList<>();
                    if (dtl.getSpec_files() != null) {
                        dtl.getSpec_files().forEach(spec -> {
                            AppFileDto specFileDto = new AppFileDto();
                            Path path = Paths.get(spec.getName());
                            specFileDto.setName(path.getFileName().toString());
                            specFileDtos.add(specFileDto);
                        });
                    }
                    detailDto.setSpec_files(specFileDtos);
                    detailDto.setItem_uom(dtl.getItem_uom());
                    detailDto.setItem_qty(dtl.getItem_qty());
                    detailDto.setSupplier(dtl.getSupplier());
                    detailDto.setItem_price(dtl.getItem_price());
                    detailDto.setItem_subtotal(dtl.getItem_subtotal());

                    detailDtos.add(detailDto);
                });
            }
            result.setDetails(detailDtos);
        } else {
            result = new TransferDto();
        }
        return result;
    }

    @Override
    public Long countAll(AppDto<TransferDto> dto) {
        return transferRequestRepo.count();
    }

    @Override
    public Boolean checkApprovalFlow(AppDto<TransferDto> dto) {
        String id = dto.getParams().getId();
        Optional<Transfer> reqOpt = transferRequestRepo.findById(id);
        String nrp = getNrp();

        if (reqOpt.isPresent()) {
            boolean result = false;
            if (reqOpt.get().getTemApprovalDtos() != null) {
                result = reqOpt.get().getTemApprovalDtos().stream()
                        .anyMatch(usr -> usr.getUsers() != null && usr.getUsers().getNrp() != null
                                && usr.getUsers().getNrp().equalsIgnoreCase(nrp));
            }

            if (reqOpt.get().getTempPurchaserApprovalDto() != null
                    && reqOpt.get().getTempPurchaserApprovalDto().getUsers() != null) {
                result = !reqOpt.get().getTempPurchaserApprovalDto().getUsers().getNrp().equalsIgnoreCase(nrp);
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public ApprovalEnum checkStatusApprovalIsPermanent(AppDto<TransferDto> dto) {
        TransferDto params = dto.getParams();
        String id = params.getId();
        String nrp = getNrp();

        Optional<Transfer> reqOpt = transferRequestRepo.findById(id);
        if (reqOpt.isPresent()) {
            ApprovalEnum result = ApprovalEnum.NON_APPROVAL;
            List<ApprovalDto> approvals = new ArrayList<>();
            if (reqOpt.get().getTemApprovalDtos() != null) {
                approvals.addAll(reqOpt.get().getTemApprovalDtos());
            }
            if (reqOpt.get().getTempPurchaserApprovalDto() != null) {
                approvals.add(reqOpt.get().getTempPurchaserApprovalDto());
            }
            int index = 0;
            for (int i = 0; i < approvals.size(); i++) {
                if (approvals.get(i).getUsers() != null && approvals.get(i).getUsers().getNrp() != null
                        && approvals.get(i).getUsers().getNrp().equalsIgnoreCase(nrp)) {
                    index = i;
                }
            }
            if (approvals.stream().anyMatch(data -> data.getUsers() != null && data.getUsers().getNrp() != null
                    && data.getUsers().getNrp().equalsIgnoreCase(nrp))) {
                if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                    if (approvals.stream()
                            .anyMatch(data -> data.getUsers() != null && data.getUsers().getNrp() != null
                                    && data.getUsers().getNrp().equalsIgnoreCase(nrp)
                                    && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                        result = ApprovalEnum.ALREADY_APPROVED;
                    }

                    if (approvals.stream()
                            .anyMatch(data -> data.getUsers() != null && data.getUsers().getNrp() != null
                                    && data.getUsers().getNrp().equalsIgnoreCase(nrp)
                                    && !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
                            && index == (reqOpt.get().getCurrentApprovalStage() != null
                                    ? reqOpt.get().getCurrentApprovalStage()
                                    : 0)) {
                        result = ApprovalEnum.NEED_APPROVAL;
                    }

                }
            }
            return result;
        } else {
            throw new RequisitionException("TransferRequest ID " + id + " not found");
        }
    }

    @Override
    public Resource download(AppDto<TransferDto> dto) {
        String from = dto.getParams().getFrom();
        String filename = dto.getParams().getFilename();

        try {
            Path path = Paths.get(AppFileUtil.getServerPath(), "data", from, filename);
            return new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error downloading file", e);
        }
    }

    private Transfer dtoToEntityAdd(TransferDto dto, String id, Transfer entity) {
        if (id != null) {
            entity.setId(id);
        }
        entity.setTitle(dto.getTitle());
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDue_date());
        entity.setNote(dto.getNote());
        List<TransferDetailDto> details = new ArrayList<>();
        int count = 1;
        if (dto.getDetails() != null) {
            for (TransferDetailDto detail : dto.getDetails()) {
                String detailId = String.format(id + "-" + "%d", count);
                detail.setId(detailId);
                details.add(detail);
                count++;
            }
        }
        entity.setDetails(details);
        entity.setRejectNote(dto.getReject_note());
        entity.setDepartment(getDepartment());
        entity.setCurrentApprovalStage(0);

        if (entity.getTemApprovalDtos() == null && dto.getApprovals() != null) {
            List<ApprovalDto> newApproval = new ArrayList<>();
            for (ApprovalDto approvalDto : dto.getApprovals()) {
                if (Boolean.TRUE.equals(approvalDto.getIsBypassChecked())) {
                    entity.setCurrentApprovalStage(entity.getCurrentApprovalStage() + 1);
                    approvalDto.setIsBypassPermanent(true);
                }
                newApproval.add(approvalDto);
            }
            entity.setTemApprovalDtos(newApproval);
        }

        if (entity.getTempPurchaserApprovalDto() == null) {
            entity.setTempPurchaserApprovalDto(dto.getPurchaser());
        }

        if (entity.getDepartment() != null && entity.getDepartment().getApprovals() != null) {
            int totalApprover = entity.getDepartment().getApprovals().size();
            entity.setTotalApprovalStage(totalApprover + 1);
        }

        if (dto.getStatus() != null && dto.getStatus().equalsIgnoreCase(RequisitionEnum.SEND.getValue())) {
            entity.setStatus(RequisitionEnum.WAITING_APPROVAL.getValue());
        } else {
            entity.setStatus(dto.getStatus());
        }

        return entity;
    }

    private Transfer dtoToEntityUpdate(String nrp, TransferDto dto, Transfer entity) {
        entity.setTitle(dto.getTitle());
        entity.setNote(dto.getNote());
        entity.setDueDate(dto.getDue_date());

        if (entity.getDetails() != null) {
            List<TransferDetailDto> existingDetails = entity.getDetails();
            existingDetails.forEach(dtl -> {
                if (dtl.getSpec_files() != null) {
                    dtl.getSpec_files().forEach(file -> {
                        try {
                            Files.deleteIfExists(Paths.get(file.getName()));
                        } catch (IOException e) {
                            log.error(e.getMessage(), e.getCause());
                        }
                    });
                }
            });
            existingDetails.clear();
        }

        List<TransferDetailDto> details = new ArrayList<>();
        if (dto.getDetails() != null) {
            dto.getDetails().forEach(dtl -> {
                TransferDetailDto detail = dtl;
                detail.setItem_name(dtl.getItem_name());
                detail.setItem_number(dtl.getItem_number());
                detail.setItem_description(dtl.getItem_description());

                if (dtl.getSpec_files() != null) {
                    List<AppFileDto> specFiles = dtl.getSpec_files();
                    specFiles.forEach(file -> {
                        try {
                            Path fileFromBase64 = AppFileUtil.createFileFromBase64(dtl.getId(), "requisitions", file);
                            log.info("File " + fileFromBase64 + " is created");
                        } catch (IOException e) {
                            log.error(e.getMessage(), e.getCause());
                        }
                    });
                    detail.setSpec_files(specFiles.stream().map(data -> {
                        AppFileDto fileDto = new AppFileDto();
                        fileDto.setName(data.getName());
                        return fileDto;
                    }).toList());
                }
                detail.setItem_uom(dtl.getItem_uom());
                detail.setItem_qty(dtl.getItem_qty());
                detail.setSupplier(dtl.getSupplier());
                detail.setItem_price(dtl.getItem_price());
                detail.setItem_subtotal(dtl.getItem_subtotal());

                details.add(detail);
            });
        }
        entity.setDetails(details);

        if (entity.getDepartment() != null && entity.getDepartment().getApprovals() != null) {
            entity.setTotalApprovalStage(entity.getDepartment().getApprovals().size() + 1);
        }

        Optional<Users> userOpt = (nrp != null) ? userRepo.findById(nrp) : Optional.empty();
        if (userOpt.isPresent()) {
            Users users = userOpt.get();

            if (entity.getTemApprovalDtos() != null) {
                Optional<ApprovalDto> currentApproval = entity.getTemApprovalDtos().stream()
                        .filter(data -> data.getUsers() != null && data.getUsers().getNrp() != null
                                && data.getUsers().getNrp().equalsIgnoreCase(users.getNrp()))
                        .findFirst();
                if (currentApproval.isPresent()) {
                    if (entity.getCurrentApprovalStage() == null) {
                        entity.setCurrentApprovalStage(1);
                    } else {
                        entity.setCurrentApprovalStage(entity.getCurrentApprovalStage() + 1);
                    }

                    List<ApprovalDto> entityApproval = entity.getTemApprovalDtos().stream().map(data -> {
                        if (data.getUsers() != null && data.getUsers().getNrp() != null
                                && data.getUsers().getNrp().equalsIgnoreCase(nrp)) {
                            ApprovalDto newData = data;
                            if (Boolean.TRUE.equals(data.getIsApprovalChecked())) {
                                newData.setIsApprovalPermanent(true);
                            }

                            if (Boolean.TRUE.equals(data.getIsBypassChecked())) {
                                newData.setIsBypassPermanent(true);
                                entity.setCurrentApprovalStage(entity.getCurrentApprovalStage() + 1);
                            }
                            return newData;
                        } else {
                            return data;
                        }
                    }).toList();
                    entity.setTemApprovalDtos(entityApproval);
                    String status = null;
                    if (dto.getStatus() != null && "APPROVED".equalsIgnoreCase(dto.getStatus())) {
                        status = RequisitionEnum.WAITING_APPROVAL.getValue();
                    } else if (dto.getStatus() != null && "REJECT".equalsIgnoreCase(dto.getStatus())) {
                        entity.setRejectNote(dto.getReject_note());
                        status = RequisitionEnum.REJECT.getValue();
                    } else {
                        status = dto.getStatus();
                    }
                    entity.setStatus(status);
                    entity.setLastApproved(userOpt.get().getFullname());
                }
            }

            if (entity.getDepartment() != null && entity.getDepartment().getDeptPurchaser() != null
                    && entity.getDepartment().getDeptPurchaser().getNrp().equalsIgnoreCase(users.getNrp())) {
                if (entity.getCurrentApprovalStage() == null) {
                    entity.setCurrentApprovalStage(1);
                } else {
                    entity.setCurrentApprovalStage(entity.getCurrentApprovalStage() + 1);
                }

                ApprovalDto purchaser = entity.getTempPurchaserApprovalDto();
                if (purchaser != null) {
                    if (Boolean.TRUE.equals(purchaser.getIsApprovalChecked())) {
                        purchaser.setIsApprovalPermanent(true);
                    }

                    if (Boolean.TRUE.equals(purchaser.getIsBypassChecked())) {
                        purchaser.setIsBypassPermanent(true);
                    }
                    entity.setTempPurchaserApprovalDto(purchaser);
                }
                String status = null;
                if (dto.getStatus() != null && "APPROVED".equalsIgnoreCase(dto.getStatus())) {
                    status = "CLOSED";
                    entity.setLastApproved(userOpt.get().getFullname());

                    for (TransferDetailDto dtl : details) {
                        Optional<Inventory> inventoryOpt = inventoryRepo.findById(dtl.getItem_number());

                        if (inventoryOpt.isPresent()) {
                            Inventory inv = inventoryOpt.get();
                            BigInteger qty = inv.getQty() != null ? inv.getQty() : BigInteger.ZERO;
                            BigInteger dtlQty = dtl.getItem_qty() != null ? dtl.getItem_qty() : BigInteger.ZERO;
                            inv.setQty(qty.subtract(dtlQty));
                            inventoryRepo.save(inv);

                            if (inv.getQty().compareTo(BigInteger.ZERO) == 0) {
                                inventoryRepo.delete(inv);
                            }

                            if (inv.getQty().compareTo(BigInteger.ZERO) < 0) {
                                throw new TransferException(
                                        "Inconsistent data. This item causes minus qty in the warehouse");
                            }
                        } else {
                            throw new TransferException("Data from Warehouse not found");
                        }
                    }
                } else if (dto.getStatus() != null && "REJECT".equalsIgnoreCase(dto.getStatus())) {
                    entity.setRejectNote(dto.getReject_note());
                    status = RequisitionEnum.REJECT.getValue();
                    entity.setLastApproved(userOpt.get().getFullname());
                } else {
                    status = dto.getStatus();
                }
                entity.setStatus(status);
            }
        } else {
            entity.setStatus(dto.getStatus());
        }

        return entity;
    }

    private TransferDto entityToDto(Transfer entity) {
        TransferDto dto = new TransferDto();
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

        if (entity.getDepartment() != null) {
            DepartmentDto deptDto = new DepartmentDto();
            deptDto.setDept_number(entity.getDepartment().getDeptNumber());
            deptDto.setDept_name(entity.getDepartment().getDeptName());
            dto.setDepartment(deptDto);
        }

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

    private String getNrp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        Optional<Users> user = userRepo.findByUsernameIgnoreCase(authentication.getName());
        return user.map(u -> u.getNrp()).orElse(null);
    }

    private Department getDepartment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        Optional<Users> user = userRepo.findByUsernameIgnoreCase(authentication.getName());
        return user.map(u -> u.getDepartment()).orElse(null);
    }

}
