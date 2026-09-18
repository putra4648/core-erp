package id.putra.core_erp.purchase_requisition.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.purchase_requisition.enums.ApprovalEnum;
import id.putra.core_erp.purchase_requisition.exception.RFQException;
import id.putra.core_erp.purchase_requisition.exception.RequisitionException;
import id.putra.core_erp.purchase_requisition.model.Approval;
import id.putra.core_erp.purchase_requisition.model.RFQ;
import id.putra.core_erp.purchase_requisition.repository.RFQRepo;
import id.putra.core_erp.purchase_requisition.service.RFQService;
import id.putra.core_erp.shared.entity.Supplier;
import id.putra.core_erp.shared.util.AppFileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class RFQServiceImpl implements RFQService {

    private final RFQRepo rfqRepository;

    public RFQServiceImpl(RFQRepo rfqRepository) {
        this.rfqRepository = rfqRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(RFQ dto) {
        rfqRepository.persist(dto);
    }

    @Override
    public List<RFQ> findAll(RFQ params) {
        return rfqRepository.find("id like ?1", params.getId()).list();
    }

    @Override
    public RFQ getById(Long id) {
        return rfqRepository.findByIdOptional(id).get();
    }

    @Override
    public Boolean checkApprovalFlow(RFQ dto) {
        Optional<RFQ> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
        if (rfqOpt.isPresent()) {
            // Optional<Users> userOpt = userRepo
            // .findByUsernameIgnoreCase(authentication != null ? authentication.getName() :
            // "");
            // String userNrp = userOpt.map(u -> u.getNrp()).orElse("");
            // if (rfqOpt.get().getTemApprovals() != null) {
            // return rfqOpt.get().getTemApprovals().stream()
            // .anyMatch(aprv -> aprv.getUsers() != null && aprv.getUsers().getNrp() != null
            // && aprv.getUsers().getNrp().equalsIgnoreCase(userNrp));
            // }
            return false;
        } else {
            throw new RFQException("RFQ " + dto.getId() + " not found");
        }
    }

    @Override
    public ApprovalEnum checkStatusApprovalIsPermanent(RFQ dto) {
        String nrp = "";

        Optional<RFQ> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
        if (rfqOpt.isPresent()) {
            ApprovalEnum result = ApprovalEnum.NON_APPROVAL;
            List<Approval> approvals = new ArrayList<>();
            rfqOpt.ifPresent(data -> {
                if (data.getTemApprovals() != null) {
                    approvals.addAll(data.getTemApprovals());
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
            throw new RequisitionException("RFQ " + dto.getId() + " not found");
        }
    }

    @Override
    public byte[] download(RFQ dto) throws Exception{

        Optional<RFQ> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
        if (rfqOpt.isPresent()) {
            try {
                Supplier supplier = rfqOpt.get().getSuppliers().stream()
                        .filter(data -> data.getAccNo() != null)
                        .findFirst().orElse(null);
                if (supplier != null) {
                    Path path = Paths.get(AppFileUtil.getServerPath(), "data", "rfq",
                            supplier.getFilename());
                    return Files.readAllBytes(path);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error reading file", e);
            }
        } else {
            throw new RFQException("File ID " + dto.getId() + " not found");
        }
    }

    @Override
    public void update(RFQ entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
