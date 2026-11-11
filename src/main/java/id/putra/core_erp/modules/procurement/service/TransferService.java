package id.putra.core_erp.modules.procurement.service;

import java.util.List;
import id.putra.core_erp.modules.procurement.model.Transfer;
import id.putra.core_erp.modules.procurement.repository.TransferRequestRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferService implements BaseService<Transfer> {

	private final TransferRequestRepo transferRequestRepo;

	public TransferService(TransferRequestRepo transferRequestRepo) {
		this.transferRequestRepo = transferRequestRepo;
	}

	public void add(Transfer dto) {
		transferRequestRepo.persist(dto);
	}

	@Override
	public void save(Transfer entity) {
		transferRequestRepo.persist(entity);
	}

	public List<Transfer> findAll(Transfer dto) {
		String status = (dto != null && dto != null && dto.status != null && !dto.status.isBlank())
				? dto.status
				: "%%";
		String id = (dto != null && dto != null && dto.id != null) ? dto.id.toString() : "%%";

		return transferRequestRepo.findByStatusLikeAndIdLike(status, id);
	}

	@Override
	public List<Transfer> findAll(Transfer transfer, Page p) {
		return transferRequestRepo.findAll().list();
	}

	@Override
	public Transfer findById(Long id) {
		return transferRequestRepo.findById(id);
	}

	public Transfer getById(Transfer dto) {
		return transferRequestRepo.findById(dto.id);
	}

	@Override
	public void update(Transfer dto) {
		transferRequestRepo.persist(dto);
	}

	@Override
	public void delete(Long id) {
		transferRequestRepo.softDeleteById(id);
	}

}
