package id.putra.core_erp.modules.procurement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.exceptions.ModuleException;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.repository.QuotationRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class QuotationService implements BaseService<Quotation> {

<<<<<<< HEAD
	@PersistenceContext
	private EntityManager entityManager;

	private final QuotationRepo quotationRepository;

	public QuotationService(QuotationRepo rfqRepository) {
		this.quotationRepository = rfqRepository;
	}
=======
	private final QuotationRepo quotationRepository;

	public QuotationService(QuotationRepo rfqRepository) {
		this.quotationRepository = rfqRepository;
	}

	@PersistenceContext
	private EntityManager entityManager;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	public void add(Quotation dto) {
		quotationRepository.persist(dto);
	}

<<<<<<< HEAD
	public Boolean checkApprovalFlow(Quotation dto) {
		Optional<Quotation> rfqOpt = quotationRepository.findByIdOptional(dto.getId());
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

	public ApprovalStatus checkStatusApprovalIsPermanent(Quotation dto) {
		String nrp = "";

		Optional<Quotation> rfqOpt = quotationRepository.findByIdOptional(dto.getId());
		if (rfqOpt.isPresent()) {
			ApprovalStatus result = ApprovalStatus.NON_APPROVAL;
			List<Approval> approvals = new ArrayList<>();
			rfqOpt.ifPresent(data -> {
				if (data.getTemApprovals() != null) {
					approvals.addAll(data.getTemApprovals());
				}
			});
			if (nrp != null) {
				int index = 0;
				for (int i = 0; i < approvals.size(); i++) {
					if (approvals.get(i).getUsers() != null && nrp.equalsIgnoreCase(approvals.get(i).getUsers())) {
						index = i;
					}
				}
				if (approvals.stream()
						.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers()))) {

					if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
						if (approvals.stream()
								.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
										&& Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
							result = ApprovalStatus.ALREADY_APPROVED;
						}

						if (approvals.stream()
								.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
										&& !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
								&& index == (rfqOpt.get().getCurrentApprovalStage() != null
										? rfqOpt.get().getCurrentApprovalStage()
										: 0)) {
							result = ApprovalStatus.NEED_APPROVAL;
						}

					}
				}
			}

			return result;
		} else {
			throw new ModuleException("RFQ " + dto.getId() + " not found");
		}
	}

	@Override
	public void delete(Long id) {
		quotationRepository.deleteById(id);
	}

	public List<Quotation> findAll(Quotation params) {
		return quotationRepository.find("id like ?1", params.getId()).list();
	}

	@Override
	public List<Quotation> findAll(Quotation quotation, Page p) {
		return quotationRepository.findAll().list();
	}

	@Override
	public Quotation findById(Long id) {
		return quotationRepository.findById(id);
	}

	public Quotation getById(Long id) {
		return quotationRepository.findByIdOptional(id).get();
	}

	@Override
	public void save(Quotation entity) {
		quotationRepository.persist(entity);
	}

	@Override
	public void update(Quotation entity) {
		quotationRepository.persist(entity);
=======
	public List<Quotation> findAll(Quotation params) {
		return quotationRepository.find("CAST(id AS text) like ?1", params.id != null ? params.id.toString() : "")
				.list();
	}

	@Override
	public void save(Quotation entity) {
		quotationRepository.persist(entity);
	}

	@Override
	public Quotation findById(Long id) {
		return quotationRepository.findById(id);
	}

	@Override
	public Iterable<Quotation> findAll(Quotation quotation, Page p) {
		return quotationRepository.findAll().list();
	}

	public Quotation getById(Long id) {
		return quotationRepository.findByIdOptional(id).get();
	}

	public Boolean checkApprovalFlow(Quotation dto) {
		Optional<Quotation> rfqOpt = quotationRepository.findByIdOptional(dto.id);
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
			throw new ModuleException("RFQ " + dto.id + " not found");
		}
	}

	public ApprovalStatus checkStatusApprovalIsPermanent(Quotation dto) {
		String nrp = "";

		Optional<Quotation> rfqOpt = quotationRepository.findByIdOptional(dto.id);
		if (rfqOpt.isPresent()) {
			ApprovalStatus result = ApprovalStatus.NON_APPROVAL;
			List<Approval> approvals = new ArrayList<>();
			rfqOpt.ifPresent(data -> {
				if (data.getTemApprovals() != null) {
					approvals.addAll(data.getTemApprovals());
				}
			});
			if (nrp != null) {
				int index = 0;
				for (int i = 0; i < approvals.size(); i++) {
					if (approvals.get(i).getUsers() != null && nrp.equalsIgnoreCase(approvals.get(i).getUsers())) {
						index = i;
					}
				}
				if (approvals.stream()
						.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers()))) {

					if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
						if (approvals.stream()
								.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
										&& Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
							result = ApprovalStatus.ALREADY_APPROVED;
						}

						if (approvals.stream()
								.anyMatch(data -> data.getUsers() != null && nrp.equalsIgnoreCase(data.getUsers())
										&& !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
								&& index == (rfqOpt.get().getCurrentApprovalStage() != null
										? rfqOpt.get().getCurrentApprovalStage()
										: 0)) {
							result = ApprovalStatus.NEED_APPROVAL;
						}

					}
				}
			}

			return result;
		} else {
			throw new ModuleException("RFQ " + dto.id + " not found");
		}
	}

	public byte[] download(Quotation dto) throws Exception {

		Optional<Quotation> rfqOpt = quotationRepository.findByIdOptional(dto.id);
		if (rfqOpt.isPresent()) {
			try {
				Supplier supplier = rfqOpt.get().getSuppliers().stream().findFirst().orElse(null);

				return null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error reading file", e);
			}
		} else {
			throw new ModuleException("File ID " + dto.id + " not found");
		}
	}

	@Override
	public void update(Quotation entity) {
		quotationRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		quotationRepository.deleteById(id);
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	}

}
