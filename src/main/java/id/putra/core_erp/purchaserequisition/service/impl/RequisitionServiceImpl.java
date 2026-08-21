package id.putra.core_erp.purchaserequisition.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import id.putra.core_erp.purchaserequisition.constant.MessageConstant;
import id.putra.core_erp.purchaserequisition.dto.AppDto;
import id.putra.core_erp.purchaserequisition.dto.AppFileDto;
import id.putra.core_erp.purchaserequisition.dto.ApprovalDto;
import id.putra.core_erp.purchaserequisition.dto.BudgetTypeDto;
import id.putra.core_erp.purchaserequisition.dto.DepartmentDto;
import id.putra.core_erp.purchaserequisition.dto.RequisitionDetailDto;
import id.putra.core_erp.purchaserequisition.dto.RequisitionDto;
import id.putra.core_erp.purchaserequisition.dto.TransferDetailDto;
import id.putra.core_erp.purchaserequisition.enums.ApprovalEnum;
import id.putra.core_erp.purchaserequisition.enums.RequisitionEnum;
import id.putra.core_erp.purchaserequisition.exception.BudgetException;
import id.putra.core_erp.purchaserequisition.exception.RequisitionException;
import id.putra.core_erp.purchaserequisition.model.BudgetDetail;
import id.putra.core_erp.purchaserequisition.model.Department;
import id.putra.core_erp.purchaserequisition.model.Requisition;
import id.putra.core_erp.purchaserequisition.model.RequisitionDetail;
import id.putra.core_erp.purchaserequisition.model.Users;
import id.putra.core_erp.purchaserequisition.repository.BudgetDetailRepo;
import id.putra.core_erp.purchaserequisition.repository.DepartmentRepo;
import id.putra.core_erp.purchaserequisition.repository.RequisitionRepo;
import id.putra.core_erp.purchaserequisition.repository.TransferRequestRepo;
import id.putra.core_erp.purchaserequisition.repository.UserRepo;
import id.putra.core_erp.purchaserequisition.service.RequisitionService;
import id.putra.core_erp.purchaserequisition.util.AppFileUtil;
import id.putra.core_erp.purchaserequisition.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("requisitionService")
@Transactional(rollbackFor = { ParseException.class, RequisitionException.class,
        BudgetException.class })
@RequiredArgsConstructor
@Slf4j
public class RequisitionServiceImpl implements RequisitionService {

    private final TransferRequestRepo transferRequestRepo;
    private final RequisitionRepo requisitionRepo;
    private final DepartmentRepo departmentRepo;
    private final BudgetDetailRepo budgetDetailRepo;
    private final UserRepo userRepo;

    @Override
    public String handleData(AppDto<RequisitionDto> dto) {
        RequisitionDto params = dto.getParams();
        String id = params.getId();

        Optional<Requisition> reqOpt = (id != null && !id.isBlank()) ? requisitionRepo.findById(id) : Optional.empty();

        if (reqOpt.isEmpty()) {
            Long count = requisitionRepo.count();
            Requisition entity = new Requisition();
            String generatedID = AppUtil.generatedIdFromZeros("FTS", count);
            dtoToEntityAdd(generatedID, params, entity);
            requisitionRepo.save(entity);
            return MessageConstant.SUCCESS;
        } else {
            Requisition entity = reqOpt.get();
            dtoToEntityUpdate(params, entity);
            requisitionRepo.save(entity);
            return MessageConstant.UPDATE;
        }
    }

    @Override
    public List<RequisitionDto> getAll(AppDto<RequisitionDto> dto) {
        List<RequisitionDto> list = new ArrayList<>();
        requisitionRepo.findAll().forEach(r -> list.add(entityToDto(r)));
        return list;
    }

    @Override
    public RequisitionDto getSingleData(AppDto<RequisitionDto> dto) {
        RequisitionDto result = null;
        RequisitionDto params = dto.getParams();
        String id = params.getId();

        Optional<Requisition> reqOpt = requisitionRepo.findById(id);

        if (reqOpt.isPresent()) {
            result = new RequisitionDto();
            Requisition req = reqOpt.get();

            result.setId(req.getId());
            result.setTitle(req.getTitle());
            result.setNote(req.getNote());
            result.setDue_date(req.getDueDate());
            result.setBudget_detail_id(req.getBudgetDetailId());
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

            List<RequisitionDetailDto> detailDtos = new ArrayList<>();
            if (req.getDetails() != null) {
                req.getDetails().forEach(dtl -> {
                    RequisitionDetailDto detailDto = new RequisitionDetailDto();
                    detailDto.setName(dtl.getName());
                    detailDto.setItem_number(dtl.getItemNumber());
                    detailDto.setId(dtl.getId());
                    detailDto.setDescription(dtl.getDescription());

                    List<AppFileDto> specFileDtos = new ArrayList<>();
                    if (dtl.getSpecFiles() != null) {
                        dtl.getSpecFiles().forEach(spec -> {
                            AppFileDto specFileDto = new AppFileDto();
                            Path path = Paths.get(spec);
                            specFileDto.setName(path.getFileName().toString());
                            specFileDtos.add(specFileDto);
                        });
                    }
                    detailDto.setSpec_files(specFileDtos);
                    detailDto.setUom(dtl.getUom());
                    detailDto.setQty(dtl.getQty());
                    detailDto.setSupplier_id(dtl.getSupplier());
                    detailDto.setUnit_price(dtl.getUnitPrice());
                    detailDto.setSubtotal(dtl.getSubTotal());

                    detailDtos.add(detailDto);
                });
            }
            result.setDetails(detailDtos);
        } else {
            result = new RequisitionDto();
        }
        return result;
    }

    @Override
    public List<RequisitionDto> getPaginationData(AppDto<RequisitionDto> dto) {
        List<RequisitionDto> reqDtos = new ArrayList<>();
        RequisitionEnum requisitionEnum;

        RequisitionDto params = dto.getParams();
        List<String> deptIds = params.getDeptIds() != null ? params.getDeptIds() : new ArrayList<>();
        String status = params.getStatus() != null ? params.getStatus() : "";
        String search = params.getSearch() != null ? params.getSearch() : "";

        String nrp = getNrp();

        if (deptIds.contains("SUP03")) {
            if (status.isBlank() || status.isEmpty()) {
                requisitionEnum = RequisitionEnum.ALL;
            } else {
                try {
                    requisitionEnum = RequisitionEnum.valueOf(status);
                } catch (Exception e) {
                    requisitionEnum = RequisitionEnum.ALL;
                }
            }

            String keyStatus = requisitionEnum == RequisitionEnum.ALL ? "" : requisitionEnum.getValue();

            List<Requisition> reqs = requisitionRepo
                    .getTitleContainingIgnoreCaseOrStatusAndPurchaser(
                            search, keyStatus, nrp);

            reqs.forEach(req -> reqDtos.add(entityToDto(req)));
        } else {
            if (status.isBlank() || status.isEmpty()) {
                requisitionEnum = RequisitionEnum.ALL;
            } else {
                try {
                    requisitionEnum = RequisitionEnum.valueOf(status);
                } catch (Exception e) {
                    requisitionEnum = RequisitionEnum.ALL;
                }
            }

            String keyStatus = requisitionEnum == RequisitionEnum.ALL ? "" : requisitionEnum.getValue();

            List<Requisition> reqs = requisitionRepo
                    .getTitleContainingIgnoreCaseOrStatusAndDepartmentIn(
                            search, keyStatus, deptIds);

            reqs.forEach(req -> reqDtos.add(entityToDto(req)));
        }
        return reqDtos;
    }

    @Override
    public Long countAll(AppDto<RequisitionDto> dto) {
        RequisitionDto params = dto.getParams();
        RequisitionEnum requisitionEnum;

        List<String> deptIds = params.getDeptIds() != null ? params.getDeptIds() : new ArrayList<>();
        String status = params.getStatus() != null ? params.getStatus() : "";
        String search = params.getSearch() != null ? params.getSearch() : "";
        String nrp = getNrp();

        if (status.isBlank() || status.isEmpty()) {
            requisitionEnum = RequisitionEnum.ALL;
        } else {
            try {
                requisitionEnum = RequisitionEnum.valueOf(status);
            } catch (Exception e) {
                requisitionEnum = RequisitionEnum.ALL;
            }
        }

        String keyStatus = requisitionEnum == RequisitionEnum.ALL ? "" : requisitionEnum.getValue();

        if (deptIds.contains("SUP03")) {
            return requisitionRepo.countByTitleContainingIgnoreCaseOrStatusAndPurchaserNrp(search, keyStatus, nrp);
        } else {
            return requisitionRepo.countByTitleContainingIgnoreCaseOrStatusAndDepartmentIn(search, keyStatus, deptIds);
        }
    }

    @Override
    public Boolean checkApprovalFlow(AppDto<RequisitionDto> dto) {
        String id = dto.getParams().getId();
        Optional<Requisition> reqOpt = requisitionRepo.findById(id);
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
    public ApprovalEnum checkStatusApprovalIsPermanent(AppDto<RequisitionDto> dto) {
        RequisitionDto params = dto.getParams();
        String id = params.getId();
        String nrp = getNrp();

        Optional<Requisition> reqOpt = requisitionRepo.findById(id);
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
            if (approvals.stream().anyMatch(
                    data -> data.getUsers() != null && data.getUsers().getNrp() != null
                            && data.getUsers().getNrp().equalsIgnoreCase(nrp))) {
                if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                    if (approvals.stream().anyMatch(
                            data -> data.getUsers() != null && data.getUsers().getNrp() != null
                                    && data.getUsers().getNrp().equalsIgnoreCase(nrp)
                                    && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                        result = ApprovalEnum.ALREADY_APPROVED;
                    }

                    if (approvals.stream().anyMatch(
                            data -> data.getUsers() != null && data.getUsers().getNrp() != null
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
            throw new RequisitionException("Requisition ID " + id + " not found");
        }
    }

    @Override
    public Resource download(AppDto<RequisitionDto> dto) {
        String filename = dto.getParams().getFilename();

        try {
            Path path = Paths.get(AppFileUtil.getServerPath(), "data", "requisitions", filename);
            return new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (IOException e) {
            log.error("Error reading file {}: {}", filename, e.getMessage());
            throw new RuntimeException("Error reading file " + filename, e);
        }
    }

    private RequisitionDto entityToDto(Requisition req) {
        RequisitionDto dto = new RequisitionDto();
        dto.setId(req.getId());
        dto.setTitle(req.getTitle());
        dto.setNote(req.getNote());
        dto.setDue_date(req.getDueDate());
        dto.setBudget_detail_id(req.getBudgetDetailId());
        dto.setStatus(req.getStatus());
        dto.setReject_note(req.getRejectNote());
        dto.setLast_approved(req.getLastApproved());

        List<String> nextApprovals = new ArrayList<>();

        if (req.getDepartment() != null) {
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setDept_name(req.getDepartment().getDeptName());
            departmentDto.setDept_number(req.getDepartment().getDeptNumber());
            dto.setDepartment(departmentDto);
        }

        if (req.getTemApprovalDtos() != null) {
            dto.setApprovals(req.getTemApprovalDtos());
            nextApprovals.addAll(req.getTemApprovalDtos().stream()
                    .filter(data -> !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
                    .filter(data -> data.getUsers() != null && data.getUsers().getFullname() != null)
                    .map(data -> data.getUsers().getFullname()).toList());
        }

        if (req.getTempPurchaserApprovalDto() != null) {
            dto.setPurchaser(req.getTempPurchaserApprovalDto());
            if (Boolean.FALSE.equals(req.getTempPurchaserApprovalDto().getIsApprovalPermanent())
                    && req.getTempPurchaserApprovalDto().getUsers() != null) {
                nextApprovals.add(req.getTempPurchaserApprovalDto().getUsers().getFullname());
            }
        }

        dto.setNext_approvals(nextApprovals);

        List<RequisitionDetailDto> detailDtos = new ArrayList<>();
        if (req.getDetails() != null) {
            req.getDetails().forEach(dtl -> {
                RequisitionDetailDto detailDto = new RequisitionDetailDto();
                detailDto.setName(dtl.getName());
                detailDto.setItem_number(dtl.getItemNumber());
                detailDto.setId(dtl.getId());
                detailDto.setDescription(dtl.getDescription());

                List<AppFileDto> specFileDtos = new ArrayList<>();
                if (dtl.getSpecFiles() != null) {
                    dtl.getSpecFiles().forEach(spec -> {
                        AppFileDto specFileDto = new AppFileDto();
                        Path path = Paths.get(spec);
                        specFileDto.setName(path.getFileName().toString());
                        specFileDtos.add(specFileDto);
                    });
                }
                detailDto.setSpec_files(specFileDtos);
                detailDto.setUom(dtl.getUom());
                detailDto.setQty(dtl.getQty());
                detailDto.setSupplier_id(dtl.getSupplier());
                detailDto.setUnit_price(dtl.getUnitPrice());
                detailDto.setSubtotal(dtl.getSubTotal());

                detailDtos.add(detailDto);
            });
        }
        dto.setDetails(detailDtos);

        return dto;
    }

    private void dtoToEntityAdd(String id, RequisitionDto dto, Requisition entity) {
        if (getNrp() == null) {
            throw new RequisitionException("NRP login is not exist");
        }

        entity.setId(id);
        if (dto.getDepartment() != null && dto.getDepartment().getDept_number() != null) {
            Optional<Department> deptOpt = departmentRepo.findById(dto.getDepartment().getDept_number());
            deptOpt.ifPresent(entity::setDepartment);
        }
        entity.setTitle(dto.getTitle());
        entity.setNote(dto.getNote());
        entity.setDueDate(dto.getDue_date());
        entity.setBudgetDetailId(dto.getBudget_detail_id());
        entity.setBudgetDate(new Date());

        if (dto.getInvestment_id() != null && !dto.getInvestment_id().isBlank()) {
            entity.setInvestmentId(dto.getInvestment_id());
        }

        if (entity.getDetails() != null) {
            entity.getDetails().clear();
        }

        entity.setCurrentApprovalStage(0);

        if (entity.getTemApprovalDtos() == null && dto.getApprovals() != null) {
            for (ApprovalDto approvalDto : dto.getApprovals()) {
                if (Boolean.TRUE.equals(approvalDto.getIsBypassChecked())) {
                    entity.setCurrentApprovalStage(entity.getCurrentApprovalStage() + 1);
                }
            }
            entity.setTemApprovalDtos(dto.getApprovals());
        }

        if (entity.getTempPurchaserApprovalDto() == null) {
            entity.setTempPurchaserApprovalDto(dto.getPurchaser());
        }

        List<RequisitionDetail> details = new ArrayList<>();
        if (dto.getBudget_detail_id() != null) {
            Optional<BudgetDetail> budgetDetailOpt = budgetDetailRepo.findById(dto.getBudget_detail_id());
            if (budgetDetailOpt.isPresent()) {
                BudgetDetail budgetDetail = budgetDetailOpt.get();
                String month = new SimpleDateFormat("MMM").format(new Date());
                List<BudgetTypeDto> budgetTypes = budgetDetail.getDetails();
                if (dto.getStatus() != null && !dto.getStatus().equalsIgnoreCase("REJECT")
                        && dto.getDetails() != null) {
                    BigInteger total = dto.getDetails().stream()
                            .map(data -> data.getSubtotal() != null ? data.getSubtotal() : BigInteger.ZERO)
                            .reduce(BigInteger::add).orElse(BigInteger.ZERO);
                    if (budgetTypes != null) {
                        for (int i = 0; i < budgetTypes.size(); i++) {
                            if (budgetTypes.get(i).getBudgetDateStr() != null
                                    && budgetTypes.get(i).getBudgetDateStr().equalsIgnoreCase(month)) {
                                BigInteger planVal = budgetTypes.get(i).getPlan() != null ? budgetTypes.get(i).getPlan()
                                        : BigInteger.ZERO;
                                BigInteger reduced = planVal.subtract(total);
                                if (reduced.signum() == -1) {
                                    throw new BudgetException("Budget is minus");
                                } else {
                                    budgetTypes.get(i).setPlan(reduced);
                                }
                            }
                        }
                    }
                }
                budgetDetail.setDetails(budgetTypes);
                budgetDetailRepo.save(budgetDetail);
            }
        }

        if (dto.getRequest_no() != null) {
            for (String reqNo : dto.getRequest_no()) {
                transferRequestRepo.findById(reqNo).ifPresent(transfer -> {
                    List<TransferDetailDto> trfDetails = transfer.getDetails();
                    if (trfDetails != null) {
                        trfDetails.forEach(trf -> {
                            trf.setStatus("PR_CREATED");
                            trf.setItem_pr_no(id);
                        });
                    }
                });
            }
        }

        if (dto.getDetails() != null) {
            dto.getDetails().forEach(dtl -> {
                RequisitionDetail detail = new RequisitionDetail();
                detail.setName(dtl.getName());
                detail.setItemNumber(dtl.getItem_number());
                detail.setDescription(dtl.getDescription());

                List<String> specFilenames = new ArrayList<>();
                List<AppFileDto> specFiles = dtl.getSpec_files();
                if (specFiles != null) {
                    specFiles.forEach(file -> {
                        try {
                            Path fileBase64 = AppFileUtil.createFileFromBase64(dtl.getId(), "requisitions", file);
                            specFilenames.add(fileBase64.toString());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e.getCause());
                        }
                    });
                }
                detail.setSpecFiles(specFilenames);
                detail.setUom(dtl.getUom());
                detail.setQty(dtl.getQty());
                detail.setSupplier(dtl.getSupplier_id());
                detail.setUnitPrice(dtl.getUnit_price());
                detail.setSubTotal(dtl.getSubtotal());

                detail.setRequisition(entity);
                details.add(detail);
            });
        }
        entity.setDetails(details);

        if (entity.getDepartment() != null && entity.getDepartment().getApprovals() != null) {
            int totalApprover = entity.getDepartment().getApprovals().size();
            entity.setTotalApprovalStage(totalApprover);
        }

        if (dto.getStatus() != null && dto.getStatus().equalsIgnoreCase(RequisitionEnum.SEND.getValue())) {
            entity.setStatus(RequisitionEnum.WAITING_APPROVAL.getValue());
        } else {
            entity.setStatus(dto.getStatus());
        }
    }

    private void dtoToEntityUpdate(RequisitionDto dto, Requisition entity) {
        if (dto.getDepartment() != null && dto.getDepartment().getDept_number() != null) {
            Optional<Department> deptOpt = departmentRepo.findById(dto.getDepartment().getDept_number());
            deptOpt.ifPresent(entity::setDepartment);
        }
        entity.setTitle(dto.getTitle());
        entity.setNote(dto.getNote());
        entity.setDueDate(dto.getDue_date());
        entity.setBudgetDetailId(dto.getBudget_detail_id());
        entity.setBudgetDate(new Date());

        if (dto.getInvestment_id() != null && !dto.getInvestment_id().isBlank()) {
            entity.setInvestmentId(dto.getInvestment_id());
        }

        if (entity.getDetails() != null) {
            List<RequisitionDetail> details = entity.getDetails();
            details.forEach(dtl -> {
                if (dtl.getSpecFiles() != null) {
                    dtl.getSpecFiles().forEach(file -> {
                        try {
                            Files.deleteIfExists(Paths.get(file));
                        } catch (IOException e) {
                            log.error(e.getMessage(), e.getCause());
                        }
                    });
                }
            });
            details.clear();
        }

        List<RequisitionDetail> details = new ArrayList<>();
        if (dto.getDetails() != null) {
            dto.getDetails().forEach(dtl -> {
                RequisitionDetail detail = new RequisitionDetail();
                detail.setName(dtl.getName());
                detail.setItemNumber(dtl.getItem_number());
                detail.setDescription(dtl.getDescription());

                if (dtl.getSpec_files() != null) {
                    List<String> specFilenames = new ArrayList<>();
                    List<AppFileDto> specFiles = dtl.getSpec_files();
                    specFiles.forEach(file -> {
                        try {
                            Path fileFromBase64 = AppFileUtil.createFileFromBase64(dtl.getId(), "requisitions", file);
                            specFilenames.add(fileFromBase64.toString());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e.getCause());
                        }
                    });
                    detail.setSpecFiles(specFilenames);
                }
                detail.setUom(dtl.getUom());
                detail.setQty(dtl.getQty());
                detail.setSupplier(dtl.getSupplier_id());
                detail.setUnitPrice(dtl.getUnit_price());
                detail.setSubTotal(dtl.getSubtotal());

                detail.setRequisition(entity);
                details.add(detail);
            });
        }
        entity.setDetails(details);

        String currentNrp = getNrp();
        Optional<Users> userOpt = (currentNrp != null) ? userRepo.findById(currentNrp) : Optional.empty();

        if (entity.getDepartment() != null && entity.getDepartment().getApprovals() != null) {
            entity.setTotalApprovalStage(entity.getDepartment().getApprovals().size() + 1);
        }

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
                                && data.getUsers().getNrp().equalsIgnoreCase(getNrp())) {
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
                    status = RequisitionEnum.PO_CREATED.getValue();
                    entity.setLastApproved(userOpt.get().getFullname());
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
    }

    private String getNrp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        Optional<Users> user = userRepo.findByUsernameIgnoreCase(authentication.getName());
        return user.map(u -> u.getNrp()).orElse(null);
    }

}

