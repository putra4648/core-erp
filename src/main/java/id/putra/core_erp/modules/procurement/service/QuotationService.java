package id.putra.core_erp.modules.procurement.service;

import java.util.List;
import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.modules.procurement.repository.QuotationRepo;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class QuotationService implements BaseService<Quotation> {

	private final QuotationRepo quotationRepository;

	public QuotationService(QuotationRepo rfqRepository) {
		this.quotationRepository = rfqRepository;
	}

	@PersistenceContext
	private EntityManager entityManager;

	public void add(Quotation dto) {
		quotationRepository.persist(dto);
	}

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
	public List<Quotation> findAll(Quotation quotation, Page p) {
		return quotationRepository.findAll().list();
	}

	public Quotation getById(Long id) {
		return quotationRepository.findByIdOptional(id).get();
	}

	@Override
	public void update(Quotation entity) {
		quotationRepository.getEntityManager().merge(entity);
	}

	@Override
	public void delete(Long id) {
		quotationRepository.deleteById(id);
	}

}
