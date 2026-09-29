package id.putra.core_erp.modules.procurement.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.exceptions.ModuleException;
import id.putra.core_erp.modules.approval.model.Approval;
import id.putra.core_erp.modules.budget.repository.BudgetDetailRepo;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.repository.RequisitionRepo;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import id.putra.core_erp.service.BaseService;
import id.putra.core_erp.util.AppFileUtil;
import io.quarkus.panache.common.Page;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { Exception.class })
public class RequisitionService implements BaseService<Requisition> {

	private final BudgetDetailRepo budgetDetailRepo;
	private final DepartmentRepo departmentRepo;
	private final RequisitionRepo requisitionRepo;
	private final TransferRequestRepo transferRequestRepo;
	private final Vertx vertx;

	public RequisitionService(TransferRequestRepo transferRequestRepo, RequisitionRepo requisitionRepo,
			DepartmentRepo departmentRepo, BudgetDetailRepo budgetDetailRepo, Vertx vertx) {
		this.transferRequestRepo = transferRequestRepo;
		this.requisitionRepo = requisitionRepo;
		this.departmentRepo = departmentRepo;
		this.budgetDetailRepo = budgetDetailRepo;
		this.vertx = vertx;
	}

	public void add(Requisition entity) {
		requisitionRepo.persist(entity);
	}

	public Boolean checkApprovalFlow(Requisition dto) {
		Optional<Requisition> reqOpt = requisitionRepo.findByIdOptional(dto.getId());
		String nrp = "";

		if (reqOpt.isPresent()) {
			boolean result = false;
			if (reqOpt.get().getApprovals() != null) {
				result = reqOpt.get().getApprovals().stream().anyMatch(usr -> usr.getUsers() != null
						&& usr.getUsers() != null && usr.getUsers().equalsIgnoreCase(nrp));
			}

			if (reqOpt.get().getApproval() != null && reqOpt.get().getApproval().getUsers() != null) {
				result = !reqOpt.get().getApproval().getUsers().equalsIgnoreCase(nrp);
			}
			return result;
		} else {
			return false;
		}
	}

	public ApprovalStatus checkStatusApprovalIsPermanent(Requisition dto) {
		String nrp = "";

		Optional<Requisition> reqOpt = requisitionRepo.findByIdOptional(dto.getId());
		if (reqOpt.isPresent()) {
			ApprovalStatus result = ApprovalStatus.NON_APPROVAL;
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
			if (approvals.stream().anyMatch(data -> data.getUsers() != null && data.getUsers() != null
					&& data.getUsers().equalsIgnoreCase(nrp))) {
				if (approvals.stream().anyMatch(aprv -> aprv.getIsApprovalPermanent() != null)) {
					if (approvals.stream()
							.anyMatch(data -> data.getUsers() != null && data.getUsers() != null
									&& data.getUsers().equalsIgnoreCase(nrp)
									&& Boolean.TRUE.equals(data.getIsApprovalPermanent()))) {
						result = ApprovalStatus.ALREADY_APPROVED;
					}

					if (approvals.stream()
							.anyMatch(data -> data.getUsers() != null && data.getUsers() != null
									&& data.getUsers().equalsIgnoreCase(nrp)
									&& !Boolean.TRUE.equals(data.getIsApprovalPermanent()))
							&& index == (reqOpt.get().getCurrentApprovalStage() != null
									? reqOpt.get().getCurrentApprovalStage()
									: 0)) {
						result = ApprovalStatus.NEED_APPROVAL;
					}

				}
			}
			return result;
		} else {
			throw new ModuleException("Requisition ID " + dto.getId() + " not found");
		}
	}

	@Override
	public void delete(Long id) {
		requisitionRepo.deleteById(id);
	}

	public void delete(Requisition entity) {
		requisitionRepo.delete(entity);
	}

	public byte[] download(Requisition dto) throws IOException {
		String filename = "";

		Path path = Paths.get(AppFileUtil.getServerPath(), "data", "requisitions", filename);
		return Files.readAllBytes(path);

	}

	@Override
	public List<Requisition> findAll(Requisition r, Page p) {
		return requisitionRepo.findAll().list();
	}

	@Override
	public Requisition findById(Long id) {
		return requisitionRepo.findById(id);
	}

	@Override
	public void save(Requisition entity) {
		requisitionRepo.persist(entity);
	}

	@Override
	public void update(Requisition entity) {
		requisitionRepo.persist(entity);
	}

}
