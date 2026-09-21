package id.putra.core_erp.modules.procurement.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.budget.repository.BudgetDetailRepo;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.model.Approval;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import id.putra.core_erp.modules.procurement.service.RequisitionService;
import id.putra.core_erp.shared.exceptions.ModuleException;
import id.putra.core_erp.shared.util.AppFileUtil;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { Exception.class })
public class RequisitionServiceImpl implements RequisitionService {

    private final TransferRequestRepo transferRequestRepo;
    private final RequisitionRepo requisitionRepo;
    private final DepartmentRepo departmentRepo;
    private final BudgetDetailRepo budgetDetailRepo;
    private final Vertx vertx;

    public RequisitionServiceImpl(TransferRequestRepo transferRequestRepo, RequisitionRepo requisitionRepo,
            DepartmentRepo departmentRepo, BudgetDetailRepo budgetDetailRepo, Vertx vertx) {
        this.transferRequestRepo = transferRequestRepo;
        this.requisitionRepo = requisitionRepo;
        this.departmentRepo = departmentRepo;
        this.budgetDetailRepo = budgetDetailRepo;
        this.vertx = vertx;
    }

    @Override
    public Boolean checkApprovalFlow(Requisition dto) {
        Optional<Requisition> reqOpt = requisitionRepo.findByIdOptional(dto.getId());
        String nrp = "";

        if (reqOpt.isPresent()) {
            boolean result = false;
            if (reqOpt.get().getApprovals() != null) {
                result = reqOpt.get().getApprovals().stream()
                        .anyMatch(usr -> usr.getUsers() != null && usr.getUsers() != null
                                && usr.getUsers().equalsIgnoreCase(nrp));
            }

            if (reqOpt.get().getApproval() != null
                    && reqOpt.get().getApproval().getUsers() != null) {
                result = !reqOpt.get().getApproval().getUsers().equalsIgnoreCase(nrp);
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public ApprovalEnum checkStatusApprovalIsPermanent(Requisition dto) {
        String nrp = "";

        Optional<Requisition> reqOpt = requisitionRepo.findByIdOptional(dto.getId());
        if (reqOpt.isPresent()) {
            ApprovalEnum result = ApprovalEnum.NON_APPROVAL;
            List<Approval> approvals = new ArrayList<>();
            if (reqOpt.get().getApprovals() != null) {
                approvals.addAll(reqOpt.get().getApprovals());
            }
            if (reqOpt.get().getApproval() != null) {
                approvals.add(reqOpt.get().getApproval());
            }
            int index = 0;
            for (int i = 0; i < approvals.size(); i++) {
                if (approvals.get(i).getUsers() != null && approvals.get(i).getUsers() != null
                        && approvals.get(i).getUsers().equalsIgnoreCase(nrp)) {
                    index = i;
                }
            }
            if (approvals.stream().anyMatch(
                    data -> data.getUsers() != null && data.getUsers() != null
                            && data.getUsers().equalsIgnoreCase(nrp))) {
                if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                    if (approvals.stream().anyMatch(
                            data -> data.getUsers() != null && data.getUsers() != null
                                    && data.getUsers().equalsIgnoreCase(nrp)
                                    && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                        result = ApprovalEnum.ALREADY_APPROVED;
                    }

                    if (approvals.stream().anyMatch(
                            data -> data.getUsers() != null && data.getUsers() != null
                                    && data.getUsers().equalsIgnoreCase(nrp)
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
            throw new ModuleException("Requisition ID " + dto.getId() + " not found");
        }
    }

    @Override
    public byte[] download(Requisition dto) throws IOException {
        String filename = "";

        Path path = Paths.get(AppFileUtil.getServerPath(), "data", "requisitions", filename);
        return Files.readAllBytes(path);

    }

    @Override
    public void add(Requisition entity) {
        requisitionRepo.persist(entity);
    }

    @Override
    public void update(Requisition entity) {
        requisitionRepo.persist(entity);
    }

    @Override
    public void delete(Requisition entity) {
        requisitionRepo.delete(entity);
    }

    @Override
    public Requisition findById(Long id) {
        return requisitionRepo.findById(id);
    }

    @Override
    public List<Requisition> findAll() {
        return requisitionRepo.findAll().list();
    }

}
