package id.putra.core_erp.purchase_requisition.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.exception.RequisitionException;
import id.putra.core_erp.purchase_requisition.model.Approval;
import id.putra.core_erp.purchase_requisition.model.Requisition;
import id.putra.core_erp.purchase_requisition.repository.BudgetDetailRepo;
import id.putra.core_erp.purchase_requisition.repository.DepartmentRepo;
import id.putra.core_erp.purchase_requisition.repository.RequisitionRepo;
import id.putra.core_erp.purchase_requisition.repository.TransferRequestRepo;
import id.putra.core_erp.purchase_requisition.service.RequisitionService;
import id.putra.core_erp.shared.util.AppFileUtil;
import io.quarkus.oidc.IdToken;
import io.vertx.core.file.OpenOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.file.AsyncFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
            DepartmentRepo departmentRepo, BudgetDetailRepo budgetDetailRepo, Vertx vertx, JsonWebToken principal) {
        this.transferRequestRepo = transferRequestRepo;
        this.requisitionRepo = requisitionRepo;
        this.departmentRepo = departmentRepo;
        this.budgetDetailRepo = budgetDetailRepo;
        this.vertx = vertx;
        this.principal = principal;
    }

    @Inject
    @IdToken
    private JsonWebToken principal;

    @Override
    public Boolean checkApprovalFlow(Requisition dto) {
        Optional<Requisition> reqOpt = requisitionRepo.findByIdOptional(dto.getId());
        String nrp = principal.getSubject();

        if (reqOpt.isPresent()) {
            boolean result = false;
            if (reqOpt.get().getApprovals() != null) {
                result = reqOpt.get().getApprovals().stream()
                        .anyMatch(usr -> usr.getUsers() != null && usr.getUsers().getNrp() != null
                                && usr.getUsers().getNrp().equalsIgnoreCase(nrp));
            }

            if (reqOpt.get().getApproval() != null
                    && reqOpt.get().getApproval().getUsers() != null) {
                result = !reqOpt.get().getApproval().getUsers().getNrp().equalsIgnoreCase(nrp);
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public ApprovalEnum checkStatusApprovalIsPermanent(Requisition dto) {
        String nrp = principal.getSubject();

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
            throw new RequisitionException("Requisition ID " + dto.getId() + " not found");
        }
    }

    @Override
    public AsyncFile download(Requisition dto) {
        String filename = "";

        OpenOptions options = new OpenOptions().setCreate(false).setWrite(false);
        Path path = Paths.get(AppFileUtil.getServerPath(), "data", "requisitions", filename);
        return vertx.fileSystem().openAndAwait(path.getFileName().toString(), options);

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
