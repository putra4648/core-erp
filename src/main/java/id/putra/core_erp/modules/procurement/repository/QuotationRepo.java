package id.putra.core_erp.modules.procurement.repository;

import id.putra.core_erp.modules.procurement.model.Quotation;
import id.putra.core_erp.repository.SoftDeleteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuotationRepo implements SoftDeleteRepository<Quotation> {

	@Inject
	QuotationLineRepo quotationLineRepo;

	/** Quotation owns its lines — flag them with the header. */
	@Override
	public void cascadeToChildren(Quotation quotation) {
		quotationLineRepo.find("quotation.id", quotation.id).list()
				.forEach(quotationLineRepo::softDelete);
	}

}
