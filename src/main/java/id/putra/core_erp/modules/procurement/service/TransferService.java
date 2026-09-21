package id.putra.core_erp.modules.procurement.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.enums.ApprovalEnum;
import id.putra.core_erp.exceptions.ModuleException;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.modules.procurement.repository.InventoryRepo;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import id.putra.core_erp.service.BaseService;
import id.putra.core_erp.util.AppFileUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferService implements BaseService<Transfer> {

    private final InventoryRepo inventoryRepo;
    private final TransferRequestRepo transferRequestRepo;

    public TransferService(InventoryRepo inventoryRepo, TransferRequestRepo transferRequestRepo) {
        this.inventoryRepo = inventoryRepo;
        this.transferRequestRepo = transferRequestRepo;
    }

    public void add(Transfer dto) {
        transferRequestRepo.persist(dto);
    }

    public Transfer save(Transfer entity) {
        transferRequestRepo.persist(entity);
        return entity;
    }

    public List<Transfer> findAll(Transfer dto) {
        String status = (dto != null && dto != null && dto.getStatus() != null
                && !dto.getStatus().isBlank())
                        ? dto.getStatus()
                        : "%%";
        String id = (dto != null && dto != null && dto.getId() != null)
                ? dto.getId().toString()
                : "%%";

        return transferRequestRepo.findByStatusLikeAndIdLike(status, id);
    }

    public Iterable<Transfer> findAll() {
        return transferRequestRepo.findAll().list();
    }

    public Transfer findById(Long id) {
        return transferRequestRepo.findById(id);
    }

    public Transfer getById(Transfer dto) {
        return transferRequestRepo.findById(dto.getId());
    }

    public Boolean checkApprovalFlow(Transfer dto) {
        Optional<Transfer> reqOpt = transferRequestRepo.findByIdOptional(dto.getId());
        String nrp = "";

        if (reqOpt.isPresent()) {
            boolean result = false;
            if (reqOpt.get().getTemApprovals() != null) {
                result = reqOpt.get().getTemApprovals().stream()
                        .anyMatch(usr -> usr.getUsers() != null && usr.getUsers() != null
                                && usr.getUsers().equalsIgnoreCase(nrp));
            }

            if (reqOpt.get().getTempPurchaserApproval() != null
                    && reqOpt.get().getTempPurchaserApproval().getUsers() != null) {
                result = !reqOpt.get().getTempPurchaserApproval().getUsers()
                        .equalsIgnoreCase(nrp);
            }
            return result;
        } else {
            return false;
        }
    }

    public ApprovalEnum checkStatusApprovalIsPermanent(Transfer dto) {
        Transfer params = dto;
        String nrp = "";

        Optional<Transfer> reqOpt = transferRequestRepo.findByIdOptional(params.getId());
        if (reqOpt.isPresent()) {
            ApprovalEnum result = ApprovalEnum.NON_APPROVAL;
            List<Approval> approvals = new ArrayList<>();
            if (reqOpt.get().getTemApprovals() != null) {
                approvals.addAll(reqOpt.get().getTemApprovals());
            }
            if (reqOpt.get().getTempPurchaserApproval() != null) {
                approvals.add(reqOpt.get().getTempPurchaserApproval());
            }
            int index = 0;
            for (int i = 0; i < approvals.size(); i++) {
                if (approvals.get(i).getUsers() != null && approvals.get(i).getUsers() != null
                        && approvals.get(i).getUsers().equalsIgnoreCase(nrp)) {
                    index = i;
                }
            }
            if (approvals.stream().anyMatch(data -> data.getUsers() != null && data.getUsers() != null
                    && data.getUsers().equalsIgnoreCase(nrp))) {
                if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                    if (approvals.stream()
                            .anyMatch(data -> data.getUsers() != null && data.getUsers() != null
                                    && data.getUsers().equalsIgnoreCase(nrp)
                                    && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                        result = ApprovalEnum.ALREADY_APPROVED;
                    }

                    if (approvals.stream()
                            .anyMatch(data -> data.getUsers() != null && data.getUsers() != null
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
            throw new ModuleException("TransferRequest ID " + params.getId() + " not found");
        }
    }

    public byte[] download(Transfer dto) throws Exception {
        Path path = Paths.get(AppFileUtil.getServerPath(), "data", dto.getFilename());
        return Files.readAllBytes(path);

    }

    public void update(Transfer dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
