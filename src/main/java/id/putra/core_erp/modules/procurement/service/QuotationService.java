package id.putra.core_erp.modules.procurement.service;

import id.putra.core_erp.shared.service.BaseService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.modules.approval.enums.ApprovalEnum;
import id.putra.core_erp.modules.procurement.model.Approval;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.repository.QuotationRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.shared.exceptions.ModuleException;
import id.putra.core_erp.shared.util.AppFileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class QuotationService implements BaseService<Quotation> {

    private final QuotationRepo rfqRepository;

    public QuotationService(QuotationRepo rfqRepository) {
        this.rfqRepository = rfqRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;
    public void add(Quotation dto) {
        rfqRepository.persist(dto);
    }
    public List<Quotation> findAll(Quotation params) {
        return rfqRepository.find("id like ?1", params.getId()).list();
    }
    public Quotation save(Quotation entity) {
        rfqRepository.persist(entity);
        return entity;
    }
    public Quotation findById(Long id) {
        return rfqRepository.findById(id);
    }
    public Iterable<Quotation> findAll() {
        return rfqRepository.findAll().list();
    }
    public Quotation getById(Long id) {
        return rfqRepository.findByIdOptional(id).get();
    }
    public Boolean checkApprovalFlow(Quotation dto) {
        Optional<Quotation> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
        if (rfqOpt.isPresent()) {
            // Optional<Users> userOpt = userRepo
            // .findByUsernameIgnoreCase(authentication != null ? authentication.getName() :
            // "");
            // String userNrp = userOpt.map(u -> u).orElse("");
            // if (rfqOpt.get().getTemApprovals() != null) {
            // return rfqOpt.get().getTemApprovals().stream()
            // .anyMatch(aprv -> aprv.getUsers() != null && aprv.getUsers() != null
            // && aprv.getUsers().equalsIgnoreCase(userNrp));
            // }
            return false;
        } else {
            throw new ModuleException("RFQ " + dto.getId() + " not found");
        }
    }
    public ApprovalEnum checkStatusApprovalIsPermanent(Quotation dto) {
        String nrp = "";

        Optional<Quotation> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
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
                            && nrp.equalsIgnoreCase(approvals.get(i).getUsers())) {
                        index = i;
                    }
                }
                if (approvals.stream().anyMatch(
                        data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers()))) {

                    if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
                        if (approvals.stream().anyMatch(
                                data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
                                        && Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
                            result = ApprovalEnum.ALREADY_APPROVED;
                        }

                        if (approvals.stream().anyMatch(
                                data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
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
            throw new ModuleException("RFQ " + dto.getId() + " not found");
        }
    }
    public byte[] download(Quotation dto) throws Exception{

        Optional<Quotation> rfqOpt = rfqRepository.findByIdOptional(dto.getId());
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
            throw new ModuleException("File ID " + dto.getId() + " not found");
        }
    }
    public void update(Quotation entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
